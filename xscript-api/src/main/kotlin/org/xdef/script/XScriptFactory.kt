package org.xdef.script

/**
 * Factory for creating XScript instance
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptFactory {

    /**
     * @return New [XScript] implementation instance
     */
    fun createXScript(): XScript

    /**
     * @return New [XScriptCompiler] implementation instance
     */
    fun createXScriptCompiler(): XScriptCompiler

    /**
     * @return New [XScriptRuntime] implementation instance
     */
    fun createXScriptRuntime(): XScriptRuntime
}
