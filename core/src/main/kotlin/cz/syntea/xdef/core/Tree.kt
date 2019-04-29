package cz.syntea.xdef.core

/**
 * File contains basic interfaces which define tree structure
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Basic interface for tree structures
 */
interface Tree {
    /**
     * Name of node in tree
     */
    val name: String
}

/**
 * Basic interface for node in tree
 *
 * @param A Type of attribute
 * @param T Type of common type of nodes
 */
interface Node<out A : Attribute<*>, out T : Tree> : Tree {
    val attributes: List<A>
    val children: List<T>
}

/**
 * Basic interface for node with value
 * Values can be in leaf
 *
 * @param V Type of value
 */
interface Leaf<V> : Tree {
    val value: V?
}
