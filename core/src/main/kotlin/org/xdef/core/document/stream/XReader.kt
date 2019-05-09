package org.xdef.core.document.stream

import java.io.Closeable

/**
 * Interface of the event-based pull parser
 * Parser read data from input and translate it to internal form
 *
 * It's inherit [AutoCloseable] and [Closeable] for managing resources
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XReader : Iterator<XEvent>, AutoCloseable, Closeable {

    /**
     * @return If last peeked event exist return it else read next event and return it
     */
    fun peek(): XEvent
}
