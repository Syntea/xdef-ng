package org.xdef.translator.document

import org.xdef.core.document.data.XDocument
import org.xdef.core.document.data.XTree

/**
 * Interface contains methods for translation
 * from document object model to inner form of document and back
 *
 * @param T Type of object model representation
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DocumentTranslator<T> : DocumentTranslatorIO {

    /**
     * Translation from document object model to inner form of document
     *
     * @param dom Document object model
     *
     * @return Inner form of document
     */
    fun translate(dom: T): XDocument

    /**
     * Translation from inner form of document to document object model
     *
     * @param document Inner form of document
     *
     * @return Document object model
     */
    fun translate(document: XDocument): T

    /**
     * Fragment translation from inner form of document to document object model
     *
     * @param documentTree Fragment of inner form of document
     *
     * @return Fragment of document object model
     */
    fun translate(documentTree: XTree): T
}
