package cz.syntea.xdef.translator.document.json.std

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import javax.json.JsonString
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedJsonString(
    private val value: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : JsonString, Localizable {

    constructor(value: String) : this(value, Location.NO_LOCATION.lineNumber, Location.NO_LOCATION.columnNumber)

    override fun getString(): String {
        return value
    }

    override fun getChars(): CharSequence {
        return value
    }

    override fun getValueType(): JsonValue.ValueType {
        return JsonValue.ValueType.STRING
    }
}
