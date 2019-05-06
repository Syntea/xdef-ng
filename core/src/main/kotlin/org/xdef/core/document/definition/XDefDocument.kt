package org.xdef.core.document.definition

import org.xdef.core.document.DocumentType

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDefDocument {

    /**
     *
     */
    val root: XDefTree

    /**
     *
     */
    val type: DocumentType
}
