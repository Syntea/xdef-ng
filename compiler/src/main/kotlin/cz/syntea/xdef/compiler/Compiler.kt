package cz.syntea.xdef.compiler

import cz.syntea.xdef.core.document.definition.XDefDocument
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Compiler {

    /**
     * @param reporter
     * @param definitions
     *
     * @return
     */
    fun compile(reporter: Appendable, vararg definitions: XDefDocument): XDPool

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
