package org.xdef.script

import org.xdef.core.definition.CompiledScript

/**
 * Interface contains methods for manipulation with XScript runtime environment
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptEnvironment {

    /**
     * It creates new local scope and execute [script]
     *
     * @param script Initial command e.g. new variables, function etc.
     *
     * @return `true` if operation finished well otherwise `false`
     */
    fun setupScope(script: CompiledScript): Boolean

    /**
     * It notifies XScript that it should cancel last local scope
     *
     * @return `true` if operation finished well otherwise `false`
     */
    fun cancelScope(): Boolean
}
