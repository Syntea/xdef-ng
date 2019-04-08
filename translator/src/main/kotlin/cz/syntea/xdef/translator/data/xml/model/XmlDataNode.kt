package cz.syntea.xdef.translator.data.xml.model

import cz.syntea.xdef.model.DataNode
import org.jdom2.Element

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XmlDataNode(
    override val parent: DataNode? = null,
    internal val element: Element
) : DataNode {

    override val name = element.qualifiedName!!

    override var value: String?
        get() = element.textNormalize
        set(value) {
            element.text = value
        }

    override val attributes = element.attributes.map { it.qualifiedName to it.value }.toMap()

    override val children = element.children.map { XmlDataNode(parent = this, element = it) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "XmlDataNode(name='$name', value=$value, attributes=$attributes, parent=${parent?.name}, children=$children)"
    }
}
