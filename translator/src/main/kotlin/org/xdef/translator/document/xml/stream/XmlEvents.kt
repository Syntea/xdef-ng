package org.xdef.translator.document.xml.stream

import org.xdef.core.Location
import org.xdef.core.document.data.LocalizedXAttribute
import org.xdef.core.document.stream.*
import org.xdef.translator.document.xml.model.XmlTextXValue
import javax.xml.stream.events.*

/**
 * File contains specified events for XML document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Event signalises start XML document
 * It's wrapper [StartDocument]
 */
data class StartXmlDocumentEvent(internal val startDocument: StartDocument) : StartDocumentEvent(
    lineNumber = startDocument.location?.lineNumber ?: Location.NO_LOCATION.lineNumber,
    columnNumber = startDocument.location?.columnNumber ?: Location.NO_LOCATION.columnNumber
)

/**
 * Event signalises start XML element
 * It's wrapper [StartElement]
 */
data class StartElementNodeEvent(internal val startElement: StartElement) : StartNodeEvent(
    name = startElement.name.toString(),
    lineNumber = startElement.location?.lineNumber ?: Location.NO_LOCATION.lineNumber,
    columnNumber = startElement.location?.columnNumber ?: Location.NO_LOCATION.columnNumber
)

/**
 * Event signalises end XML element
 * It's wrapper [EndElement]
 */
data class EndElementNodeEvent(internal val endElement: EndElement) : EndNodeEvent(
    name = endElement.name.toString(),
    lineNumber = endElement.location?.lineNumber ?: Location.NO_LOCATION.lineNumber,
    columnNumber = endElement.location?.lineNumber ?: Location.NO_LOCATION.columnNumber
)

/**
 * Event signalises XML text value
 * It's wrapper [Characters]
 */
data class XmlValueEvent(internal val data: Characters) : ValueEvent(
    value = XmlTextXValue(data.data),
    lineNumber = data.location?.lineNumber ?: Location.NO_LOCATION.lineNumber,
    columnNumber = data.location?.lineNumber ?: Location.NO_LOCATION.columnNumber
)

/**
 * Event signalises other special XML value which not used in processing
 *
 * @param notProcessedEvent Special not processing value
 */
data class XmlNotProcessedEvent(val notProcessedEvent: XMLEvent) : NotProcessedEvent()

/**
 * Special class for XML attribute which it's part of [StartElement]
 */
data class XmlEventAttribute(
    internal val attribute: Attribute
) : LocalizedXAttribute(
    attribute.name.toString(),
    XmlTextXValue(attribute.value)
)
