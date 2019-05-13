package org.xdef.translator.document.json.model

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.LocalizedXValue

/**
 * Parent class defines restrict JSON primitives values hierarchy
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
sealed class JsonXValue<out T : Any>(
    /**
     * Typed value
     * It holds real type of the value
     */
    val typedValue: T,
    location: Localizable
) : LocalizedXValue(typedValue.toString(), location)

/**
 * JSON boolean value
 */
class JsonXBoolean(value: Boolean, location: Localizable) : JsonXValue<Boolean>(value, location) {

    constructor(value: Boolean) : this(value, Location.NO_LOCATION)
}

/**
 * JSON number value
 */
class JsonXNumber(value: Number, location: Localizable) : JsonXValue<Number>(value, location) {

    constructor(value: Number) : this(value, Location.NO_LOCATION)
}

/**
 * JSON string value
 */
class JsonXString(value: String, location: Localizable) : JsonXValue<String>(value, location) {

    constructor(value: String) : this(value, Location.NO_LOCATION)
}
