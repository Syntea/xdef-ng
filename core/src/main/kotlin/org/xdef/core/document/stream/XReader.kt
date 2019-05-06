package org.xdef.core.document.stream

import java.io.Closeable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XReader : Iterator<XEvent>, AutoCloseable, Closeable {

    /**
     * @return Peeked event
     */
    fun peek(): XEvent
}
