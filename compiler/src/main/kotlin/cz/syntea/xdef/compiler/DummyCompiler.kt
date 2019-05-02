package cz.syntea.xdef.compiler

import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.script.XScriptFactory
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class DummyCompiler(private val scriptFactory: XScriptFactory) : Compiler {

    @Suppress("UNUSED_VARIABLE")
    override fun compile(reporter: Appendable, definitions: List<XDefDocument>): XDPool {
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
