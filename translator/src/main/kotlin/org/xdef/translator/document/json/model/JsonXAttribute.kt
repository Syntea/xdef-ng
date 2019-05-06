package org.xdef.translator.document.json.model

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.LocalizedXAttribute

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
