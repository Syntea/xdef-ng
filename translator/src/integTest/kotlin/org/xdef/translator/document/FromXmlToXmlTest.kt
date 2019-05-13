package org.xdef.translator.document

import io.kotlintest.shouldNotThrow
import org.xdef.translator.dataset.xml.*
import org.xmlunit.assertj.XmlAssert

/**
 * Integration tests for translation document from XML to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToXmlTest : BaseDocumentTranslatorTest() {

    init {
        "document" - {
            "minimal" - {
                "empty element" { xmlAssertEquals(minimalElement) }
                "text" { xmlAssertEquals(minimalText) }
                "empty and comment" { xmlAssertEquals(minimalEmptyAndComment) }
                "empty with comment" { xmlAssertEquals(minimalEmptyWithComment) }
            }
            "simple" - {
                "with attributes" { xmlAssertEquals(simpleElement) }
                "with comments" { xmlAssertEquals(simpleElementWithComment) }
                "with namespaces" { xmlAssertEquals(simpleWithNamespace) }
            }
            "complex" - {
                "basic" { xmlResAssertEquals("complexDocument") }
                "mixed" { xmlResAssertEquals("complexMixedDocument") }
            }
        }
        "stream" - {
            "minimal" - {
                "empty element" { xmlStreamAssertEquals(minimalElement) }
                "text" { xmlStreamAssertEquals(minimalText) }
                "empty and comment" { xmlStreamAssertEquals(minimalEmptyAndComment) }
                "empty with comment" { xmlStreamAssertEquals(minimalEmptyWithComment) }
                "with namespace" { xmlStreamAssertEquals(simpleWithNamespace) }
            }
            "simple" - {
                "with attributes" { xmlStreamAssertEquals(simpleElement) }
                "with comments" { xmlStreamAssertEquals(simpleElementWithComment) }
                "with namespaces" { xmlStreamAssertEquals(simpleWithNamespace) }
            }
            "complex" - {
                "basic" { xmlStreamResAssertEquals("complexDocument") }
                "mixed" { xmlStreamResAssertEquals("complexMixedDocument") }
            }
        }
    }

    private fun xmlAssertEquals(xml: String) {
        val document = xmlTranslator.readDocument(autoClose(xml.reader()))
        xmlTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreElementContentWhitespace()
                .areSimilar()
        }
    }

    private fun xmlResAssertEquals(xmlRes: String) {
        val document = xmlRes.resToDocument()
        xmlTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(outputWriter.toString())
                .and(xmlRes.xmlResToString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun xmlStreamAssertEquals(xml: String) {
        val reader = xml.toDocumentReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreElementContentWhitespace()
                .areSimilar()
        }
    }

    private fun xmlStreamResAssertEquals(xml: String) {
        val reader = xml.resToReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml.xmlResToString())
                .and(outputWriter.toString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun String.xmlResToString() = "$this.xml".resToString()

    private fun String.toDocumentReader() = toDocumentReader(xmlTranslator)

    private fun String.resToDocument() = "$this.xml".resToDocument(xmlTranslator)

    private fun String.resToReader() = "$this.xml".resToReader(xmlTranslator)

    private fun createWriter() = createWriter(xmlTranslator)
}
