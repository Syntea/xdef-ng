package org.xdef.translator.document.json.std

import org.xdef.core.Localizable
import org.xdef.core.Location

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedName(
    val name: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : CharSequence, Localizable {

    constructor(name: String) : this(name, Location.NO_LOCATION.lineNumber, Location.NO_LOCATION.columnNumber)

    override val length get() = name.length

    override fun get(index: Int) = name[index]

    override fun subSequence(startIndex: Int, endIndex: Int) = name.subSequence(startIndex, endIndex)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocalizedName) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = name.hashCode()

    override fun toString() = name
}
