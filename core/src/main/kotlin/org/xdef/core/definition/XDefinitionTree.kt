package org.xdef.core.definition

import org.xdef.core.Leaf
import org.xdef.core.Localizable
import org.xdef.core.Node
import org.xdef.core.Tree
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence
import org.xdef.core.lang.Option
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
