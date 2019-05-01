package cz.syntea.xdef.translator.document.json.std

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
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

    constructor(value: Boolean) : this(Location.NO_LOCATION.lineNumber, Location.NO_LOCATION.columnNumber)

    override fun getValueType() = JsonValue.ValueType.NULL

    override fun toString() = null.toString()
}
