package cz.syntea.xdef.translator.definition

import cz.syntea.xdef.core.document.DocumentType
import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.translator.SupportedDataType

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
abstract class BaseXDefDocument(
    dataType: SupportedDataType,
    override val type: DocumentType = DocumentType(dataType.name)
) : XDefDocument
