package org.xdef.script

import org.xdef.core.definition.CompiledScript

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptEnvironment {

    /**
     * @param script
     *
     * @return
     */
    fun setupScope(script: CompiledScript): Boolean

    /**
     * @return
     */
    fun cancelScope(): Boolean
}
