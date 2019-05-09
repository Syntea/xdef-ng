package org.xdef.translator.document

import io.kotlintest.shouldNotThrow
import org.xdef.translator.dataset.json.*
import org.xdef.translator.document.json.JsonDocumentTranslator.Companion.ROOT_NODE_NAME
import org.xmlunit.assertj.XmlAssert
import java.math.BigDecimal

/**
 * Integration test for reading JSON to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToXmlTest : BaseDocumentTranslatorTest() {

    init {
        "minimal document" - {
            "empty object" {
                assertEquals(
                    minimalObject,
                    """<$ROOT_NODE_NAME/>"""
                )
            }
            "empty array" {
                assertEquals(
                    minimalArray,
                    """<$ROOT_NODE_NAME/>"""
                )
            }
            "string value" {
                assertEquals(
                    minimalString,
                    """<$ROOT_NODE_NAME/>"""
                )
            }
            "number value" {
                assertEquals(
                    minimalNumber,
                    """<$ROOT_NODE_NAME>${BigDecimal(minimalNumber)}</$ROOT_NODE_NAME>"""
                )
            }
            "boolean value" {
                assertEquals(
                    minimalBoolean,
                    """<$ROOT_NODE_NAME>$minimalBoolean</$ROOT_NODE_NAME>"""
                )
            }
            "null value" {
                assertEquals(
                    minimalUndefined,
                    """<$ROOT_NODE_NAME/>"""
                )
            }
        }
        "simple document" - {
            "object" {
                assertEquals(
                    simpleObject,
                    """<$ROOT_NODE_NAME
                            A="A-value"
                            B="false"
                            C="12345"
                            U=""
                        />
                    """
                )
            }
            "number array" {
                assertEquals(
                    simpleNumberArray,
                    """<$ROOT_NODE_NAME>1
                        |2
                        |3
                        |4
                        |5</$ROOT_NODE_NAME>
                    """.trimMargin()
                )
            }
//            "string array" { assertEquals(simpleStringArray) }
//            "mixed array" {
//                assertEquals(
//                    simpleMixedArray,
//                    """<$ROOT_NODE_NAME>
//                            |A
//                            |1
//                            |<$ROOT_NODE_NAME.3/>
//                            |false
//                            |<$ROOT_NODE_NAME.5></$ROOT_NODE_NAME.5>
//                            |true
//                        |</$ROOT_NODE_NAME>
//                    """.trimMargin()
//                )
//            }
        }
    }

    private fun assertEquals(json: String, expected: String) {
        val document = jsonTranslator.readDocument(autoClose(json.reader()))
        xmlTranslator.writeDocument(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(expected)
                .and(outputWriter.toString())
                .ignoreElementContentWhitespace()
                .areSimilar()
        }
    }
}
