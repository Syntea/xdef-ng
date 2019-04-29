package cz.syntea.xdef.translator.definition.json.model

import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.definition.XDefAttribute
import cz.syntea.xdef.core.document.definition.XDefLeaf
import cz.syntea.xdef.core.document.definition.XDefNode
import cz.syntea.xdef.core.document.definition.XDefTree
import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence
import cz.syntea.xdef.translator.document.json.model.JsonXValue

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
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : XDefNode(name, script, attributes, children, allowedOccurrence, allowedEvent, location)

/**
 *
 */
class JsonObjectXDefNode(
    name: String,
    script: String?,
    attributes: List<XDefAttribute>,
    children: List<XDefTree>,
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : JsonXDefNode(name, script, attributes, children, allowedOccurrence, allowedEvent, location)

/**
 *
 */
class JsonArrayXDefNode(
    name: String,
    script: String?,
    children: List<XDefTree>,
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : JsonXDefNode(name, script, emptyList(), children, allowedOccurrence, allowedEvent, location)

/**
 *
 */
class JsonXDefLeaf(
    name: String,
    value: JsonXValue<*>?,
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : XDefLeaf(name, value, allowedOccurrence, allowedEvent, location)
