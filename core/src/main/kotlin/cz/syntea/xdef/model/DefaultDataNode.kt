package cz.syntea.xdef.model

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
open class DefaultDataNode(
    override val name: String = "",
    override var value: String? = null,
    override val attributes: Map<String, String> = emptyMap(),
    override val parent: DataNode? = null,
    override val children: List<DataNode> = emptyList()
) : DataNode {

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
        return "${this.javaClass.simpleName}(name=$name, value='$value', attributes=$attributes, parent=${parent?.name}, children=$children)"
    }
}
