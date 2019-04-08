package cz.syntea.xdef.translator.data

import cz.syntea.xdef.model.Document
import java.io.InputStream
import java.io.OutputStream

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DataTranslator {

    fun readAsDocument(input: InputStream): Document

    fun writeDocument(document: Document, output: OutputStream)
}
