package cz.syntea.xdef.core.definition

import cz.syntea.xdef.core.XScriptInfo
import java.io.Serializable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XDefinition(

    /**
     * Information about XScript which was used by compilation
     */
    val scriptInfo: XScriptInfo,

    /**
     * Root of tree which represents compiled X-definition
     */
    val root: XDefinitionTree
) : Serializable
