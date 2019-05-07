package org.xdef.translator.definition.xml.model

import org.jdom2.Element
import org.jdom2.Text
import org.xdef.core.Location
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.core.document.definition.XDefLeaf
import org.xdef.core.document.definition.XDefNode
import org.xdef.core.document.definition.XDefTree
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence
import org.xdef.translator.document.xml.model.XmlTextXValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXDefNode(
    internal val element: Element,
    name: String,
    script: String?,
    attributes: List<XDefAttribute>,
    children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefNode(name, script, attributes, children, allowedOccurrences, allowedEvents, location)

class XmlXDefLeaf(
    internal val text: Text,
    name: String,
    value: XmlTextXValue,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefLeaf(
    name,
    value,
    allowedOccurrences,
    allowedEvents,
    location
)
