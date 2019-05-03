package cz.syntea.xdef.core.definition

import cz.syntea.xdef.core.Leaf
import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Node
import cz.syntea.xdef.core.Tree
import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence
import cz.syntea.xdef.core.lang.Option
import java.io.Serializable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 *
 */
sealed class XDefinitionTree : Tree, Scripted, Localizable, Serializable

/**
 *
 */
data class XDefinitionNode(
    override val name: String,
    override val occurrence: Occurrence,
    override val minBound: Long,
    override val maxBound: Long,
    override val actions: Map<Event, CompiledScript>,
    override val options: Set<Option>,
    override val setupScopeCommand: CompiledScript?,
    override val lineNumber: Int,
    override val columnNumber: Int,
    override val attributes: List<XDefinitionAttribute>,
    override val children: List<XDefinitionTree>
) : XDefinitionTree(), Node<XDefinitionAttribute, XDefinitionTree>

/**
 *
 */
data class XDefinitionLeaf(
    override val name: String,
    override val occurrence: Occurrence,
    override val minBound: Long,
    override val maxBound: Long,
    override val actions: Map<Event, CompiledScript>,
    override val options: Set<Option>,
    override val setupScopeCommand: CompiledScript?,
    override val lineNumber: Int,
    override val columnNumber: Int,
    override val value: CompiledScript?
) : XDefinitionTree(), Leaf<CompiledScript>
