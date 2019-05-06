package org.xdef.compiler

import org.xdef.core.document.definition.XDefDocument
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Compiler {

    /**
     * @param definitions
     * @param reporter
     *
     * @return
     */
    fun compile(definitions: List<XDefDocument>, reporter: Appendable): XDPool

    /**
     * @param pool
     * @param poolStream
     */
    fun serializeXDPool(pool: XDPool, poolStream: OutputStream)

    /**
     * @param poolStream
     *
     * @return
     */
    fun deserializeXDPool(poolStream: InputStream): XDPool
}
