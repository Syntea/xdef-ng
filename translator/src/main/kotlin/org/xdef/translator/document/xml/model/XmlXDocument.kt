package org.xdef.translator.document.xml.model

import org.jdom2.Document
import org.jdom2.located.LocatedElement
import org.xdef.translator.SupportedDataType
import org.xdef.translator.document.BaseXDocument

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
