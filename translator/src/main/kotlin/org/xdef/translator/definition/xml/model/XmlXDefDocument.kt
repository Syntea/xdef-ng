package org.xdef.translator.definition.xml.model

import org.jdom2.Document
import org.xdef.core.document.definition.XDefTree
import org.xdef.translator.SupportedDataType
import org.xdef.translator.definition.BaseXDefDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XmlXDefDocument(
    internal val document: Document,
    override val root: XDefTree
) : BaseXDefDocument(SupportedDataType.XML)
