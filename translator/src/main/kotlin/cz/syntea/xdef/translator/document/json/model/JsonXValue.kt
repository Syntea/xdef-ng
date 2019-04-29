package cz.syntea.xdef.translator.document.json.model

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.document.data.LocalizedXValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
sealed class JsonXValue<out T : Any>(
    val typedValue: T,
    location: Localizable
) : LocalizedXValue(typedValue.toString(), location)

class JsonXBoolean(value: Boolean, location: Localizable) : JsonXValue<Boolean>(value, location)
class JsonXNumber(value: Number, location: Localizable) : JsonXValue<Number>(value, location)
class JsonXString(value: String, location: Localizable) : JsonXValue<String>(value, location)
