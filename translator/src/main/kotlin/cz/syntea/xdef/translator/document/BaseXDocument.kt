package cz.syntea.xdef.translator.document

import cz.syntea.xdef.core.document.DocumentType
import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.translator.SupportedDataType

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
abstract class BaseXDocument(
    dataType: SupportedDataType,
    override val type: DocumentType = DocumentType(dataType.name)
) : XDocument
