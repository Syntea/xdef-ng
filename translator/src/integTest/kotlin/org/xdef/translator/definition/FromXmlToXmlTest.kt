package org.xdef.translator.definition

import io.kotlintest.shouldNotThrow
import org.xdef.translator.dataset.xml.*
import org.xmlunit.assertj.XmlAssert

/**
 * Integrations test for translation X-definition document from XML to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToXmlTest : BaseDefinitionTranslatorTest() {

    init {
        "definition" - {
            "minimal" - {
                "empty element" { xmlAssertEquals(minimalElement) }
                "text" { xmlAssertEquals(minimalText) }
                "with comment" { xmlAssertEquals(minimalEmptyAndComment) }
                "empty with comment" { xmlAssertEquals(minimalEmptyWithComment) }
            }
            "simple" {
                xmlAssertEquals(simpleDefElement)
            }
            "complex" {
                xmlResAssertEquals("complexDefinition")
            }
        }
    }

    private fun xmlAssertEquals(xml: String) {
        val definition = xmlTranslator.readDefinition(autoClose(xml.reader()))
        xmlTranslator.writeDefinition(definition, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(outputWriter.toString())
                .and(xml)
                .ignoreElementContentWhitespace()
                .areSimilar()
        }
    }

    private fun xmlResAssertEquals(xmlRes: String) {
        val definition = xmlRes.resToDefinition()
        xmlTranslator.writeDefinition(definition, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(outputWriter.toString())
                .and(xmlRes.xmlResToString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun String.xmlResToString() = "$this.xdef".resToString()

    private fun String.resToDefinition() = "$this.xdef".resToDefinition(xmlTranslator)
}
