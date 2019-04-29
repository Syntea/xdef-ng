package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.Leaf
import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Node
import cz.syntea.xdef.core.Tree

/**
 * TODO CLASS_DESCRIPTION
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
 *
 */
open class XNode(
    name: String,
    override val attributes: List<XAttribute>,
    override val children: List<XTree>,
    location: Localizable
) : XTree(name, location), Node<XAttribute, XTree>

/**
 *
 */
open class XLeaf(
    name: String,
    override val value: XValue?,
    location: Localizable
) : XTree(name, location), Leaf<XValue>
