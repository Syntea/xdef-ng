package cz.syntea.xdef.translator.definition.json.model

import cz.syntea.xdef.core.document.definition.XDefTree
import cz.syntea.xdef.translator.SupportedDataType
import cz.syntea.xdef.translator.definition.BaseXDefDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class JsonXDefDocument(
    override val root: XDefTree
) : BaseXDefDocument(SupportedDataType.JSON)
