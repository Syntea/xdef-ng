package org.xdef.translator.document.json.model

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
sealed class JsonXNode(
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : XNode(name, attributes, children, location)

class JsonObjectXNode(
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : JsonXNode(name, attributes, children, location)

class JsonArrayXNode(
    name: String,
    children: List<XTree>,
    location: Localizable
) : JsonXNode(name, emptyList(), children, location)

class JsonXLeaf(
    name: String,
    value: JsonXValue<*>?,
    location: Localizable
) : XLeaf(name, value, location)
