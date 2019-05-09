package org.xdef.core.document.stream

import java.io.Closeable
import java.io.Flushable

/**
 * Writer translate data from internal form to data format and write it to output
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XWriter : AutoCloseable, Closeable, Flushable {

    /**
     * @param event Signaling specify part of internal model, which should be translate and written
     */
    fun writeEvent(event: XEvent)
}
