package org.xdef.translator.document.json.model

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.LocalizedXValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
sealed class JsonXValue<out T : Any>(
    val typedValue: T,
    location: Localizable
) : LocalizedXValue(typedValue.toString(), location)

class JsonXBoolean(value: Boolean, location: Localizable) : JsonXValue<Boolean>(value, location) {

    constructor(value: Boolean) : this(value, Location.NO_LOCATION)
}

class JsonXNumber(value: Number, location: Localizable) : JsonXValue<Number>(value, location) {

    constructor(value: Number) : this(value, Location.NO_LOCATION)
}

class JsonXString(value: String, location: Localizable) : JsonXValue<String>(value, location) {

    constructor(value: String) : this(value, Location.NO_LOCATION)
}
