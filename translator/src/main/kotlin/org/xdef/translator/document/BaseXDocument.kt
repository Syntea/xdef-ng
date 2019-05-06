package org.xdef.translator.document

import org.xdef.core.document.DocumentType
import org.xdef.core.document.data.XDocument
import org.xdef.translator.SupportedDataType

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
abstract class BaseXDocument(
    dataType: SupportedDataType,
    override val type: DocumentType = DocumentType(dataType.name)
) : XDocument
