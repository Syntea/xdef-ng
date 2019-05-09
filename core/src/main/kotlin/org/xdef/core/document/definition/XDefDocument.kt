package org.xdef.core.document.definition

import org.xdef.core.document.DocumentType

/**
 * Class represents document with X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDefDocument {

    /**
     * Root node of tree structure which represents document with X-definition
     */
    val root: XDefTree

    /**
     * Type of document
     */
    val type: DocumentType
}
