package org.xdef.translator.definition.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import org.apache.logging.log4j.kotlin.Logging
import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.XValue
import org.xdef.core.document.definition.*
import org.xdef.core.toLocation
import org.xdef.translator.JSON_ROOT_NODE_NAME
import org.xdef.translator.definition.DefinitionTranslator
import org.xdef.translator.definition.DefinitionTranslatorIO
import org.xdef.translator.definition.json.model.JsonArrayXDefNode
import org.xdef.translator.definition.json.model.JsonObjectXDefNode
import org.xdef.translator.definition.json.model.JsonXDefDocument
import org.xdef.translator.definition.json.model.JsonXDefLeaf
import org.xdef.translator.document.json.model.JsonXBoolean
import org.xdef.translator.document.json.model.JsonXNumber
import org.xdef.translator.document.json.model.JsonXString
import org.xdef.translator.document.json.model.JsonXValue
import org.xdef.translator.document.json.std.*
import org.xdef.translator.indexedName
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import javax.json.JsonNumber
import javax.json.JsonObject
import javax.json.JsonString
import javax.json.JsonValue

/**
 * JSON X-definition document translator
 * It translates tree form of document to inner X-definition document representation and back
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 *
 * @see DefinitionTranslator
 * @see DefinitionTranslatorIO
 */
class JsonDefinitionTranslator : DefinitionTranslator<JsonValue>, DefinitionTranslatorIO, Logging {

    companion object {
        const val X_SCRIPT_IDENTIFIER = "xd:script"
    }

    private val factory = JsonFactory()

    override fun translate(dom: JsonValue) = JsonXDefDocument(this.jsonValue2XDefTree(JSON_ROOT_NODE_NAME, dom))

    override fun translate(definition: XDefDocument): JsonValue {
        // Name was add
        return if (JSON_ROOT_NODE_NAME == definition.root.name) {
            xDefTree2JsonValue(definition.root)
        } else {
            LocalizedJsonObject(mapOf(definition.root.name to xDefTree2JsonValue(definition.root)))
        }
    }

    override fun readDefinition(input: InputStream) = readDefinition(factory.createParser(input))

    override fun readDefinition(input: Reader) = readDefinition(factory.createParser(input))

    override fun writeDefinition(definition: XDefDocument, output: OutputStream) =
        writeDefinition(definition, factory.createGenerator(output))

    override fun writeDefinition(definition: XDefDocument, output: Writer) =
        writeDefinition(definition, factory.createGenerator(output))

    private fun readDefinition(reader: JsonParser): XDefDocument {
        val jsonValue = LocalizedJsonTree.readTree(reader)
        return translate(requireNotNull(jsonValue) { "Parsed definition is empty" })
    }

    private fun writeDefinition(document: XDefDocument, writer: JsonGenerator) {
        val jsonValue = translate(document)
        writer.useDefaultPrettyPrinter() // TODO: Only debug
        LocalizedJsonTree.writeTree(writer, jsonValue)
        writer.flush()
    }

    @Throws(IllegalArgumentException::class)
    private fun jsonValue2XDefTree(nodeName: String, value: JsonValue): XDefTree {
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

                val (scripts, attrs) = attributes.partition { (name, _) -> X_SCRIPT_IDENTIFIER == name }
                val script = when (scripts.size) {
                    0 -> null
                    1 -> scripts[0].value.toString()
                    else -> {
                        logger.warn("Definition has more script declarations, first log is used")
                        scripts[0].value.toString()
                    }
                }
                JsonObjectXDefNode(
                    name = nodeName,
                    script = script,
                    attributes = attrs.map { (name, value) ->
                        XDefAttribute(
                            name = name,
                            value = jsonValue2XValue(value),
                            allowedOccurrences = emptyList(), // TODO
                            allowedEvents = emptyList(), // TODO
                            location = Location.NO_LOCATION // FIXME
                        )
                    },
                    children = children.map { (name, child) -> this.jsonValue2XDefTree(name, child) }.toList(),
                    allowedOccurrences = emptyList(), // TODO
                    allowedEvents = emptyList(), // TODO
                    location = value.extractLocation()
                )
            }
            JsonValue.ValueType.ARRAY -> JsonArrayXDefNode(
                name = nodeName,
                script = null, // JSON array hasn't script
                children = value.asJsonArray().mapIndexed { index, child ->
                    this.jsonValue2XDefTree(
                        nodeName = indexedName(nodeName, index),
                        value = child
                    )
                },
                allowedOccurrences = emptyList(), // TODO
                allowedEvents = emptyList(), // TODO
                location = value.extractLocation()
            )
            JsonValue.ValueType.STRING,
            JsonValue.ValueType.NUMBER,
            JsonValue.ValueType.TRUE,
            JsonValue.ValueType.FALSE,
            JsonValue.ValueType.NULL -> JsonXDefLeaf(
                name = nodeName,
                value = jsonValue2XValue(value),
                allowedOccurrences = emptyList(), // TODO
                allowedEvents = emptyList(), // TODO
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

    private fun xDefTree2JsonValue(tree: XDefTree): JsonValue {
        return when (tree) {
            is XDefLeaf -> xValue2JsonValue(tree.value)
            is XDefNode -> when (tree) {
                // Attributes is ignored!
                is JsonArrayXDefNode -> LocalizedJsonArray(
                    valueList = tree.children.map { xDefTree2JsonValue(it) }
                )
                is JsonObjectXDefNode -> LocalizedJsonObject(
                    valueMap = (
                            tree.attributes.map { it.name to xValue2JsonValue(it.value) }
                                    + tree.children.map { it.name to xDefTree2JsonValue(it) }
                                    // Add script as object attribute
                                    + if (tree.script != null) listOf(X_SCRIPT_IDENTIFIER to LocalizedJsonString(tree.script!!)) else emptyList()
                            ).toMap()
                )
                else -> LocalizedJsonObject(
                    valueMap = (tree.attributes.map { it.name to xValue2JsonValue(it.value) }
                            // Add script as object attribute
                            + if (tree.script != null) listOf(X_SCRIPT_IDENTIFIER to LocalizedJsonString(tree.script!!)) else emptyList<Pair<String, JsonValue>>()
                            + tree.children
                        .groupBy { it.name }
                        .map { (name, children) ->
                            if (children.size == 1) {
                                name to xDefTree2JsonValue(children.single())
                            } else {
                                name to LocalizedJsonArray(valueList = children.map { xDefTree2JsonValue(it) })
                            }
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
