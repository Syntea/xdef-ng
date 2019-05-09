package org.xdef.core.document.data

import org.xdef.core.Localizable
import org.xdef.core.Location

/**
 * Reimplementation class [XAttribute]
 * In constructor accepting type [Localizable] for comfortable creating instances
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class LocalizedXAttribute(
    name: String,
    value: XValue?,
    location: Localizable
) : XAttribute(name, value, location.lineNumber, location.columnNumber) {

    constructor(name: String, value: XValue?) : this(name, value, Location.NO_LOCATION)
}
