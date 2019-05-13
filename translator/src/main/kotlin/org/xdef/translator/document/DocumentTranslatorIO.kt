package org.xdef.translator.document

import org.xdef.core.document.data.XDocument
import org.xdef.core.document.stream.XReader
import org.xdef.core.document.stream.XWriter
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer

/**
 * Interface contains methods for read/write and translation
 * from document object model to inner form of document and back
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DocumentTranslatorIO {

    /**
     * Read document and translation to inner form
     * It deduces encoding according to information in the source document
     *
     * @param input Stream of data
     *
     * @return Inner form of document
     */
    fun readDocument(input: InputStream): XDocument

    /**
     * Read document and translation to inner form
     * It decodes document according to encoding in the [Reader]
     * If encoding in the [Reader] not corresponds with encoding of the document, translation fails
     *
     * @param input Stream of data
     *
     * @return Inner form of document
     */
    fun readDocument(input: Reader): XDocument

    /**
     * Translate [document] and write to the output
     * Encoding of the output is defined the specific document format writer
     *
     * @param document Inner form of document
     * @param output Stream of data
     */
    fun writeDocument(document: XDocument, output: OutputStream)

    /**
     * Translate [document] and write to the output
     * Encoding of the output is defined according to encoding in the [Writer]
     * It may produce inconsistency between information about encoding and real document encoding
     *
     * @param document Inner form of document
     * @param output Stream of data
     */
    fun writeDocument(document: XDocument, output: Writer)

    /**
     * Create reader for reading and translation of stream of data
     * It deduces encoding according to information in the source document
     *
     * @param input Stream of data
     *
     * @return Translation reader
     *
     * @see [XReader]
     */
    fun createTranslationReader(input: InputStream): XReader

    /**
     * Create reader for reading and translation of stream of data
     * It decodes document according to encoding in the [Reader]
     * If encoding in the [Reader] not corresponds with encoding of the document, translation fails
     *
     * @param input Stream of data
     *
     * @return Translation reader
     *
     * @see [XReader]
     */
    fun createTranslationReader(input: Reader): XReader

    /**
     * Create writer for translation and writing of stream of data
     * Encoding of the output is defined the specific document format writer
     *
     * @param output Stream of data
     *
     * @see [XWriter]
     */
    fun createTranslationWriter(output: OutputStream): XWriter

    /**
     * Create writer for translation and writing of stream of data
     * Encoding of the output is defined according to encoding in the [Writer]
     * It may produce inconsistency between information about encoding and real document encoding
     *
     * @param output Stream of data
     *
     * @see [XWriter]
     */
    fun createTranslationWriter(output: Writer): XWriter
}
