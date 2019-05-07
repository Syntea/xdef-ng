package org.xdef.translator.document.xml.model

import org.jdom2.Element
import org.xdef.core.Localizable
import org.xdef.core.document.data.XAttribute
import org.xdef.core.document.data.XLeaf
import org.xdef.core.document.data.XNode
import org.xdef.core.document.data.XTree

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXNode(
    internal val element: Element,
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : XNode(name, attributes, children, location)

class XmlXLeaf(
    name: String,
    value: XmlTextXValue,
    location: Localizable
) : XLeaf(name, value, location)
