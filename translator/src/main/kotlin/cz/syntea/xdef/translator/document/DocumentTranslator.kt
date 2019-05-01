package cz.syntea.xdef.translator.document

import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.core.document.data.XTree

/**
 * TODO CLASS_DESCRIPTION
 * @param T
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DocumentTranslator<T> : DocumentTranslatorIO {

    /**
     * @param dom
     * @return
     */
    fun translate(dom: T): XDocument

    /**
     * @param document
     * @return
     */
    fun translate(document: XDocument): T

    /**
     * @param documentTree
     * @return
     */
    fun translate(documentTree: XTree): T
}
