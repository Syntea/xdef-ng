package org.xdef.translator.definition.json.model

import org.xdef.core.document.definition.XDefTree
import org.xdef.translator.SupportedDataType
import org.xdef.translator.definition.BaseXDefDocument

/**
 * Implementation for JSON X-definition document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class JsonXDefDocument(
    override val root: XDefTree
) : BaseXDefDocument(SupportedDataType.JSON)
