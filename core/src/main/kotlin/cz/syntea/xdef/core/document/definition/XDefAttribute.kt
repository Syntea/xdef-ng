package cz.syntea.xdef.core.document.definition

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.LocalizedXAttribute
import cz.syntea.xdef.core.document.data.XValue
import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XDefAttribute(
    name: String,
    value: XValue?,
    override val allowedOccurrences: List<Occurrence>,
    override val allowedEvents: List<Event>,
    location: Localizable
) : LocalizedXAttribute(name, value, location), XDefinitionSpecifier, Scriptable {

    constructor(
        name: String,
        value: XValue?,
        allowedOccurrence: List<Occurrence>,
        allowedEvent: List<Event>
    ) : this(name, value, allowedOccurrence, allowedEvent, Location.NO_LOCATION)

    override val script = value?.toString()
}
