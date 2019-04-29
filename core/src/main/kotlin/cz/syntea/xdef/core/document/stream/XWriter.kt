package cz.syntea.xdef.core.document.stream

import java.io.Closeable
import java.io.Flushable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XWriter : AutoCloseable, Closeable, Flushable {

    /**
     * @param event
     */
    fun writeEvent(event: XEvent)
}
