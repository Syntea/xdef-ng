package cz.syntea.xdef.core.document.data

import cz.syntea.xdef.core.document.DocumentType

/**
 * Class represents document in internal structure
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDocument {

    /**
     * Type of document
     */
    val type: DocumentType

    /**
     * Root node of tree structure which represents document
     */
    val root: XTree
}
