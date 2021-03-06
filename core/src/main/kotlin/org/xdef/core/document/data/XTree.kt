package org.xdef.core.document.data

import org.xdef.core.Leaf
import org.xdef.core.Localizable
import org.xdef.core.Node
import org.xdef.core.Tree

/**
 * Basic class for represents tree structure of document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
sealed class XTree(
    override val name: String,
    location: Localizable
) : Localizable, Tree {
    override val lineNumber = location.lineNumber
    override val columnNumber = location.columnNumber
}

/**
 * Document node
 */
open class XNode(
    name: String,
    override var attributes: List<XAttribute>,
    override var children: List<XTree>,
    location: Localizable
) : XTree(name, location), Node<XAttribute, XTree>

/**
 * Document leaf
 * It represents values in document
 */
open class XLeaf(
    name: String,
    /**
     * Value can be `null`
     */
    override var value: XValue?,
    location: Localizable
) : XTree(name, location), Leaf<XValue>
