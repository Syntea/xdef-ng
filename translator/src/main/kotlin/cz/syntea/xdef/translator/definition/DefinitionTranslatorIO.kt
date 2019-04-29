package cz.syntea.xdef.translator.definition

import cz.syntea.xdef.core.document.definition.XDefDocument
import java.io.Reader
import java.io.Writer

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DefinitionTranslatorIO {

    /**
     * @param input
     *
     * @return
     */
    fun readDefinition(input: Reader): XDefDocument

    /**
     * @param definition
     * @param output
     */
    fun writeDefinition(definition: XDefDocument, output: Writer)
}
