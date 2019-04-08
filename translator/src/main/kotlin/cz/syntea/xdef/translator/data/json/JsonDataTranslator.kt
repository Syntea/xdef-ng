package cz.syntea.xdef.translator.data.json

import com.google.gson.*
import com.google.gson.internal.LazilyParsedNumber
import cz.syntea.xdef.model.DataNode
import cz.syntea.xdef.model.Document
import cz.syntea.xdef.translator.data.DataTranslator
import cz.syntea.xdef.translator.data.json.JsonDataTranslator.Companion.VALUE_SERIALIZED_NAME
import cz.syntea.xdef.translator.data.json.model.*
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonDataTranslator : DataTranslator {

    companion object {
        const val ROOT_NODE_NAME = "cz.syntea.xdef.root"
        const val VALUE_SERIALIZED_NAME = "value"
    }

    private val parser = JsonParser()
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override fun readAsDocument(input: InputStream): Document {
        val elem = parser.parse(input.reader())
        return JsonDocument(root = createNode(ROOT_NODE_NAME, elem, null))
    }

    override fun writeDocument(document: Document, output: OutputStream) {
        output.writer().also { out ->
            gson.toJson(
                when (document) {
                    is JsonDocument -> convertToJsonTree(document)
                    else -> convertToJsonTree(document)
                },
                out
            )
        }.flush()
    }

    @Throws(IllegalArgumentException::class)
    private fun createNode(elementName: String, element: JsonElement, parent: DataNode?): DataNode {
        return when (element) {
            is JsonObject -> JsonObjectDataNode(
                name = elementName,
                parent = parent
            ).also { it.children = element.entrySet().map { child -> createNode(child.key, child.value, it) } }
            is JsonArray -> JsonArrayDataNode(
                name = elementName,
                parent = parent
            ).also {
                it.children = element.map { child -> createNode(child.javaClass.name, child, it) }
            }
            is JsonPrimitive -> when {
                element.isBoolean -> JsonBooleanDataNode(name = elementName, value = element.asString, parent = parent)
                element.isNumber -> JsonNumberDataNode(name = elementName, value = element.asString, parent = parent)
                element.isString -> JsonStringDataNode(name = elementName, value = element.asString, parent = parent)
                else -> throw IllegalArgumentException("Unknown JSON primitive: $element")
            }
            is JsonNull -> JsonStringDataNode(
                name = elementName,
                value = null,
                parent = parent
            )
            else -> throw IllegalArgumentException("Unknown JSON element: $element")
        }
    }

    private fun convertToJsonTree(document: JsonDocument): JsonElement {
        return when (document.root) {
            is JsonDataNode -> document.root.toJsonElement()
            else -> document.root.toJsonObject()
        }
    }

    private fun convertToJsonTree(document: Document): JsonElement {
        return JsonObject().apply {
            add(document.root.name, document.root.toJsonObject())
        }
    }
}

private fun JsonDataNode.toJsonElement(): JsonElement {
    return when (this) {
        is JsonObjectDataNode -> JsonObject().also {
            children.forEach { child ->
                it.add(
                    child.name,
                    (child as JsonDataNode).toJsonElement()
                )
            }
        }
        is JsonArrayDataNode -> JsonArray().also { children.forEach { child -> it.add((child as JsonDataNode).toJsonElement()) } }
        is JsonBooleanDataNode -> value?.let { JsonPrimitive(it.toBoolean()) } ?: JsonNull.INSTANCE
        is JsonNumberDataNode -> value?.let { JsonPrimitive(LazilyParsedNumber(it)) } ?: JsonNull.INSTANCE
        is JsonStringDataNode -> value?.let { JsonPrimitive(it) } ?: JsonNull.INSTANCE
    }
}

private fun DataNode.toJsonObject(): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty(VALUE_SERIALIZED_NAME, value)
    attributes.forEach { name, value -> jsonObject.addProperty(name, value) }
    children.groupBy { it.name }.forEach { name, items -> jsonObject.add(name, items.toJsonArray()) }

    return jsonObject
}

private fun List<DataNode>.toJsonArray(): JsonArray {
    val jsonArray = JsonArray()
    forEach { jsonArray.add(it.toJsonObject()) }
    return jsonArray
}
