package org.xdef.translator.document.xml.model

import org.xdef.core.document.data.LocalizedXAttribute

/**
 * XML attribute
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXAttribute(
    name: String,
    value: XmlTextXValue // XML not support nullable attribute
) : LocalizedXAttribute(name, value/*NO LOCATION*/)
