package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.Attribute
import cz.syntea.xdef.core.Localizable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XAttribute(
    override val name: String,
    override var value: XValue?,
    override val lineNumber: Int,
    override val columnNumber: Int
) : Attribute<XValue>, Localizable
