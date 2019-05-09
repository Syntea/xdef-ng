package org.xdef.translator.definition

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import org.apache.commons.io.IOUtils
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.json.*
import java.math.BigDecimal

/**
 * Integration test for reading JSON to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToJsonTest : BaseDefinitionTranslatorTest() {

    init {
        "minimal document" - {
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
        "complex document" - {
            "homogenous array" { jsonAssertEquals("complexHomogenousArray".resToString()) }
            "mixed" { jsonAssertEquals("complexMixedDefinition".resToString()) }
        }
    }

    private fun jsonAssertEquals(json: String, strict: Boolean = false) {
        val document = jsonTranslator.readDocument(autoClose(json.reader()))
        jsonTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), strict)
        }
    }

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            FromJsonToJsonTest::class.java.getResourceAsStream("/dataset/$this.jdef").reader()
        )
    )
}
