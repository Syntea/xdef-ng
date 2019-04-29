package cz.syntea.xdef.translator.document.xml.model

import cz.syntea.xdef.translator.SupportedDataType
import cz.syntea.xdef.translator.document.BaseXDocument
import org.jdom2.Document
import org.jdom2.located.LocatedElement

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XmlXDocument constructor(
    internal val document: Document
) : BaseXDocument(SupportedDataType.XML) {

    override val root = XmlXNode(element = document.rootElement as LocatedElement)
}
