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
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 *
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
 *
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
 *
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
 *
 */
class JsonXDefLeaf(
    name: String,
    value: JsonXValue<*>?,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefLeaf(name, value, allowedOccurrences, allowedEvents, location)
