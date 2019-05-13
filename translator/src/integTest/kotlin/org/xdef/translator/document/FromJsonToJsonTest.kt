package org.xdef.translator.document

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.json.*
import java.math.BigDecimal

/**
 * Integrations test for translation document from JSON to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToJsonTest : BaseDocumentTranslatorTest() {

    init {
        "document" - {
            "minimal" - {
                "empty object" { jsonAssertEquals(minimalObject) }
                "empty array" { jsonAssertEquals(minimalArray) }
                "string value" { jsonAssertEquals(minimalString) }
                // JSONAssert not support number similarity
                "number value" {
                    val document = jsonTranslator.readDocument(autoClose(minimalNumber.reader()))
                    jsonTranslator.writeDocument(document, outputWriter)
                    BigDecimal(outputWriter.toString()) shouldBe BigDecimal(minimalNumber)
                }
                // JSONAssert not support boolean as root value
                "boolean value" {
                    val document = jsonTranslator.readDocument(autoClose(minimalBoolean.reader()))
                    jsonTranslator.writeDocument(document, outputWriter)
                    outputWriter.toString() shouldBe minimalBoolean
                }
                // JSONAssert not support null as root value
                "null value" {
                    val document = jsonTranslator.readDocument(autoClose(minimalUndefined.reader()))
                    jsonTranslator.writeDocument(document, outputWriter)
                    outputWriter.toString() shouldBe minimalUndefined
                }
            }
            "simple" - {
                "object" { jsonAssertEquals(simpleObject) }
                "number array" { jsonAssertEquals(simpleNumberArray) }
                "string array" { jsonAssertEquals(simpleStringArray) }
                "mixed array" { jsonAssertEquals(simpleMixedArray) }
            }
            "complex" - {
                "homogenous array" { jsonAssertEquals("complexHomogenousArray".jsonResToString()) }
                "mixed" { jsonAssertEquals("complexMixedDocument".jsonResToString()) }
            }
        }
        "stream" - {
            "minimal" - {
                "empty object" { jsonStreamAssertEquals(minimalObject) }
                "empty array" { jsonStreamAssertEquals(minimalArray) }
                "string value" { jsonStreamAssertEquals(minimalString) }
                // JSONAssert not support number similarity
                "number value" {
                    val reader = minimalNumber.toDocumentReader()
                    val writer = createWriter()
                    reader.forEach { event -> writer.writeEvent(event) }
                    BigDecimal(outputWriter.toString()) shouldBe BigDecimal(minimalNumber)
                }
                // JSONAssert not support boolean as root value
                "boolean value" {
                    val reader = minimalBoolean.toDocumentReader()
                    val writer = createWriter()
                    reader.forEach { event -> writer.writeEvent(event) }
                    outputWriter.toString() shouldBe minimalBoolean
                }
                // JSONAssert not support null as root value
                "null value" {
                    val reader = minimalUndefined.toDocumentReader()
                    val writer = createWriter()
                    reader.forEach { event -> writer.writeEvent(event) }
                    outputWriter.toString() shouldBe minimalUndefined
                }
            }
            "simple" - {
                "object" { jsonStreamAssertEquals(simpleObject) }
                "number array" { jsonStreamAssertEquals(simpleNumberArray) }
                "string array" { jsonStreamAssertEquals(simpleStringArray) }
                "mixed array" { jsonStreamAssertEquals(simpleMixedArray) }
            }
            "complex" - {
                "homogenous array" { jsonStreamResAssertEquals("complexHomogenousArray") }
                "mixed" { jsonStreamResAssertEquals("complexMixedDocument") }
            }
        }
    }

    private fun jsonAssertEquals(json: String, strict: Boolean = false) {
        val document = jsonTranslator.readDocument(autoClose(json.reader()))
        jsonTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), strict)
        }
    }

    private fun jsonStreamAssertEquals(json: String, strict: Boolean = false) {
        val reader = json.toDocumentReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), strict)
        }
    }

    private fun jsonStreamResAssertEquals(jsonRes: String, strict: Boolean = false) {
        val reader = jsonRes.resToReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(jsonRes.jsonResToString(), outputWriter.toString(), strict)
        }
    }

    private fun String.jsonResToString() = "$this.json".resToString()

    private fun String.toDocumentReader() = toDocumentReader(jsonTranslator)

    private fun String.resToDocument() = "$this.json".resToDocument(jsonTranslator)

    private fun String.resToReader() = "$this.json".resToReader(jsonTranslator)

    private fun createWriter() = createWriter(jsonTranslator)
}
