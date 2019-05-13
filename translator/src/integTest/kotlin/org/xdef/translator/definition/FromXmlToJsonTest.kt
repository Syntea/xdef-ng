package org.xdef.translator.definition

import io.kotlintest.shouldNotThrow
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.xml.*
import org.xdef.translator.definition.json.JsonDefinitionTranslator.Companion.X_SCRIPT_IDENTIFIER

/**
 * Integration tests for translation X-definition document from XML to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToJsonTest : BaseDefinitionTranslatorTest() {

    private val minimalElementAsJson = """{"A": {}}"""
    private val minimalTextAsJson = """{"T": {"T.0": "minimalElement text"}}"""
    private val minimalEmptyAndCommentAsJson = """{"A": {}}"""
    private val minimalEmptyWithCommentAsJson = """{"A": {}}"""
    private val simpleElementAsJson = """{
  "root" : {
    "a" : "a",
    "b" : "b",
    "c" : "c",
    "$X_SCRIPT_IDENTIFIER" : "required"
  }
}
"""
    private val complexAsJson = """{
  "xd:def" : {
    "xd:root" : "List",
    "xd:name" : "Example",
    "xd:def.0" : "\n    ",
    "List" : {
      "List.0" : "\n        required string()\n        ",
      "Employee" : {
        "test:Name" : "required string(1,30)",
        "FamilyName" : "required string(1,30)",
        "Ingoing" : "required string(1,30)",
        "Salary" : "required string(1,30)",
        "Qualification" : "required string(1,30)",
        "SecondaryQualification" : "required string(1,30)",
        "$X_SCRIPT_IDENTIFIER" : "occurs 0..*; onAbsence outln('Not Found')"
      },
      "List.2" : "\n        required string()\n    "
    },
    "xd:def.2" : "\n"
  }
}"""

    init {
        "definition" - {
            "minimal" - {
                "empty element" { xmlAssertEquals(minimalElement, minimalElementAsJson) }
                "text" { xmlAssertEquals(minimalText, minimalTextAsJson) }
                "with comment" { xmlAssertEquals(minimalEmptyAndComment, minimalEmptyAndCommentAsJson) }
                "empty with comment" { xmlAssertEquals(minimalEmptyWithComment, minimalEmptyWithCommentAsJson) }
            }
            "simple" {
                xmlAssertEquals(simpleDefElement, simpleElementAsJson)
            }
            "complex" {
                xmlResAssertEquals("complexDefinition", complexAsJson)
            }
        }
    }

    private fun xmlAssertEquals(xml: String, json: String) {
        val document = xmlTranslator.readDefinition(autoClose(xml.reader()))
        jsonTranslator.writeDefinition(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun xmlResAssertEquals(xmlRes: String, json: String) {
        val definition = xmlRes.resToDefinition()
        jsonTranslator.writeDefinition(definition, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun String.xmlResToString() = "$this.xdef".resToString()

    private fun String.resToDefinition() = "$this.xdef".resToDefinition(xmlTranslator)
}
