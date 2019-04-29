package cz.syntea.xdef.translator.document.xml.model

import cz.syntea.xdef.core.document.data.LocalizedXAttribute

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXAttribute(
    name: String,
    value: XmlTextXValue // XML not support nullable attribute
) : LocalizedXAttribute(name, value/*NO LOCATION*/)
