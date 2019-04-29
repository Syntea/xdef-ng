package cz.syntea.xdef.translator.document.xml.model

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.data.LocalizedXValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlTextXValue(
    value: String,
    location: Localizable = Location.NO_LOCATION
) : LocalizedXValue(value, location)
