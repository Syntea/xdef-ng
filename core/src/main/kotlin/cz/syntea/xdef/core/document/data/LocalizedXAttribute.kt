package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location

/**
 * TODO CLASS_DESCRIPTION
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
