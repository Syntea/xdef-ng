package cz.syntea.xdef.core.definition

import java.io.Serializable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XDefinition(
//    val scriptInfo: XScriptInfo,
//    val factory: XTreeFactory,
    val root: XDefinitionTree
) : Serializable
