package org.xdef.translator.document.xml.stream

import com.sun.xml.internal.stream.events.AttributeImpl
import com.sun.xml.internal.stream.events.CharacterEvent
import com.sun.xml.internal.stream.events.EndElementEvent
import com.sun.xml.internal.stream.events.StartElementEvent
import org.xdef.core.document.stream.*
import java.io.OutputStream
import java.io.Writer
import javax.xml.stream.XMLEventWriter
import javax.xml.stream.XMLOutputFactory

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXWriter private constructor(private val writer: XMLEventWriter) : XWriter {

    constructor(output: OutputStream) : this(XMLOutputFactory.newFactory().createXMLEventWriter(output))
    constructor(output: Writer) : this(XMLOutputFactory.newFactory().createXMLEventWriter(output))

    override fun writeEvent(event: XEvent) {
        when (event) {
            is StartDocumentEvent -> writer.add(
                when (event) {
                    is StartXmlDocumentEvent -> event.startDocument
                    else -> com.sun.xml.internal.stream.events.StartDocumentEvent()
                }
            )
            is EndDocumentEvent -> {
                writer.add(com.sun.xml.internal.stream.events.EndDocumentEvent())
                writer.flush()
            }
            is StartNodeEvent -> writer.add(
                when (event) {
                    is StartElementNodeEvent -> event.startElement
                    else -> StartElementEvent("", null, event.name)
                }
            )
            is EndNodeEvent -> writer.add(
                when (event) {
                    is EndElementNodeEvent -> event.endElement
                    else -> EndElementEvent("", null, event.name)
                }
            )
            is AttributeEvent -> writer.add(
                event.attribute.let {
                    when (it) {
                        is XmlEventAttribute -> it.attribute
                        else -> AttributeImpl(it.name, it.value?.value.orEmpty())
                    }
                }
            )
            is ValueEvent -> writer.add(
                when (event) {
                    is XmlValueEvent -> event.data
                    else -> CharacterEvent(event.value?.value?.plus('\n'))
                }
            )
            is NotProcessedEvent -> writer.add(
                when (event) {
                    is XmlNotProcessedEvent -> event.notProcessedEvent
                    else -> TODO()
                }
            )
        }
    }

    override fun close() = writer.close()

    override fun flush() = writer.flush()
}
