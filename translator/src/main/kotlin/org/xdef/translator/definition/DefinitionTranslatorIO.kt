package org.xdef.translator.definition

import org.xdef.core.document.definition.XDefDocument
import java.io.InputStream
import java.io.OutputStream
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
    fun readDefinition(input: InputStream): XDefDocument

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
    fun writeDefinition(definition: XDefDocument, output: OutputStream)

    /**
     * @param definition
     * @param output
     */
    fun writeDefinition(definition: XDefDocument, output: Writer)
}
