package org.xdef.script

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptFactory {
    fun createXScript(): XScript

    fun createXScriptCompiler(): XScriptCompiler

    fun createXScriptRuntime(): XScriptRuntime
}
