package org.xdef.translator.document.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import org.apache.logging.log4j.kotlin.Logging
import org.xdef.core.document.data.*
import org.xdef.translator.JSON_ROOT_NODE_NAME
import org.xdef.translator.document.DocumentTranslator
import org.xdef.translator.document.DocumentTranslatorIO
import org.xdef.translator.document.json.model.*
import org.xdef.translator.document.json.std.*
import org.xdef.translator.document.json.stream.JsonXReader
import org.xdef.translator.document.json.stream.JsonXWriter
import org.xdef.translator.extractLocation
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
 * JSON document translator
 * It translates tree form of document to inner document representation and back
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 *
 * @see [DocumentTranslator]
 * @see [DocumentTranslatorIO]
 */
class JsonDocumentTranslator : DocumentTranslator<JsonValue>, Logging {

    private val factory = JsonFactory()

    override fun translate(dom: JsonValue) = JsonXDocument(jsonValue2XTree(JSON_ROOT_NODE_NAME, dom))

    override fun translate(document: XDocument) = translate(document.root)

    override fun translate(documentTree: XTree): JsonValue {
        // Name was add
        return if (JSON_ROOT_NODE_NAME == documentTree.name) {
            xTree2JsonValue(documentTree)
        } else {
            LocalizedJsonObject(mapOf(documentTree.name to xTree2JsonValue(documentTree)))
        }
    }

    override fun readDocument(input: InputStream) = readDocument(factory.createParser(input))

    override fun readDocument(input: Reader) = readDocument(factory.createParser(input))

    override fun writeDocument(document: XDocument, output: OutputStream) =
        writeDocument(document, factory.createGenerator(output))

    override fun writeDocument(document: XDocument, output: Writer) =
        writeDocument(document, factory.createGenerator(output))

    override fun createTranslationReader(input: InputStream) = JsonXReader(input)

    override fun createTranslationReader(input: Reader) = JsonXReader(input)

    override fun createTranslationWriter(output: Writer) = JsonXWriter(output)

    override fun createTranslationWriter(output: OutputStream) = JsonXWriter(output)

    private fun readDocument(reader: JsonParser): XDocument {
        val jsonValue = LocalizedJsonTree.readTree(reader)
        return translate(requireNotNull(jsonValue) { "Parsed document is empty" })
    }

    private fun writeDocument(document: XDocument, writer: JsonGenerator) {
        val jsonValue = translate(document)
        writer.useDefaultPrettyPrinter() // TODO: Only debug
        LocalizedJsonTree.writeTree(writer, jsonValue)
        writer.flush()
    }

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
                            value = jsonValue2XValue(value)
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
                            if (children.size == 1) {
                                name to xTree2JsonValue(children.single())
                            } else {
                                name to LocalizedJsonArray(valueList = children.map { xTree2JsonValue(it) })
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
}
