package org.xdef.compiler

import org.xdef.core.document.definition.XDefDocument
import java.io.InputStream
import java.io.OutputStream

/**
 * Interface of Compiler component
 * It declares method used for compiling X-definitions
 * and methods for de/serialization set of compiled X-definitions [XDPool]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Compiler {

    /**
     * @param definitions Collection of raw X-definition
     * @param reporter Use for reporting data by processing
     *
     * @return Set of compiled X-definitions
     */
    fun compile(definitions: List<XDefDocument>, reporter: Appendable): XDPool

    /**
     * Serialize X-definition
     *
     * @param pool Set of compiled X-definitions
     * @param poolStream Output
     */
    fun serializeXDPool(pool: XDPool, poolStream: OutputStream)

    /**
     * Deserialize X-definition
     *
     * @param poolStream Input
     *
     * @return Set of compiled X-definitions
     */
    fun deserializeXDPool(poolStream: InputStream): XDPool
}
