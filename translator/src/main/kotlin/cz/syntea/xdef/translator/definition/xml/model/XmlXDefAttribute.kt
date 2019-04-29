package cz.syntea.xdef.translator.definition.xml.model

import cz.syntea.xdef.core.document.data.XValue
import cz.syntea.xdef.core.document.definition.XDefAttribute

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXDefAttribute(
    name: String,
    value: XValue?
) : XDefAttribute(
    name,
    value,
    listOf(), // TODO Determine it
    listOf() // TODO Determine it
    /*NO LOCATION*/
)
