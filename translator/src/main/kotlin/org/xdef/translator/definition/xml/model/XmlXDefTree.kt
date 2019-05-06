package org.xdef.translator.definition.xml.model

import org.jdom2.located.LocatedElement
import org.jdom2.located.LocatedText
import org.xdef.core.Location
import org.xdef.core.document.definition.XDefLeaf
import org.xdef.core.document.definition.XDefNode
import org.xdef.translator.document.xml.model.XmlTextXValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXDefNode(
    script: String,
    internal val element: LocatedElement
) : XDefNode(
    name = element.qualifiedName,
    script = script,
    attributes = element.attributes.map {
        XmlXDefAttribute(
            name = it.qualifiedName,
            value = XmlTextXValue(it.value)
        )
    },
    children = element.content.filter { it is LocatedElement || it is LocatedText }.mapIndexed { index, child ->
        when (child) {
            is LocatedElement -> XmlXDefNode("TODO", child) // TODO
            is LocatedText -> XmlXDefLeaf(
                name = "${element.qualifiedName}[$index]",
                value = XmlTextXValue(child.text),
                location = Location(child.line, child.column)
            )
            else -> throw IllegalArgumentException("Missing specified type: ${child::class.simpleName}")
        }
    },
    allowedOccurrences = listOf(), // TODO Determine it
    allowedEvents = listOf(), // TODO Determine it
    location = Location(element.line, element.column)
)

class XmlXDefLeaf(
    name: String,
    value: XmlTextXValue,
    location: Location
) : XDefLeaf(
    name,
    value,
    listOf(), // TODO Determine it
    listOf(), // TODO Determine it
    location
)
