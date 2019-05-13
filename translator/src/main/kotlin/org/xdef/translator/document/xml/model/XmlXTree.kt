package org.xdef.translator.document.xml.model

import org.jdom2.Element
import org.jdom2.Text
import org.xdef.core.Localizable
import org.xdef.core.document.data.XAttribute
import org.xdef.core.document.data.XLeaf
import org.xdef.core.document.data.XNode
import org.xdef.core.document.data.XTree

/**
 * File contains special XML implementation for tree of document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Node for XML element
 */
class XmlXNode(
    /**
     * Reference to XML document object model ([Element])
     * Using for preserve not processing information
     */
    internal val element: Element,
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : XNode(name, attributes, children, location)

/**
 * Leaf for XML text value
 */
class XmlXLeaf(
    /**
     * Reference to XML document object model ([Text])
     */
    internal val text: Text,
    name: String,
    value: XmlTextXValue,
    location: Localizable
) : XLeaf(name, value, location)
