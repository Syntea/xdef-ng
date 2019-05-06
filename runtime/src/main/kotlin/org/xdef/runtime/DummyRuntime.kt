package org.xdef.runtime

import org.xdef.core.definition.XDefinition
import org.xdef.core.document.data.XTree
import org.xdef.core.document.stream.XReader
import org.xdef.core.document.stream.XWriter
import org.xdef.script.XScriptFactory
import org.xdef.script.XScriptRuntime

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

    override fun create(
        definition: XDefinition,
        modelName: String,
        input: XTree?,
        output: XWriter?,
        reporter: Appendable
    ): XTree {
        TODO("not implemented")
    }

    override fun create(
        definition: XDefinition,
        modelName: String,
        input: XReader?,
        output: XWriter?,
        reporter: Appendable
    ): XTree {
        TODO("not implemented")
    }
}
