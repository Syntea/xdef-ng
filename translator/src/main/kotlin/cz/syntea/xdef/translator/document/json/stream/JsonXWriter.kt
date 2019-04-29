package cz.syntea.xdef.translator.document.json.stream

import com.fasterxml.jackson.core.JsonFactory
import cz.syntea.xdef.core.document.stream.*
import cz.syntea.xdef.translator.document.json.model.JsonXBoolean
import cz.syntea.xdef.translator.document.json.model.JsonXNumber
import org.apache.logging.log4j.kotlin.Logging
import java.io.Writer
import java.util.*

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonXWriter(writer: Writer) : XWriter, Logging {

    private val factory = JsonFactory()
    private val writer = factory.createGenerator(writer)
    private val structureStack = Stack<JsonStructure>()

    override fun writeEvent(event: XEvent) {
        when (event) {
            is StartJsonObjectNodeEvent -> {
                if (JsonStructure.OBJECT == structureStack.peek()) writer.writeFieldName(event.name)
                writer.writeStartObject()
                structureStack.push(JsonStructure.OBJECT)
            }
            is EndJsonObjectNodeEvent -> {
                writer.writeEndObject()
                structureStack.pop()
            }
            is StartJsonArrayNodeEvent -> {
                if (JsonStructure.OBJECT == structureStack.peek()) writer.writeFieldName(event.name)
                writer.writeStartArray()
                structureStack.push(JsonStructure.ARRAY)
            }
            is EndJsonArrayNodeEvent -> {
                writer.writeEndArray()
                structureStack.pop()
            }


            is StartDocumentEvent -> structureStack.push(JsonStructure.DOCUMENT)
            is EndDocumentEvent -> {
                structureStack.pop()
                writer.flush()
            }
            is StartNodeEvent -> {
            }
            is EndNodeEvent -> {
            }
            is AttributeEvent -> {
                writer.writeFieldName(event.attribute.name)
                when (event.attribute.value) {
                    is JsonXNumber -> writer.writeNumber((event.attribute.value as JsonXNumber).value)
                    is JsonXBoolean -> writer.writeBoolean((event.attribute.value as JsonXBoolean).typedValue)
                    else -> writer.writeString(event.attribute.value?.value) // if it is null write as null
                }
            }
            is ValueEvent -> {
                when (event.value) {
                    is JsonXNumber -> writer.writeNumber((event.value as JsonXNumber).value)
                    is JsonXBoolean -> writer.writeBoolean((event.value as JsonXBoolean).typedValue)
                    else -> writer.writeString(event.value?.value)
                }

            }
            is NotProcessedEvent -> {
                // ignored
                logger.info("NotProcessedEvent was ignored")
            }
        }
    }

    override fun close() = writer.close()

    override fun flush() = writer.flush()
}
