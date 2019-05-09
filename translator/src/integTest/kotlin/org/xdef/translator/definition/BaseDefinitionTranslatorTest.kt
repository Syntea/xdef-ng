package org.xdef.translator.definition

import io.kotlintest.TestCase
import io.kotlintest.specs.FreeSpec
import org.xdef.translator.JsonTranslator
import org.xdef.translator.XmlTranslator
import java.io.StringWriter

/**
 * TODO CLASS_DESCRIPTION
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

}
