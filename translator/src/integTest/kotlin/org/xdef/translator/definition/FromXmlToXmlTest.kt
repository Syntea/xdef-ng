package org.xdef.translator.definition

import io.kotlintest.shouldNotThrow
import org.xdef.translator.dataset.xml.*
import org.xmlunit.assertj.XmlAssert

/**
 * Integration test for reading XML to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToXmlTest : BaseDefinitionTranslatorTest() {

    init {
        "minimal definition" - {
            "minimalElement" { xmlAssertEquals(minimalElement) }
            "only text value" { xmlAssertEquals(minimalText) }
            "with comment" { xmlAssertEquals(minimalWithComment) }
            "empty with comment" { xmlAssertEquals(minimalEmptyWithComment) }
            "with namespace" { xmlAssertEquals(simpleWithNamespace) }
        }
//        "simple document" - {
//            "object" { xmlAssertEquals(simpleObject) }
//            "number array" { xmlAssertEquals(simpleNumberArray) }
//            "string array" { xmlAssertEquals(simpleStringArray) }
//            "mixed array" { xmlAssertEquals(simpleMixedArray) }
//        }
    }

    private fun xmlAssertEquals(xml: String) {
        val document = xmlTranslator.readDefinition(autoClose(xml.reader()))
        xmlTranslator.writeDefinition(document, outputWriter)
        println(outputWriter.toString())
        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreElementContentWhitespace()
                .areSimilar()
        }
    }
}
