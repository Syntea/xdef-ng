package cz.syntea.xdef.translator.document.xml.stream

import com.sun.xml.internal.fastinfoset.stax.events.Util
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.stream.AttributeEvent
import cz.syntea.xdef.core.document.stream.EndDocumentEvent
import cz.syntea.xdef.core.document.stream.XEvent
import cz.syntea.xdef.core.document.stream.XReader
import org.apache.logging.log4j.kotlin.Logging
import java.io.Reader
import java.util.*
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXReader(reader: Reader) : XReader, Logging {

    private val inputFactory = XMLInputFactory.newFactory()
    private val eventFactory = XMLEventFactory.newFactory()
    private val reader = inputFactory.createXMLEventReader(reader)

    private val processedAttributes = LinkedList<AttributeEvent>()

    private var peekedEvent: XEvent? = null

    override fun peek() = peekedEvent ?: doPeek()

    override fun hasNext() = reader.hasNext()

    override fun next(): XEvent {
        val event = peek()

        peekedEvent = null
        return event
    }

    override fun close() = reader.close()

    private fun doPeek(): XEvent {
        return if (processedAttributes.isNotEmpty()) {
            logger.trace("Next attribute event")
            processedAttributes.poll()
        } else {
            reader.nextEvent().let { event ->
                logger.trace { "Peeked event: ${Util.getEventTypeString(event.eventType)}" }
                when (event.eventType) {
                    XMLStreamConstants.START_DOCUMENT -> StartXmlDocumentEvent(
                        event as StartDocument
                    )
                    XMLStreamConstants.END_DOCUMENT -> EndDocumentEvent(
                        event.location.lineNumber,
                        event.location.columnNumber
                    )
                    XMLStreamConstants.START_ELEMENT -> {
                        event.asStartElement().let { startElement ->
                            processedAttributes.addAll(
                                startElement
                                    .attributes
                                    .asSequence()
                                    .map {
                                        AttributeEvent(
                                            XmlEventAttribute(it as Attribute),
                                            Location.NO_LOCATION.lineNumber,
                                            Location.NO_LOCATION.columnNumber
                                        )
                                    }
                            )
                            StartElementNodeEvent(
                                eventFactory
                                    .apply { setLocation(startElement.location) }
                                    .createStartElement(startElement.name, null, startElement.namespaces)
                            )
                        }
                    }
                    XMLStreamConstants.END_ELEMENT -> EndElementNodeEvent(
                        event.asEndElement()
                    )
                    // It is consuming and forward in StartElement event
                    // XMLStreamConstants.NAMESPACE
                    // XMLStreamConstants.ATTRIBUTE
                    XMLStreamConstants.CHARACTERS -> XmlValueEvent(event.asCharacters())
                    // It is forwarded as Characters event
                    // XMLStreamConstants.SPACE
                    // XMLStreamConstants.CDATA
                    XMLStreamConstants.PROCESSING_INSTRUCTION,
                    XMLStreamConstants.COMMENT,
                    XMLStreamConstants.ENTITY_REFERENCE,
                    XMLStreamConstants.DTD,
                    XMLStreamConstants.NOTATION_DECLARATION,
                    XMLStreamConstants.ENTITY_DECLARATION -> XmlNotProcessedEvent(
                        event
                    )
                    else -> throw IllegalStateException("Unexpected event: ${event::class.simpleName}")
                }
            }.also { logger.trace { "Translated as ${it.javaClass.simpleName} event" } }
        }
    }
}
