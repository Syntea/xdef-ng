package org.xdef.translator.document

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.json.*
import java.math.BigDecimal

/**
 * Integration test for reading XML to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToJsonTest : BaseDocumentTranslatorTest() {

    init {
        "minimalElement document" - {
            "empty object" { jsonAssertEquals(minimalObject) }
            "empty array" { jsonAssertEquals(minimalArray) }
            "string value" { jsonAssertEquals(minimalString) }
            // JSONAssert not support number as root value
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
        "simple document" - {
            "object" { jsonAssertEquals(simpleObject) }
            "number array" { jsonAssertEquals(simpleNumberArray) }
            "string array" { jsonAssertEquals(simpleStringArray) }
            "mixed array" { jsonAssertEquals(simpleMixedArray) }
        }
    }

    private fun jsonAssertEquals(json: String, strict: Boolean = false) {
        val document = jsonTranslator.readDocument(autoClose(json.reader()))
        jsonTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), strict)
        }
    }
}
