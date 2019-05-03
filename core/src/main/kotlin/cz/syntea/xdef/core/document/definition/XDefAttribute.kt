package cz.syntea.xdef.core.document.definition

import cz.syntea.xdef.core.Attribute
import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.XValue
import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XDefAttribute(
    override val name: String,
    override val value: XValue?,
    override val allowedOccurrences: List<Occurrence>,
    override val allowedEvents: List<Event>,
    location: Localizable
) : XDefinitionSpecifier, Scriptable, Attribute<XValue>, Localizable {

    constructor(
        name: String,
        value: XValue?,
        allowedOccurrence: List<Occurrence>,
        allowedEvent: List<Event>
    ) : this(name, value, allowedOccurrence, allowedEvent, Location.NO_LOCATION)

    override val lineNumber = location.lineNumber
    override val columnNumber = location.columnNumber

    override val script get() = value?.toString()
}
