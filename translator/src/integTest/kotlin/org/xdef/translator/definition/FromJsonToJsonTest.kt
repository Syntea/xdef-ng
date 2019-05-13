package org.xdef.translator.definition

import io.kotlintest.shouldNotThrow
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.json.*

/**
 * Integration tests for translation X-definition document from JSON to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToJsonTest : BaseDefinitionTranslatorTest() {

    init {
        "definition" - {
            "minimal" - {
                "object" { jsonAssertEquals(minimalDefObject) }
                "array" { jsonAssertEquals(minimalDefArray) }
                "value" { jsonAssertEquals(minimalDefValue) }
            }
            "simple" - {
                "object" { jsonAssertEquals(simpleDefObject) }
                "array" { jsonAssertEquals(simpleDefArray) }
            }
            "complex" - {
                "mixed" { jsonAssertEquals("complexMixedDefinition".jsonResToString()) }
            }
        }
    }

    private fun jsonAssertEquals(json: String, strict: Boolean = false) {
        val document = jsonTranslator.readDefinition(autoClose(json.reader()))
        jsonTranslator.writeDefinition(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), strict)
        }
    }

    private fun String.jsonResToString() = "$this.jdef".resToString()

    private fun String.resToDefinition() = "$this.jdef".resToDefinition(jsonTranslator)
}
