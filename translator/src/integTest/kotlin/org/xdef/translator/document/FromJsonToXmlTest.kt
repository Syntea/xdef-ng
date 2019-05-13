package org.xdef.translator.document

import io.kotlintest.shouldNotThrow
import org.xdef.translator.JSON_ROOT_NODE_NAME
import org.xdef.translator.dataset.json.*
import org.xmlunit.assertj.XmlAssert
import java.math.BigDecimal

/**
 * Integration tests for translation document from JSON to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToXmlTest : BaseDocumentTranslatorTest() {

    private val minimalObjectAsXml = "<$JSON_ROOT_NODE_NAME/>"
    private val minimalArrayAsXml = "<$JSON_ROOT_NODE_NAME/>"
    private val minimalStringAsXml = "<$JSON_ROOT_NODE_NAME/>"
    private val minimalNumberAsXml = "<$JSON_ROOT_NODE_NAME>${BigDecimal(minimalNumber)}</$JSON_ROOT_NODE_NAME>"
    private val minimalBooleanAsXml = "<$JSON_ROOT_NODE_NAME>$minimalBoolean</$JSON_ROOT_NODE_NAME>"
    private val nullAsXml = "<$JSON_ROOT_NODE_NAME/>"
    private val simpleObjectAsXml = """<$JSON_ROOT_NODE_NAME
        A="A-value"
        B="false"
        C="12345"
        U=""/>"""
    private val simpleNumberArrayAsXml = """<$JSON_ROOT_NODE_NAME>
        |1
        |2
        |3
        |4
        |5</$JSON_ROOT_NODE_NAME>""".trimMargin()
    private val simpleMixedArrayAsXml = """<$JSON_ROOT_NODE_NAME>
        |A
        |1
        |<$JSON_ROOT_NODE_NAME.3/>
        |false
        |<$JSON_ROOT_NODE_NAME.5></$JSON_ROOT_NODE_NAME.5>
        |true
        |</$JSON_ROOT_NODE_NAME>""".trimMargin()
    private val complexHomogenousArray = """
<org.xdef.root>
  <org.xdef.root.0 A="A-value" B="true" C="12345" U="" />
  <org.xdef.root.1 A="A-value" B="true" C="12345" U="" />
  <org.xdef.root.2 A="A-value" B="true" C="12345" U="" />
  <org.xdef.root.3 A="A-value" B="true" C="12345" U="" />
</org.xdef.root>"""
    private val complexMixed = """
<org.xdef.root
    _id="dsfkjlkad516ds"
    index="1345"
    guid="a7354b5e-1547-4d80-857b-074ec78e3868"
    isActive="false" balance=",790.49"
    picture="http://placehold.it/32x32"
    age="37"
    eyeColor="green"
    company="INDEXIA"
    email="avila.johnston@indexia.io"
    phone="+1 (847) 477-2146"
    address="682 Elton Street, Newkirk, Nebraska, 3946"
    greeting="Hello, Avila! You have 5 unread messages."
    favoriteFruit="strawberry">
  <name first="Avila" last="Johnston" />
  <tags>ad
duis
occaecat
incididunt
ut</tags>
<range>0
1
2
3
4
5
6
7
8
9</range>
  <friends>
    <friends.0 id="0" name="Davis Montgomery" />
  </friends>
</org.xdef.root>"""

    init {
        "document" - {
            "minimal" - {
                "empty object" { assertEquals(minimalObject, minimalObjectAsXml) }
                "empty array" { assertEquals(minimalArray, minimalArrayAsXml) }
                "string value" { assertEquals(minimalString, minimalStringAsXml) }
                "number value" { assertEquals(minimalNumber, minimalNumberAsXml) }
                "boolean value" { assertEquals(minimalBoolean, minimalBooleanAsXml) }
                "null value" { assertEquals(minimalUndefined, nullAsXml) }
            }
            "simple" - {
                "object" { assertEquals(simpleObject, simpleObjectAsXml) }
                "number array" { assertEquals(simpleNumberArray, simpleNumberArrayAsXml) }
                "mixed array" { assertEquals(simpleMixedArray, simpleMixedArrayAsXml) }
            }
            "complex" - {
                "homogenous array" { resAssertEquals("complexHomogenousArray", complexHomogenousArray) }
                "mixed" { resAssertEquals("complexMixedDocument", complexMixed) }
            }
        }
        // TODO: Now it isn't specified streaming translation between formats
//        "stream" - {
//            "minimal" - {
//                "empty object" { streamAssertEquals(minimalObject, minimalObjectAsXml) }
//                "empty array" { streamAssertEquals(minimalArray, minimalArrayAsXml) }
//                "string value" { streamAssertEquals(minimalString, minimalStringAsXml) }
//                "number value" { streamAssertEquals(minimalNumber, minimalNumberAsXml) }
//                "boolean value" { streamAssertEquals(minimalBoolean, minimalBooleanAsXml) }
//                "null value" { streamAssertEquals(minimalUndefined, nullAsXml) }
//            }
//            "simple" - {
//                "object" { streamAssertEquals(simpleObject, simpleObjectAsXml) }
//                "number array" { streamAssertEquals(simpleNumberArray, simpleNumberArrayAsXml) }
//                "mixed array" { streamAssertEquals(simpleMixedArray, simpleMixedArrayAsXml) }
//            }
//            "complex"  - {
//                "homogenous array" { resStreamAssertEquals("complexHomogenousArray", complexHomogenousArray) }
//                "mixed" { resStreamAssertEquals("complexMixedDocument", complexMixed) }
//            }
//        }
    }

    private fun assertEquals(json: String, xml: String) {
        val document = jsonTranslator.readDocument(autoClose(json.reader()))
        xmlTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun resAssertEquals(jsonRes: String, xml: String) {
        val document = jsonRes.resToDocument()
        xmlTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun streamAssertEquals(json: String, xml: String) {
        val reader = json.toDocumentReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun resStreamAssertEquals(jsonRes: String, xml: String) {
        val reader = jsonRes.resToReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(xml)
                .and(outputWriter.toString())
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun String.jsonResToString() = "$this.json".resToString()

    private fun String.toDocumentReader() = toDocumentReader(jsonTranslator)

    private fun String.resToDocument() = "$this.json".resToDocument(jsonTranslator)

    private fun String.resToReader() = "$this.json".resToReader(jsonTranslator)

    private fun createWriter() = createWriter(xmlTranslator)
}
