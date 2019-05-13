package org.xdef.translator.definition

import org.xdef.core.document.definition.XDefDocument

/**
 * Interface contains methods for translation
 * from document object model to inner form of X-definition document and back
 *
 * @param T Type of object model representation
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DefinitionTranslator<T> {

    /**
     * Translation from document object model to inner form of X-definition document
     *
     * @param dom Document object model
     *
     * @return Inner form of X-definition document
     */
    fun translate(dom: T): XDefDocument

    /**
     * Translation from inner form of X-definition document to document object model
     *
     * @param definition Inner form of X-definition document
     *
     * @return Document object model
     */
    fun translate(definition: XDefDocument): T
}
