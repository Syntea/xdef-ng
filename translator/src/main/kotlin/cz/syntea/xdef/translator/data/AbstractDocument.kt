package cz.syntea.xdef.translator.data

import cz.syntea.xdef.model.Document
import cz.syntea.xdef.translator.SupportedDataType

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
abstract class AbstractDocument(
    dataType: SupportedDataType,
    override val type: Document.Type = Document.Type(dataType.name)
) : Document {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Document) return false

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}
