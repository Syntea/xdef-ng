package cz.syntea.xdef.translator.document.xml.stream

import cz.syntea.xdef.core.document.data.LocalizedXAttribute
import cz.syntea.xdef.core.document.stream.*
import cz.syntea.xdef.translator.document.xml.model.XmlTextXValue
import javax.xml.stream.events.*

/**
 * File contains specified events for XML document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 *
 */
data class StartXmlDocumentEvent(internal val startDocument: StartDocument) : StartDocumentEvent(
    lineNumber = startDocument.location.lineNumber,
    columnNumber = startDocument.location.columnNumber
)

/**
 *
 */
data class StartElementNodeEvent(internal val startElement: StartElement) : StartNodeEvent(
    name = startElement.name.toString(),
    lineNumber = startElement.location.lineNumber,
    columnNumber = startElement.location.columnNumber
)

/**
 *
 */
data class EndElementNodeEvent(internal val endElement: EndElement) : EndNodeEvent(
    name = endElement.name.toString(),
    lineNumber = endElement.location.lineNumber,
    columnNumber = endElement.location.columnNumber
)

/**
 *
 */
data class XmlValueEvent(internal val data: Characters) : ValueEvent(
    value = XmlTextXValue(data.data),
    lineNumber = data.location.lineNumber,
    columnNumber = data.location.columnNumber
)

/**
 * @param notProcessedEvent
 */
data class XmlNotProcessedEvent(val notProcessedEvent: XMLEvent) : NotProcessedEvent()

/**
 *
 */
data class XmlEventAttribute(
    internal val attribute: Attribute
) : LocalizedXAttribute(
    attribute.name.toString(),
    XmlTextXValue(attribute.value)
)
