package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class LocalizedXValue(
    value: String,
    location: Localizable
) : XValue(value, location.lineNumber, location.columnNumber) {

    constructor(value: String) : this(value, Location.NO_LOCATION)
}
