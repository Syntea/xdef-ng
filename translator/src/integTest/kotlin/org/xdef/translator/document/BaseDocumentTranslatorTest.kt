package org.xdef.translator.document

import io.kotlintest.TestCase
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.xdef.translator.JsonTranslator
import org.xdef.translator.XmlTranslator
import java.io.StringWriter

/**
 * Base test class for document translation
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal open class BaseDocumentTranslatorTest : FreeSpec() {

    protected val jsonTranslator = JsonTranslator()
    protected val xmlTranslator = XmlTranslator()

    protected lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    protected fun String.resToString() =
        requireNotNull(IOUtils.toString(autoClose(BaseDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this").reader())))

    protected fun String.toDocumentReader(translator: DocumentTranslator<*>) =
        autoClose(translator.createTranslationReader(byteInputStream()))

    protected fun String.resToDocument(translator: DocumentTranslator<*>) =
        translator.readDocument(autoClose(BaseDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this")))

    protected fun String.resToReader(translator: DocumentTranslator<*>) =
        autoClose(translator.createTranslationReader(BaseDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this")))

    protected fun createWriter(translator: DocumentTranslator<*>) =
        autoClose(translator.createTranslationWriter(outputWriter))
}
