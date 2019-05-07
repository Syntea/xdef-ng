package org.xdef.translator

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

val WS_REGEX = "\\s".toRegex()

fun String.normalizeName() = split(WS_REGEX)
    .joinToString(separator = "_")
    .ifEmpty { "__WS__" }
