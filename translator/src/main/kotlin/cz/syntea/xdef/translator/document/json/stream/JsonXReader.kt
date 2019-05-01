package cz.syntea.xdef.translator.document.json.stream

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.stream.*
import cz.syntea.xdef.translator.document.json.indexedName
import cz.syntea.xdef.translator.document.json.model.JsonXAttribute
import cz.syntea.xdef.translator.document.json.model.JsonXBoolean
import cz.syntea.xdef.translator.document.json.model.JsonXNumber
import cz.syntea.xdef.translator.document.json.model.JsonXString
import cz.syntea.xdef.translator.peekOrNull
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.util.*

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonXReader private constructor(private val reader: JsonParser) : XReader {

    constructor(input: InputStream) : this(JsonFactory().createParser(input))
    constructor(input: Reader) : this(JsonFactory().createParser(input))

    //TODO Maybe used low-level array
    private val nameStack = Stack<String>()
    private val structureStack = Stack<JsonStructure>()
    private val indexStack = Stack<Int>()

    private var peekedEvent: XEvent? = null

    init {
        structureStack.push(JsonStructure.DOCUMENT)
        nameStack.push("root")

        peekedEvent = StartDocumentEvent(Location(1, 1))
    }

    override fun hasNext() = !structureStack.empty()

    override fun next(): XEvent {
        val event = peek()

        peekedEvent = null

        // Because of consuming EndDocumentEvent event
        if (event is EndDocumentEvent) structureStack.pop()
        return event
    }

    override fun peek() = peekedEvent ?: doPeek()

    override fun close() {
        reader.close()
        nameStack.clear()
        structureStack.clear()
        peekedEvent = null
    }

    private fun doPeek(): XEvent {
        structureStack.peekOrNull().takeIf { JsonStructure.ARRAY == it }?.let {
            val index = indexStack.pop()
            nameStack.push(indexedName(nameStack.peek(), index))
            indexStack.push(index + 1)
        }

        val location = Location(
            lineNumber = reader.tokenLocation.lineNr,
            columnNumber = reader.tokenLocation.columnNr
        )

        return when (reader.nextToken()) {
            JsonToken.START_ARRAY -> {
                structureStack.push(JsonStructure.ARRAY)
                indexStack.push(0)
                StartJsonArrayNodeEvent(
                    name = nameStack.peek(),
                    location = location
                )
            }
            JsonToken.END_ARRAY -> {
                structureStack.pop()
                indexStack.pop()
                EndJsonArrayNodeEvent(
                    name = nameStack.pop(),
                    location = location
                )
            }
            JsonToken.START_OBJECT -> {
                structureStack.push(JsonStructure.OBJECT)
                StartJsonObjectNodeEvent(
                    name = nameStack.peek(),
                    location = location
                )
            }
            JsonToken.END_OBJECT -> {
                structureStack.pop()
                EndJsonObjectNodeEvent(
                    name = nameStack.pop(),
                    location = location
                )
            }
            // TODO location name
            JsonToken.FIELD_NAME -> {
                nameStack.push(reader.currentName)
//                reader.nextToken() // consume it
                doPeek()
            }
            JsonToken.VALUE_STRING,
            JsonToken.VALUE_NUMBER_INT,
            JsonToken.VALUE_NUMBER_FLOAT,
            JsonToken.VALUE_TRUE,
            JsonToken.VALUE_FALSE,
            JsonToken.VALUE_NULL -> doPeekPrimitive()
            JsonToken.VALUE_EMBEDDED_OBJECT,
            JsonToken.NOT_AVAILABLE -> TODO()
            null -> EndDocumentEvent(location)
        }.also { peekedEvent = it }
    }

    private fun doPeekPrimitive(): XEvent {
        val location = Location(
            lineNumber = reader.tokenLocation.lineNr,
            columnNumber = reader.tokenLocation.columnNr
        )

        val value = when (reader.currentToken()) {
            JsonToken.VALUE_STRING -> JsonXString(reader.valueAsString, location)
            JsonToken.VALUE_NUMBER_INT,
            JsonToken.VALUE_NUMBER_FLOAT -> JsonXNumber(
                BigDecimal(reader.valueAsString),
                Location.NO_LOCATION
            )
            JsonToken.VALUE_TRUE -> JsonXBoolean(true, location)
            JsonToken.VALUE_FALSE -> JsonXBoolean(false, location)
            JsonToken.VALUE_NULL -> null
            else -> throw AssertionError()
        }

        return when (structureStack.peek()!!) {
            JsonStructure.OBJECT -> AttributeEvent(
                attribute = JsonXAttribute(
                    name = nameStack.pop(),
                    value = value
                ),
                location = location
            )
            JsonStructure.DOCUMENT,
            JsonStructure.ARRAY -> {
                nameStack.pop()
                ValueEvent(
                    value = value,
                    location = location
                )
            }
        }
    }
}
