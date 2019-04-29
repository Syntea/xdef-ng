package cz.syntea.xdef.core

/**
 * Basic interface for node attribute
 * Attribute represents key-value structure
 *
 * @param V type of value
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Attribute<V> {
    /**
     * Attribute name
     */
    val name: String

    /**
     * Value of attribute
     */
    val value: V?
}
