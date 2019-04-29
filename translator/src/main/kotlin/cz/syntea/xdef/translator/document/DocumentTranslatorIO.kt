package cz.syntea.xdef.translator.document

import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.core.document.stream.XReader
import cz.syntea.xdef.core.document.stream.XWriter
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
    fun readDocument(input: Reader): XDocument

    /**
     * @param document
     * @param output
     */
    fun writeDocument(document: XDocument, output: Writer)

    /**
     * @param input
     * @return
     */
    fun createTranslationReader(input: Reader): XReader

    /**
     * @param output
     * @return
     */
    fun createTranslationWriter(output: Writer): XWriter
}
