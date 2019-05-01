package cz.syntea.xdef.script

import cz.syntea.xdef.core.XScriptInfo
import cz.syntea.xdef.core.definition.CompiledScript
import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.core.document.data.XValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XScriptRuntime : XScriptEnvironment {

    /**
     * @param info
     */
    fun checkInfo(info: XScriptInfo): Boolean

    /**
     * @param script
     * @param value
     * @param collector
     *
     * @return
     */
    fun validate(script: CompiledScript, value: XValue?, collector: InfoCollector): Boolean

    /**
     * @param script
     * @param context
     * @param collector
     *
     * @return
     */
    fun execute(script: CompiledScript, context: XTree?, collector: InfoCollector): List<XTree>
}
