package org.xdef.compiler

import org.xdef.core.XScriptInfo
import org.xdef.core.definition.XDefinition
import java.io.Serializable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author <a href="mailto:smidfil3@fit.cvut.cz">Filip Šmíd</a>
 */
data class XDPool(
    private val scriptInfo: XScriptInfo,
    private val pool: Map<String, XDefinition>
) : Serializable {

    fun createXDefinition(name: String) = pool[name]
}
