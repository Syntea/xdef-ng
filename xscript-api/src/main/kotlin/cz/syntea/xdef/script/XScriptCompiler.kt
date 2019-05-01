package cz.syntea.xdef.script

import cz.syntea.xdef.core.XScriptInfo
import cz.syntea.xdef.core.definition.CompiledScript

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptCompiler : XScriptEnvironment {

    /**
     * Information about XScript implementation
     *
     * @see [XScriptInfo]
     */
    val scriptInfo: XScriptInfo

    /**
     * @param script
     *
     * @return
     */
    fun validateScript(script: String): Boolean

    /**
     * @param script
     *
     * @return
     */
    fun compileScript(script: String): CompiledScript
}
