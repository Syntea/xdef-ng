package cz.syntea.xdef.translator.document.json.model

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.LocalizedXAttribute

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonXAttribute(
    name: String,
    value: JsonXValue<*>?,
    location: Localizable
) : LocalizedXAttribute(name, value, location) {

    constructor(name: String, value: JsonXValue<*>?) : this(name, value, Location.NO_LOCATION)
}
