package org.xdef.core.document.data

import org.xdef.core.Localizable

/**
 * Class represents the value of tree of document
 * Wrapped value cannot be changed. For change new this class with correct value
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XValue(
    open val value: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : Localizable
