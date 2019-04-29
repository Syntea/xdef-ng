package cz.syntea.xdef.translator.document.json.model

import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.translator.SupportedDataType
import cz.syntea.xdef.translator.document.BaseXDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class JsonXDocument(
    override val root: XTree
) : BaseXDocument(SupportedDataType.JSON)
