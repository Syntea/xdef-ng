package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.Localizable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XValue(
    open val value: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : Localizable
