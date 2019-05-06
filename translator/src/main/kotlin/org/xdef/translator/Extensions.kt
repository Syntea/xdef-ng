package org.xdef.translator

import java.util.*

/**
 * File contains extension functions
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

fun <T> Stack<T>.peekOrNull(): T? {
    return if (isEmpty()) null else peek()
}
