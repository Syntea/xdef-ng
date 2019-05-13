package org.xdef.translator

import org.jdom2.Content
import org.jdom2.located.Located
import org.xdef.core.Location

/**
 * File contains extension and util functions
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

val WS_REGEX = "\\s".toRegex()

fun String.normalizeName() = split(WS_REGEX)
    .joinToString(separator = "_")
    .ifEmpty { "__WS__" }

/**
 *
 */
fun Content.extractLocation() = if (this is Located) Location(line, column) else Location.NO_LOCATION

fun indexedName(name: String, index: Int) = "$name.$index"
