package cz.syntea.xdef.translator.data.xml.model

import cz.syntea.xdef.translator.SupportedDataType
import cz.syntea.xdef.translator.data.AbstractDocument
import org.jdom2.Document

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XmlDocument constructor(
    internal val document: Document
) : AbstractDocument(SupportedDataType.XML) {

    override val root = XmlDataNode(element = document.rootElement)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is cz.syntea.xdef.model.Document) return false

        if (type != other.type) return false
        if (root != other.root) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + root.hashCode()
        return result
    }

    override fun toString(): String {
        return "XmlDocument(root=$root)"
    }
}
