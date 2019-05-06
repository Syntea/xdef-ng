package org.xdef.core

/**
 * Basic implementation of [Localizable] interface
 * For not specified location sets values to -1
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class Location(
    override val lineNumber: Int,
    override val columnNumber: Int
) : Localizable {

    companion object {
        val NO_LOCATION = Location(-1, -1)
    }
}

fun Localizable.toLocation() = Location(lineNumber, columnNumber)
