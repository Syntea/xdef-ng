package org.xdef.translator.definition.json.model

import org.xdef.core.Location
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.core.document.definition.XDefLeaf
import org.xdef.core.document.definition.XDefNode
import org.xdef.core.document.definition.XDefTree
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence
import org.xdef.translator.document.json.model.JsonXValue

/**
 * File contains special JSON implementation for tree of X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Parent class defines restrict JSON structures hierarchy
 */
sealed class JsonXDefNode(
    name: String,
    script: String?,
    attributes: List<XDefAttribute>,
    children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefNode(name, script, attributes, children, allowedOccurrences, allowedEvents, location)

/**
 * Node for JSON object
 */
class JsonObjectXDefNode(
    name: String,
    script: String?,
    attributes: List<XDefAttribute>,
    children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : JsonXDefNode(name, script, attributes, children, allowedOccurrences, allowedEvents, location)

/**
 * Node for JSON array
 */
class JsonArrayXDefNode(
    name: String,
    script: String?,
    children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : JsonXDefNode(name, script, emptyList(), children, allowedOccurrences, allowedEvents, location)

/**
 * Leaf for JSON array value
 */
class JsonXDefLeaf(
    name: String,
    value: JsonXValue<*>?,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefLeaf(name, value, allowedOccurrences, allowedEvents, location)
