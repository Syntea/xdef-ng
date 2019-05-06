package org.xdef.translator.document.xml.model

import org.jdom2.Element
import org.jdom2.located.LocatedElement
import org.jdom2.located.LocatedText
import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.XLeaf
import org.xdef.core.document.data.XNode

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXNode(
    internal val element: Element
) : XNode(
    name = element.qualifiedName,
    attributes = element.attributes.map { XmlXAttribute(name = it.qualifiedName, value = XmlTextXValue(it.value)) },
    children = element.content.filter { it is LocatedElement || it is LocatedText }.mapIndexed { index, child ->
        when (child) {
            is LocatedElement -> XmlXNode(child)
            is LocatedText -> XmlXLeaf(
                name = "${element.qualifiedName}[$index]",
                value = XmlTextXValue(child.text),
                location = Location(child.line, child.column)
            )
            else -> throw IllegalArgumentException("Missing specified type: ${child::class.simpleName}")
        }
    },
    location = if (element is LocatedElement) Location(element.line, element.column) else Location.NO_LOCATION
)

class XmlXLeaf(
    name: String,
    value: XmlTextXValue,
    location: Localizable
) : XLeaf(name, value, location)
