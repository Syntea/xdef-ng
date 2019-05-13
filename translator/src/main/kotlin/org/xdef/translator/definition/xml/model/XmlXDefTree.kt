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
 * File contains special XML implementation for tree of X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Node for XML element
 */
class XmlXDefNode(
    /**
     * Reference to XML document object model ([Element])
     * Using for preserve not processing information
     */
    internal val element: Element,
    name: String,
    script: String?,
    attributes: List<XDefAttribute>,
    children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefNode(name, script, attributes, children, allowedOccurrences, allowedEvents, location)

/**
 * Leaf for XML text value
 */
class XmlXDefLeaf(
    /**
     * Reference to XML document object model ([Text])
     */
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
