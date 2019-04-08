package cz.syntea.xdef.translator.data.json.model

import cz.syntea.xdef.model.DataNode
import cz.syntea.xdef.model.Document
import cz.syntea.xdef.translator.SupportedDataType
import cz.syntea.xdef.translator.data.AbstractDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class JsonDocument(
    override val root: DataNode
) : AbstractDocument(SupportedDataType.JSON) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Document) return false
        if (!super.equals(other)) return false

        if (root != other.root) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + root.hashCode()
        return result
    }
}
