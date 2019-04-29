package cz.syntea.xdef.translator.definition

import cz.syntea.xdef.core.document.definition.XDefDocument

/**
 * TODO CLASS_DESCRIPTION
 *
 * @param T
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DefinitionTranslator<T> {

    /**
     * @param dom
     *
     * @return
     */
    fun translate(dom: T): XDefDocument

    /**
     * @param definition
     *
     * @return
     */
    fun translate(definition: XDefDocument): T
}
