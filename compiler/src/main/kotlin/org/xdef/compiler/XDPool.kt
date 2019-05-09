package org.xdef.compiler

import org.xdef.core.XScriptInfo
import org.xdef.core.definition.XDefinition
import java.io.Serializable

/**
 * Structure holds set of compiled X-definitions and information
 * about used XScript by compilation
 *
 * It supports de/serialization
 *
 * @author <a href="mailto:smidfil3@fit.cvut.cz">Filip Šmíd</a>
 */
data class XDPool(
    private val scriptInfo: XScriptInfo,
    private val pool: Map<String, XDefinition>
) : Serializable {

    /**
     * It creates new X-definition for using in processing
     *
     * @param name Name of X-definition
     */
    fun createXDefinition(name: String) = pool[name]
}
