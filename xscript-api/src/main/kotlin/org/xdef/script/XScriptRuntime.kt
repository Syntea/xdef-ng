package org.xdef.script

import org.xdef.core.XScriptInfo
import org.xdef.core.definition.CompiledScript
import org.xdef.core.document.data.XTree
import org.xdef.core.document.data.XValue

/**
 * Interface contains methods for runtime execution
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 *
 * @see [XScriptEnvironment]
 */
interface XScriptRuntime : XScriptEnvironment {

    /**
     * It checks compatibility this XScript instance version with version defined in [info]
     *
     * @param info Information about XScript implementation
     */
    fun checkInfo(info: XScriptInfo): Boolean

    /**
     * Run validation of type of value
     *
     * @param script Compiled script for check type of [value] e.g. string(2, 10)
     * @param value Checked value
     * @param collector Special collection for collecting supplemental data
     *
     * @return `true` if [value] corresponds rule in [script] otherwise `false`
     */
    fun validate(script: CompiledScript, value: XValue?, collector: InfoCollector): Boolean

    /**
     * Run action for concrete event
     *
     * @param script Compiled script
     * @param context Part of actual processing data
     * @param collector Special collection for collecting supplemental data
     *
     * @return Collection of new/changed data
     */
    fun execute(script: CompiledScript, context: XTree?, collector: InfoCollector): List<XTree>
}
