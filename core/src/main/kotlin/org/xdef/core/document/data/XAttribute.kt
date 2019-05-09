package org.xdef.core.document.data

import org.xdef.core.Attribute
import org.xdef.core.Localizable

/**
 * Class represents attribute of document's node
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class XAttribute(
    override val name: String,
    /**
     * Value of attribute is mutable
     */
    override var value: XValue?,
    override val lineNumber: Int,
    override val columnNumber: Int
) : Attribute<XValue>, Localizable
