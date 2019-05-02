package cz.syntea.xdef.runtime

import cz.syntea.xdef.core.definition.XDefinition
import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.core.document.stream.XReader
import cz.syntea.xdef.core.document.stream.XWriter
import cz.syntea.xdef.script.XScriptFactory
import cz.syntea.xdef.script.XScriptRuntime

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class DummyRuntime(private val scriptFactory: XScriptFactory) : Runtime {

    @Suppress("UNUSED_VARIABLE")
    override fun parse(definition: XDefinition, input: XTree, output: XWriter?, reporter: Appendable): XTree {
        val script: XScriptRuntime = scriptFactory.createXScriptRuntime()
        TODO("not implemented")
    }

    override fun parse(definition: XDefinition, input: XReader, output: XWriter?, reporter: Appendable): XTree {
        TODO("not implemented")
    }

    override fun create(definition: XDefinition, input: XTree?, output: XWriter?, reporter: Appendable): XTree {
        TODO("not implemented")
    }

    override fun create(definition: XDefinition, input: XReader?, output: XWriter?, reporter: Appendable): XTree {
        TODO("not implemented")
    }
}
