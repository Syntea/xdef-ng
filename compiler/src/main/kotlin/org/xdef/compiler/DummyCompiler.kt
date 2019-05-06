package org.xdef.compiler

import org.xdef.core.document.definition.XDefDocument
import org.xdef.script.XScriptFactory
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class DummyCompiler(private val scriptFactory: XScriptFactory) : Compiler {

    @Suppress("UNUSED_VARIABLE")
    override fun compile(definitions: List<XDefDocument>, reporter: Appendable): XDPool {
        val script = scriptFactory.createXScriptCompiler()
        TODO("not implemented")
    }

    override fun serializeXDPool(pool: XDPool, poolStream: OutputStream) {
        TODO("not implemented")
    }

    override fun deserializeXDPool(poolStream: InputStream): XDPool {
        TODO("not implemented")
    }
}
