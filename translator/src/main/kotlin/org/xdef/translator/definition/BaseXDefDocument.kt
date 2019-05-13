package org.xdef.translator.definition

import org.xdef.core.document.DocumentType
import org.xdef.core.document.definition.XDefDocument
import org.xdef.translator.SupportedDataType

/**
 * Base implementation of [XDefDocument]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
abstract class BaseXDefDocument(
    dataType: SupportedDataType,
    override val type: DocumentType = DocumentType(dataType.name)
) : XDefDocument
