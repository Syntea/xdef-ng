package cz.syntea.xdef.translator.document.xml.model

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.XLeaf
import cz.syntea.xdef.core.document.data.XNode
import org.jdom2.located.LocatedElement
import org.jdom2.located.LocatedText

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXNode(
    internal val element: LocatedElement
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
    location = Location(element.line, element.column)
)

class XmlXLeaf(
    name: String,
    value: XmlTextXValue,
    location: Localizable
) : XLeaf(name, value, location)
