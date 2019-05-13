package org.xdef.translator.document.json.model

import org.xdef.core.document.data.XTree
import org.xdef.translator.SupportedDataType
import org.xdef.translator.document.BaseXDocument

/**
 * Implementation for JSON document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class JsonXDocument(
    override val root: XTree
) : BaseXDocument(SupportedDataType.JSON)
