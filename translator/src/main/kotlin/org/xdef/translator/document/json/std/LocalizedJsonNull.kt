package org.xdef.translator.document.json.std

import org.xdef.core.Localizable
import org.xdef.core.Location
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedJsonNull(
    override val lineNumber: Int,
    override val columnNumber: Int
) : JsonValue, Localizable {

    constructor() : this(Location.NO_LOCATION.lineNumber, Location.NO_LOCATION.columnNumber)

    override fun getValueType() = JsonValue.ValueType.NULL

    override fun toString() = null.toString()
}
