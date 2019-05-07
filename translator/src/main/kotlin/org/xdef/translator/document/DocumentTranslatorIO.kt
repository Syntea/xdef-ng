package org.xdef.translator.document

import org.xdef.core.document.data.XDocument
import org.xdef.core.document.stream.XReader
import org.xdef.core.document.stream.XWriter
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DocumentTranslatorIO {

    /**
     * @param input
     * @return
     */
    fun readDocument(input: InputStream): XDocument

    /**
     * @param input
     * @return
     */
    fun readDocument(input: Reader): XDocument

    /**
     * @param document
     * @param output
     */
    fun writeDocument(document: XDocument, output: OutputStream)

    /**
     * @param document
     * @param output
     */
    fun writeDocument(document: XDocument, output: Writer)

    /**
     * @param input
     * @return
     */
    fun createTranslationReader(input: InputStream): XReader

    /**
     * @param input
     * @return
     */
    fun createTranslationReader(input: Reader): XReader

    /**
     * @param output
     * @return
     */
    fun createTranslationWriter(output: OutputStream): XWriter

    /**
     * @param output
     * @return
     */
    fun createTranslationWriter(output: Writer): XWriter
}