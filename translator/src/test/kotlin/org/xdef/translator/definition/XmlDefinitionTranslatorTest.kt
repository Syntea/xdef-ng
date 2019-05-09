package org.xdef.translator.definition

import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.xdef.translator.definition.xml.XmlDefinitionTranslator
import java.io.StringWriter

/**
 * Tests for [XmlDefinitionTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class XmlDefinitionTranslatorTest : FreeSpec() {

    private val translator = XmlDefinitionTranslator()

    init {
        "read definition" - {
            //            "empty" {
//                shouldThrow<JDOMParseException> { "".toDefinition() }
//            }
//            "minimal" - {
//                "element" {
//                    minimalElement.toDefinition() should { doc ->
//                        doc.type.name shouldBe SupportedDataType.XML.name
//                        doc.shouldBeInstanceOf<XmlXDocument>()
//                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
//                            node.name shouldBe "A"
//                            node.attributes.shouldBeEmpty()
//                            node.children.shouldBeEmpty()
//                        }
//                    }
//                }
//                "text" {
//                    minimalText.toDefinition() should { doc ->
//                        doc.type.name shouldBe SupportedDataType.XML.name
//                        doc.shouldBeInstanceOf<XmlXDocument>()
//                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
//                            node.name shouldBe "T"
//                            node.attributes.shouldBeEmpty()
//                            node.children shouldHaveSize 1
//                            node.children[0].shouldBeInstanceOf<XmlXLeaf> { leaf ->
//                                leaf.value.shouldBeInstanceOf<XmlTextXValue> { it.value shouldBe "minimalElement text" }
//                            }
//                        }
//                    }
//                }
//                "with comment" {
//                    minimalEmptyWithComment.toDefinition() should { doc ->
//                        doc.type.name shouldBe SupportedDataType.XML.name
//                        doc.shouldBeInstanceOf<XmlXDocument>()
//                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
//                            node.name shouldBe "A"
//                            node.attributes.shouldBeEmpty()
//                            node.children.shouldBeEmpty()
//                        }
//                    }
//                }
//            }
//            "simple" - {
//                "with attributes" {
//                    simpleElement.toDefinition() should { doc ->
//                        doc.type.name shouldBe SupportedDataType.XML.name
//                        doc.shouldBeInstanceOf<XmlXDocument>()
//                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
//                            node.name shouldBe "root"
//                            node.attributes shouldHaveSize 3
//                            node.attributes[0].shouldBeInstanceOf<XmlXAttribute> { attribute ->
//                                attribute.name shouldBe "a"
//                                attribute.value?.value shouldBe "a"
//                            }
//                            node.attributes[1].shouldBeInstanceOf<XmlXAttribute> { attribute ->
//                                attribute.name shouldBe "b"
//                                attribute.value?.value shouldBe "b"
//                            }
//                            node.attributes[2].shouldBeInstanceOf<XmlXAttribute> { attribute ->
//                                attribute.name shouldBe "c"
//                                attribute.value?.value shouldBe "c"
//                            }
//                            node.children.shouldBeEmpty()
//                        }
//                    }
//                }
//            }
            "complex" - {
                "basic" {
                    "definition".resToDefinition().let {

                        val w = StringWriter()
                        translator.writeDefinition(it, w)
                        println(w.toString())
                    }
                }
            }
        }
    }

    private fun String.toDefinition() = translator.readDefinition(autoClose(byteInputStream()))

    private fun String.resToDefinition() = translator.readDefinition(
        autoClose(
            XmlDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xdef")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            XmlDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xdef").reader()
        )
    )
}
