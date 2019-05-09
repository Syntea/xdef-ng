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
 * File contains classes for defined tree structure of X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Basic class for defined X-definition tree
 */
sealed class XDefinitionTree : Tree, Scripted, Localizable, Serializable

/**
 * X-definition node
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
 * X-definition leaf
 * It contains script for validation of type of value
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
    /**
     * Part of X-definition script, which used for validation of type of value
     */
    override val value: CompiledScript?
) : XDefinitionTree(), Leaf<CompiledScript>
