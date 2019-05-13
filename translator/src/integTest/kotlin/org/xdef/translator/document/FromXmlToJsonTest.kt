package org.xdef.translator.document

import io.kotlintest.shouldNotThrow
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.translator.dataset.xml.*

/**
 * Integration tests for translation document from XML to JSON
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromXmlToJsonTest : BaseDocumentTranslatorTest() {

    private val minimalElementAsJson = """{"A": {}}"""
    private val minimalTextAsJson = """{"T": {"T.0": "minimalElement text"}}"""
    private val minimalEmptyAndCommentAsJson = """{"A": {}}"""
    private val minimalEmptyWithCommentAsJson = """{"A": {}}"""
    private val simpleElementAsJson = """{"root": {"a": "a", "b": "b", "c": "c"}}"""
    private val simpleElementWithCommentAsJson = """{"root": {"a": "a", "b": "b", "c": "c"}}"""
    private val simpleWithNamespaceAsJson = """{
        |"root" : {
        |   "root.0" : "\n\n",
        |   "h:table" : {
        |       "h:tr" : { }
        |   },
        |   "root.2" : "\n\n",
        |   "f:table" : {
        |       "f:name" : { }
        |   },
        |   "root.4" : "\n\n"
        |}
}""".trimMargin()
    private val complexBasicAsJson = """
{
  "List" : {
    "List.0" : "\n    ",
    "Employee" : [ {
      "Name" : "John",
      "FamilyName" : "Braun",
      "Ingoing" : "2004-10-01",
      "Salary" : "2000",
      "Qualification" : "worker"
    }, {
      "Name" : "Mary",
      "FamilyName" : "White",
      "Ingoing" : "1998-19-13",
      "Salary" : "abcd",
      "Qualification" : ""
    }, {
      "Name" : "Peter",
      "FamilyName" : "Black",
      "Ingoing" : "1998-02-13",
      "Salary" : "1600",
      "Qualification" : "electrician"
    } ],
    "List.2" : "\n    ",
    "List.4" : "\n    ",
    "List.6" : "\n"
  }
}"""
    private val complexMixedAsJson = """
{
  "root" : {
    "a" : "aRoot",
    "root.0" : "\n    TextContent1\n    ",
    "h:table" : {
      "h:table.0" : "\n        ",
      "h:tr" : {
        "h:tr.0" : "\n            ",
        "h:td" : [ {
          "h:td.0" : "Apples"
        }, {
          "h:td.0" : "Bananas"
        } ],
        "h:tr.2" : "\n            ",
        "h:tr.4" : "\n        "
      },
      "h:table.2" : "\n    "
    },
    "root.2" : "\n    TextContent2\n    ",
    "f:table" : {
      "f:table.0" : "\n        ",
      "f:name" : {
        "f:name.0" : "African Coffee Table"
      },
      "f:table.2" : "\n        ",
      "f:width" : {
        "f:width.0" : "80"
      },
      "f:table.4" : "\n        ",
      "f:length" : {
        "f:length.0" : "120"
      },
      "f:table.6" : "\n    "
    },
    "root.4" : "\n    TextContent3\n"
  }
}"""

    init {
        "document" - {
            "minimal" - {
                "empty element" { assertEquals(minimalElement, minimalElementAsJson) }
                "text" { assertEquals(minimalText, minimalTextAsJson) }
                "empty and comment" { assertEquals(minimalEmptyAndComment, minimalEmptyAndCommentAsJson) }
                "empty with comment" { assertEquals(minimalEmptyWithComment, minimalEmptyWithCommentAsJson) }
            }
            "simple" - {
                "with attributes" { assertEquals(simpleElement, simpleElementAsJson) }
                "with comments" { assertEquals(simpleElementWithComment, simpleElementWithCommentAsJson) }
                "with namespaces" { assertEquals(simpleWithNamespace, simpleWithNamespaceAsJson) }
            }
            "complex" - {
                "basic" { resAssertEquals("complexDocument", complexBasicAsJson) }
                "mixed" { resAssertEquals("complexMixedDocument", complexMixedAsJson) }
            }
        }
        // TODO: Now it isn't specified streaming translation between formats
        "stream" - {
            "minimal" - {
                "empty element" { streamAssertEquals(minimalElement, minimalElementAsJson) }
//                "text" { streamAssertEquals(minimalText, minimalTextAsJson) }
//                "empty and comment" { streamAssertEquals(minimalEmptyAndComment, minimalEmptyAndCommentAsJson) }
//                "empty with comment" { streamAssertEquals(minimalEmptyWithComment, minimalEmptyWithCommentAsJson) }
            }
//            "simple" - {
//                "with attributes" { streamAssertEquals(simpleElement, simpleElementAsJson) }
//                "with comments" { streamAssertEquals(simpleElementWithComment, simpleElementWithCommentAsJson) }
//                "with namespaces" { streamAssertEquals(simpleWithNamespace, simpleWithNamespaceAsJson) }
//            }
//            "complex" - {
//                "basic" { streamResAssertEquals("complexDocument", complexBasicAsJson) }
//                "mixed" { streamResAssertEquals("complexMixedDocument", complexMixedAsJson) }
//            }
        }
    }

    private fun assertEquals(xml: String, json: String) {
        val document = xmlTranslator.readDocument(autoClose(xml.reader()))
        jsonTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun resAssertEquals(xmlRes: String, json: String) {
        val document = xmlRes.resToDocument()
        jsonTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun streamAssertEquals(xml: String, json: String) {
        val reader = xml.toDocumentReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        println(outputWriter.toString())
        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun streamResAssertEquals(xmlRes: String, json: String) {
        val reader = xmlRes.resToReader()
        val writer = createWriter()
        reader.forEach { event -> writer.writeEvent(event) }

        shouldNotThrow<AssertionError> {
            JSONAssert.assertEquals(json, outputWriter.toString(), false)
        }
    }

    private fun String.xmlResToString() = "$this.xml".resToString()

    private fun String.toDocumentReader() = toDocumentReader(xmlTranslator)

    private fun String.resToDocument() = "$this.xml".resToDocument(xmlTranslator)

    private fun String.resToReader() = "$this.xml".resToReader(xmlTranslator)

    private fun createWriter() = createWriter(jsonTranslator)
}
