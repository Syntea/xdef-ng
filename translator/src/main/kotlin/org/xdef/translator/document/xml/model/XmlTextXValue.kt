package org.xdef.translator.document.xml.model

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.LocalizedXValue

/**
 * XML text value
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlTextXValue(
    value: String,
    location: Localizable = Location.NO_LOCATION
) : LocalizedXValue(value, location)
