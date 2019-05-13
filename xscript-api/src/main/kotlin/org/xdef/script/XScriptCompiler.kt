package org.xdef.script

import org.xdef.core.XScriptInfo
import org.xdef.core.definition.CompiledScript

/**
 * Interface contains methods for compilation phase X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 *
 * @see [XScriptEnvironment]
 */
interface XScriptCompiler : XScriptEnvironment {

    /**
     * Information about XScript implementation
     *
     * @see [XScriptInfo]
     */
    val scriptInfo: XScriptInfo

    /**
     * It checks syntax of [script]
     *
     * @param script Script string
     *
     * @return `true` if syntax of [script] is valid otherwise `false`
     */
    fun validateScript(script: String): Boolean

    /**
     * It compiles [script]. It is transformation from string to inner form
     *
     * @param script Script string
     *
     * @return Compiled script
     */
    fun compileScript(script: String): CompiledScript
}
