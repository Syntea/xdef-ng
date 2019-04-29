package cz.syntea.xdef.translator.document.json

import com.fasterxml.jackson.core.JsonFactory
import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.*
import cz.syntea.xdef.core.toLocation
import cz.syntea.xdef.translator.document.DocumentTranslator
import cz.syntea.xdef.translator.document.json.model.*
import cz.syntea.xdef.translator.document.json.std.*
import cz.syntea.xdef.translator.document.json.stream.JsonXReader
import cz.syntea.xdef.translator.document.json.stream.JsonXWriter
import org.apache.logging.log4j.kotlin.Logging
import java.io.Reader
import java.io.Writer
import javax.json.JsonNumber
import javax.json.JsonObject
import javax.json.JsonString
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonDocumentTranslator : DocumentTranslator<JsonValue>, Logging {

    companion object {
        const val ROOT_NODE_NAME = "cz.syntea.xdef.root"
    }

    private val factory = JsonFactory()

    override fun translate(dom: JsonValue) = JsonXDocument(jsonValue2XTree(ROOT_NODE_NAME, dom))

    override fun translate(document: XDocument) = xTree2JsonValue(document.root)

    override fun readDocument(input: Reader): XDocument {
        val jsonValue = LocalizedJsonTree.readTree(factory.createParser(input))
        return translate(requireNotNull(jsonValue) { "Parsed document is empty" })
    }

    override fun writeDocument(document: XDocument, output: Writer) {
        val jsonValue = translate(document)
        val jsonOutputter = factory.createGenerator(output)
            .useDefaultPrettyPrinter() // TODO: Only debug
        LocalizedJsonTree.writeTree(jsonOutputter, jsonValue)
        jsonOutputter.flush()
    }

    override fun createTranslationReader(input: Reader) = JsonXReader(input)

    override fun createTranslationWriter(output: Writer) = JsonXWriter(output)

    @Throws(IllegalArgumentException::class)
    private fun jsonValue2XTree(nodeName: String, value: JsonValue): XTree {
        return when (value.valueType!!) {
            JsonValue.ValueType.OBJECT -> {
                value as JsonObject
                val (attributes, children) = value.entries.partition { (_, value) ->
                    when (value.valueType!!) {
                        // structures
                        JsonValue.ValueType.OBJECT,
                        JsonValue.ValueType.ARRAY -> false
                        // primitives
                        JsonValue.ValueType.STRING,
                        JsonValue.ValueType.NUMBER,
                        JsonValue.ValueType.TRUE,
                        JsonValue.ValueType.FALSE,
                        JsonValue.ValueType.NULL -> true
                    }
                }
                JsonObjectXNode(
                    name = nodeName,
                    attributes = attributes.map { (name, value) ->
                        JsonXAttribute(
                            name = name,
                            value = jsonValue2XValue(value),
                            location = Location.NO_LOCATION // FIXME
                        )
                    },
                    children = children.map { (name, child) -> jsonValue2XTree(name, child) }.toList(),
                    location = value.extractLocation()
                )
            }
            JsonValue.ValueType.ARRAY -> JsonArrayXNode(
                name = nodeName,
                children = value.asJsonArray().mapIndexed { index, child ->
                    jsonValue2XTree(
                        nodeName = indexedName(nodeName, index),
                        value = child
                    )
                },
                location = value.extractLocation()
            )
            JsonValue.ValueType.STRING,
            JsonValue.ValueType.NUMBER,
            JsonValue.ValueType.TRUE,
            JsonValue.ValueType.FALSE,
            JsonValue.ValueType.NULL -> JsonXLeaf(
                name = nodeName,
                value = jsonValue2XValue(value),
                location = value.extractLocation()
            )
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun jsonValue2XValue(value: JsonValue): JsonXValue<*>? {
        return when (value.valueType!!) {
            JsonValue.ValueType.STRING -> JsonXString((value as JsonString).string, value.extractLocation())
            JsonValue.ValueType.NUMBER -> JsonXNumber((value as JsonNumber).bigDecimalValue(), value.extractLocation())
            JsonValue.ValueType.TRUE -> JsonXBoolean(true, value.extractLocation())
            JsonValue.ValueType.FALSE -> JsonXBoolean(false, value.extractLocation())
            JsonValue.ValueType.NULL -> null

            JsonValue.ValueType.ARRAY,
            JsonValue.ValueType.OBJECT -> throw IllegalArgumentException("Not JSON primitive: ${value::class.simpleName}")
        }
    }

    private fun xTree2JsonValue(tree: XTree): JsonValue {
        return when (tree) {
            is XLeaf -> xValue2JsonValue(tree.value)
            is XNode -> when (tree) {
                // Attributes is ignored!
                is JsonArrayXNode -> LocalizedJsonArray(
                    valueList = tree.children.map { xTree2JsonValue(it) }
                )
                is JsonObjectXNode -> LocalizedJsonObject(
                    valueMap = (
                            tree.attributes.map { it.name to xValue2JsonValue(it.value) } +
                                    tree.children.map { it.name to xTree2JsonValue(it) }
                            ).toMap()
                )
                else -> LocalizedJsonObject(
                    valueMap = (tree.attributes.map { it.name to xValue2JsonValue(it.value) }
                            + tree.children
                        .groupBy { it.name }
                        .map { (name, children) ->
                            name to LocalizedJsonArray(valueList = children.map { xTree2JsonValue(it) })
                        }).toMap()
                )
            }
        }
    }

    private fun xValue2JsonValue(value: XValue?): JsonValue {
        return when (value) {
            is JsonXValue<*> -> when (value) {
                is JsonXBoolean -> LocalizedJsonBoolean(value.typedValue)
                is JsonXNumber -> LocalizedJsonNumber(value.value)
                is JsonXString -> LocalizedJsonString(value.typedValue)
            }
            null -> JsonValue.NULL
            else -> LocalizedJsonString(value.value)
        }
    }

    private fun JsonValue.extractLocation() = if (this is Localizable) toLocation() else Location.NO_LOCATION
}
