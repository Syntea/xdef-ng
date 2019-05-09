package org.xdef.translator.document

import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.jdom2.input.JDOMParseException
import org.xdef.core.Location
import org.xdef.core.document.data.XNode
import org.xdef.translator.SupportedDataType
import org.xdef.translator.dataset.xml.*
import org.xdef.translator.document.xml.XmlDocumentTranslator
import org.xdef.translator.document.xml.model.*
import java.io.StringWriter

/**
 * Tests for [XmlDocumentTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class XmlDocumentTranslatorTest : FreeSpec() {

    private val translator = XmlDocumentTranslator()

    init {
        "read document" - {
            "empty" {
                shouldThrow<JDOMParseException> { "".toDocument() }
            }
            "minimal" - {
                "element" {
                    minimalElement.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "A"
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "text" {
                    minimalText.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "T"
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 1
                            node.children[0].shouldBeInstanceOf<XmlXLeaf> { leaf ->
                                leaf.value.shouldBeInstanceOf<XmlTextXValue> { it.value shouldBe "minimalElement text" }
                            }
                        }
                    }
                }
                "with comment" {
                    minimalEmptyWithComment.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "A"
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
            }
            "simple" - {
                "with attributes" {
                    simpleElement.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "root"
                            node.attributes shouldHaveSize 3
                            node.attributes[0].shouldBeInstanceOf<XmlXAttribute> { attribute ->
                                attribute.name shouldBe "a"
                                attribute.value?.value shouldBe "a"
                            }
                            node.attributes[1].shouldBeInstanceOf<XmlXAttribute> { attribute ->
                                attribute.name shouldBe "b"
                                attribute.value?.value shouldBe "b"
                            }
                            node.attributes[2].shouldBeInstanceOf<XmlXAttribute> { attribute ->
                                attribute.name shouldBe "c"
                                attribute.value?.value shouldBe "c"
                            }
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "test" {
                    simpleElementWithComment.toDocument().let {
                        (it.root as XmlXNode).children =
                            (it.root as XmlXNode).children + XNode(
                                "Ahoj",
                                emptyList(),
                                emptyList(),
                                Location.NO_LOCATION
                            )


                        val sw = StringWriter()
                        translator.writeDocument(it, sw)
                        println(sw.toString())
                    }
                }
            }
        }
    }

    private fun String.toDocument() = translator.readDocument(autoClose(byteInputStream()))

    private fun String.resToDocument() = translator.readDocument(
        autoClose(
            XmlDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xml")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            XmlDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xml").reader()
        )
    )
}
