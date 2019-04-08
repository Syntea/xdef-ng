package cz.syntea.xdef.model

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class DefaultDocument(
    override val type: Document.Type,
    override val root: DataNode
) : Document {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Document) return false

        if (type != other.type) return false
        if (root != other.root) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + root.hashCode()
        return result
    }
}
