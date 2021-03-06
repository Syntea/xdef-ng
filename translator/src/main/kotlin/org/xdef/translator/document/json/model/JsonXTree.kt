package org.xdef.translator.document.json.model

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.XAttribute
import org.xdef.core.document.data.XLeaf
import org.xdef.core.document.data.XNode
import org.xdef.core.document.data.XTree

/**
 * File contains special JSON implementation for tree of document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Parent class defines restrict JSON structures hierarchy
 */
sealed class JsonXNode(
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : XNode(name, attributes, children, location)

/**
 * Node for JSON object
 */
class JsonObjectXNode(
    name: String,
    attributes: List<XAttribute>,
    children: List<XTree>,
    location: Localizable
) : JsonXNode(name, attributes, children, location) {

    constructor(name: String, attributes: List<XAttribute>, children: List<XTree>) : this(
        name,
        attributes,
        children,
        Location.NO_LOCATION
    )
}

/**
 * Node for JSON array
 */
class JsonArrayXNode(
    name: String,
    children: List<XTree>,
    location: Localizable
) : JsonXNode(name, emptyList(), children, location) {

    constructor(name: String, children: List<XTree>) : this(name, children, Location.NO_LOCATION)
}

/**
 * Leaf for JSON array value
 */
class JsonXLeaf(
    name: String,
    value: JsonXValue<*>?,
    location: Localizable
) : XLeaf(name, value, location) {

    constructor(name: String, value: JsonXValue<*>?) : this(name, value, Location.NO_LOCATION)
}
