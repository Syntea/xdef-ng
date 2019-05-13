package org.xdef.translator.definition

import org.xdef.core.document.definition.XDefDocument
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer

/**
 * Interface contains methods for read/write and translation
 * from document object model to inner form of X-definition document and back
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DefinitionTranslatorIO {

    /**
     * Read X-definition document and translation to inner form
     * It deduces encoding according to information in the source document
     *
     * @param input Stream of data
     *
     * @return Inner form of X-definition document
     */
    fun readDefinition(input: InputStream): XDefDocument

    /**
     * Read X-definition document and translation to inner form
     * It decodes document according to encoding in the [Reader]
     * If encoding in the [Reader] not corresponds with encoding of the document, translation fails
     *
     * @param input Stream of data
     *
     * @return Inner form of X-definition document
     */
    fun readDefinition(input: Reader): XDefDocument

    /**
     * Translate [definition] and write to the output
     * Encoding of the output is defined the specific document format writer
     *
     * @param definition Inner form of X-definition document
     * @param output Stream of data
     */
    fun writeDefinition(definition: XDefDocument, output: OutputStream)

    /**
     * Translate [definition] and write to the output
     * Encoding of the output is defined according to encoding in the [Writer]
     * It may produce inconsistency between information about encoding and real document encoding
     *
     * @param definition Inner form of X-definition document
     * @param output Stream of data
     */
    fun writeDefinition(definition: XDefDocument, output: Writer)
}
