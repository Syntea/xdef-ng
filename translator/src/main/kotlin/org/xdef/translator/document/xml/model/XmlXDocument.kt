package org.xdef.translator.document.xml.model

import org.jdom2.Document
import org.xdef.core.document.data.XTree
import org.xdef.translator.SupportedDataType
import org.xdef.translator.document.BaseXDocument

/**
 * Implementation for XML document
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XmlXDocument constructor(
    /**
     * Reference to XML document object model
     * Using for preserve not processing information
     */
    internal val document: Document,
    override val root: XTree
) : BaseXDocument(SupportedDataType.XML)
