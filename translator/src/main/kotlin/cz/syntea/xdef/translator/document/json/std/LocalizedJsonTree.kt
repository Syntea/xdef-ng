package cz.syntea.xdef.translator.document.json.std

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import cz.syntea.xdef.core.Location
import org.apache.logging.log4j.kotlin.Logging
import javax.json.JsonNumber
import javax.json.JsonString
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
object LocalizedJsonTree : Logging {

    fun readTree(input: JsonParser): JsonValue? {
        return if (input.nextToken() == null) {
            logger.info("Readed document is empty")
            null
        } else {
            read(input)
        }
    }

    fun writeTree(output: JsonGenerator, value: JsonValue) {
        when (value.valueType!!) {
            JsonValue.ValueType.ARRAY -> {
                output.writeStartArray()
                value.asJsonArray().forEach { writeTree(output, it) }
                output.writeEndArray()
            }
            JsonValue.ValueType.OBJECT -> {
                output.writeStartObject()
                value.asJsonObject().forEach { (name, value) ->
                    output.writeFieldName(name)
                    writeTree(output, value)
                }
                output.writeEndObject()
            }
            JsonValue.ValueType.STRING -> output.writeString((value as JsonString).string)
            JsonValue.ValueType.NUMBER -> output.writeNumber((value as JsonNumber).bigDecimalValue())
            JsonValue.ValueType.TRUE -> output.writeBoolean(true)
            JsonValue.ValueType.FALSE -> output.writeBoolean(false)
            JsonValue.ValueType.NULL -> output.writeNull()
        }
    }

    private fun read(input: JsonParser): JsonValue {
        val lineNumber = input.tokenLocation.lineNr
        val columnNumber = input.tokenLocation.columnNr

        return when (input.currentToken()!!) {
            JsonToken.START_ARRAY -> {
                val children = mutableListOf<JsonValue>()
                while (JsonToken.END_ARRAY != input.nextToken()) {
                    children.add(read(input))
                }
                return LocalizedJsonArray(children, lineNumber, columnNumber)
            }
            JsonToken.START_OBJECT -> {
                val children = mutableMapOf<String, JsonValue>()
                val childrenLocation = mutableMapOf<String, Location>()
                while (JsonToken.END_OBJECT != input.nextToken()) {
                    val line = input.tokenLocation.lineNr
                    val column = input.tokenLocation.columnNr

                    val name = input.currentName
                    input.nextToken() // consume it
                    childrenLocation[name] = Location(line, column)
                    children[name] = read(input)
                }
                return LocalizedJsonObject(children, childrenLocation, lineNumber, columnNumber)
            }
            JsonToken.VALUE_STRING -> LocalizedJsonString(input.valueAsString, lineNumber, columnNumber)
            JsonToken.VALUE_NUMBER_INT,
            JsonToken.VALUE_NUMBER_FLOAT -> LocalizedJsonNumber(input.valueAsString, lineNumber, columnNumber)
            JsonToken.VALUE_TRUE -> LocalizedJsonBoolean(true, lineNumber, columnNumber)
            JsonToken.VALUE_FALSE -> LocalizedJsonBoolean(false, lineNumber, columnNumber)
            JsonToken.VALUE_NULL -> LocalizedJsonNull(lineNumber, columnNumber)
            JsonToken.VALUE_EMBEDDED_OBJECT,
            JsonToken.FIELD_NAME,
            JsonToken.END_ARRAY,
            JsonToken.END_OBJECT,
            JsonToken.NOT_AVAILABLE -> throw IllegalStateException("Unexpected token '${input.currentToken}'")
        }
    }
}
