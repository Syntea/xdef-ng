package org.xdef.translator.definition

import io.kotlintest.TestCase
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.xdef.translator.JsonTranslator
import org.xdef.translator.XmlTranslator
import org.xdef.translator.document.BaseDocumentTranslatorTest
import java.io.StringWriter

/**
 * Base test class for X-definition document translation
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal open class BaseDefinitionTranslatorTest : FreeSpec() {

    protected val jsonTranslator = JsonTranslator()
    protected val xmlTranslator = XmlTranslator()

    protected lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    protected fun String.resToString() =
        requireNotNull(IOUtils.toString(autoClose(BaseDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this").reader())))

    protected fun String.resToDefinition(translator: DefinitionTranslatorIO) =
        translator.readDefinition(autoClose(BaseDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this")))
}
