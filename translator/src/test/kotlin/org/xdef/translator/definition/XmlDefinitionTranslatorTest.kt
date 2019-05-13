package org.xdef.translator.definition

import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.string.shouldInclude
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.jdom2.Attribute
import org.jdom2.Element
import org.jdom2.Namespace
import org.jdom2.Text
import org.jdom2.input.JDOMParseException
import org.xdef.core.Location
import org.xdef.core.document.data.XValue
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.core.document.definition.XDefLeaf
import org.xdef.core.document.definition.XDefNode
import org.xdef.core.document.definition.XDefTree
import org.xdef.translator.SupportedDataType
import org.xdef.translator.dataset.xml.minimalElement
import org.xdef.translator.dataset.xml.minimalEmptyWithComment
import org.xdef.translator.dataset.xml.minimalText
import org.xdef.translator.dataset.xml.simpleDefElement
import org.xdef.translator.definition.xml.XmlDefinitionTranslator
import org.xdef.translator.definition.xml.model.XmlXDefAttribute
import org.xdef.translator.definition.xml.model.XmlXDefDocument
import org.xdef.translator.definition.xml.model.XmlXDefLeaf
import org.xdef.translator.definition.xml.model.XmlXDefNode
import org.xdef.translator.document.xml.model.XmlTextXValue
import org.xmlunit.assertj.XmlAssert
import java.io.StringWriter

/**
 * Tests for [XmlDefinitionTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class XmlDefinitionTranslatorTest : FreeSpec() {

    private val translator = XmlDefinitionTranslator()

    private lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    init {
        "read definition" - {
            "empty" {
                shouldThrow<JDOMParseException> { "".toDefinition() }
            }
            "minimal" - {
                "element" {
                    minimalElement.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.XML.name
                        def.shouldBeInstanceOf<XmlXDefDocument>()
                        def.root.shouldBeInstanceOf<XmlXDefNode> { node ->
                            node.name shouldBe "A"
                            node.script.shouldBeNull()
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "text" {
                    minimalText.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.XML.name
                        def.shouldBeInstanceOf<XmlXDefDocument>()
                        def.root.shouldBeInstanceOf<XmlXDefNode> { node ->
                            node.name shouldBe "T"
                            node.script.shouldBeNull()
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 1
                            node.children[0].shouldBeInstanceOf<XmlXDefLeaf> { leaf ->
                                leaf.script.shouldNotBeNull()
                                leaf.script shouldBe "minimalElement text"
                                leaf.value.shouldBeInstanceOf<XmlTextXValue> { it.value shouldBe "minimalElement text" }
                            }
                        }
                    }
                }
                "with comment" {
                    minimalEmptyWithComment.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.XML.name
                        def.shouldBeInstanceOf<XmlXDefDocument>()
                        def.root.shouldBeInstanceOf<XmlXDefNode> { node ->
                            node.name shouldBe "A"
                            node.script.shouldBeNull()
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
            }
            "simple" {
                simpleDefElement.toDefinition() should { def ->
                    def.type.name shouldBe SupportedDataType.XML.name
                    def.shouldBeInstanceOf<XmlXDefDocument>()
                    def.root.shouldBeInstanceOf<XmlXDefNode> { node ->
                        node.name shouldBe "root"
                        node.script shouldBe "required"
                        node.attributes shouldHaveSize 3
                        node.attributes[0].shouldBeInstanceOf<XmlXDefAttribute> { attribute ->
                            attribute.name shouldBe "a"
                            attribute.value?.value shouldBe "a"
                        }
                        node.attributes[1].shouldBeInstanceOf<XmlXDefAttribute> { attribute ->
                            attribute.name shouldBe "b"
                            attribute.value?.value shouldBe "b"
                        }
                        node.attributes[2].shouldBeInstanceOf<XmlXDefAttribute> { attribute ->
                            attribute.name shouldBe "c"
                            attribute.value?.value shouldBe "c"
                        }
                        node.children.shouldBeEmpty()
                    }
                }
            }
            "complex" {
                "complexDefinition".resToDefinition() should { def ->
                    def.type.name shouldBe SupportedDataType.XML.name
                    def.shouldBeInstanceOf<XmlXDefDocument>()
                    def.root.shouldBeInstanceOf<XmlXDefNode> { root ->
                        root.children.first { "List" == it.name }.shouldBeInstanceOf<XmlXDefNode> { node ->
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 3
                            node.children[0].shouldBeInstanceOf<XmlXDefLeaf> { leaf ->
                                leaf.script.shouldNotBeNull()
                                leaf.script shouldInclude "required string()"
                            }
                            node.children[1].shouldBeInstanceOf<XmlXDefNode> { leaf ->
                                leaf.script.shouldNotBeNull()
                                leaf.script shouldBe "occurs 0..*; onAbsence outln('Not Found')"
                                leaf.attributes shouldHaveSize 6
                                leaf.attributes.forEach { it.script shouldBe "required string(1,30)" }
                                leaf.children shouldHaveSize 1
                                leaf.children.first().script shouldInclude "required string(10,999)"
                            }
                            node.children[2].shouldBeInstanceOf<XmlXDefLeaf> { leaf ->
                                leaf.script.shouldNotBeNull()
                                leaf.script shouldInclude "required string()"
                            }
                        }
                    }
                }
            }
        }
        "write definition" - {
            "minimal" - {
                "empty element" {
                    translator.writeDefinition(
                        TestXmlXDefDocument(
                            root = XDefNode(
                                name = "A",
                                script = null,
                                attributes = emptyList(),
                                children = emptyList(),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalElement)
                        .areSimilar()
                }
                "text" {
                    translator.writeDefinition(
                        TestXmlXDefDocument(
                            root = XDefNode(
                                name = "T",
                                script = null,
                                attributes = emptyList(),
                                children = listOf(
                                    XDefLeaf(
                                        name = "XXX", // Not used
                                        value = XmlTextXValue("minimalElement text"),
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    )
                                ),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalText)
                        .areSimilar()
                }
                "empty with comment" {
                    translator.writeDefinition(
                        TestXmlXDefDocument(
                            root = XDefNode(
                                name = "A",
                                script = null,
                                attributes = emptyList(),
                                children = emptyList(),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalEmptyWithComment)
                        .ignoreComments()
                        .areSimilar()
                }
            }
            "simple" {
                translator.writeDefinition(
                    TestXmlXDefDocument(
                        root = XDefNode(
                            name = "root",
                            script = "required",
                            attributes = listOf(
                                TestXmlXDefAttribute("a", XmlTextXValue("a")),
                                TestXmlXDefAttribute("b", XmlTextXValue("b")),
                                TestXmlXDefAttribute("c", XmlTextXValue("c"))
                            ),
                            children = emptyList(),
                            allowedOccurrences = emptyList(),
                            allowedEvents = emptyList(),
                            location = Location.NO_LOCATION
                        )
                    ),
                    outputWriter
                )
                XmlAssert.assertThat(outputWriter.toString())
                    .and(simpleDefElement)
                    .ignoreComments()
                    .areSimilar()
            }
            "complex" {
                // Use testing over JDOM mapping
                val element = Element("def", "xd", "http://www.syntea.cz/xdef/3.1")
                    .setAttributes(
                        listOf(
                            Attribute("root", "List", Namespace.getNamespace("xd", "http://www.syntea.cz/xdef/3.1")),
                            Attribute("name", "Example", Namespace.getNamespace("xd", "http://www.syntea.cz/xdef/3.1"))
                        )
                    )
                    .setContent(
                        Element("List")
                            .setContent(
                                listOf(
                                    Text("required string()"),
                                    Element("Employee")
                                        .setAttributes(
                                            listOf(
                                                Attribute(
                                                    "script",
                                                    "occurs 0..*; onAbsence outln('Not Found')",
                                                    Namespace.getNamespace("xd", "http://www.syntea.cz/xdef/3.1")
                                                ),
                                                Attribute(
                                                    "Name",
                                                    "required string(1,30)",
                                                    Namespace.getNamespace("test", "http://a")
                                                ),
                                                Attribute("FamilyName", "required string(1,30)"),
                                                Attribute("Ingoing", "required string(1,30)"),
                                                Attribute("Salary", "required string(1,30)"),
                                                Attribute("Qualification", "required string(1,30)"),
                                                Attribute("SecondaryQualification", "required string(1,30)")
                                            )
                                        )
                                        .setContent(Text("required string(10,999)")),
                                    Text("required string()")
                                )
                            )
                    )
                translator.writeDefinition(
                    TestXmlXDefDocument(
                        root = XmlXDefNode(
                            element = element,
                            name = "xd:def",
                            script = null,
                            attributes = emptyList(),
                            children = emptyList(),
                            allowedOccurrences = emptyList(),
                            allowedEvents = emptyList(),
                            location = Location.NO_LOCATION
                        )
                    ),
                    outputWriter
                )
                XmlAssert.assertThat(outputWriter.toString())
                    .and("complexDefinition".resToString())
                    .ignoreComments()
                    .ignoreWhitespace()
                    .areSimilar()
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

    /**
     * Dummy XML X-definition document implementation
     * Only for test of writing
     */
    private class TestXmlXDefDocument(override val root: XDefTree) : BaseXDefDocument(SupportedDataType.XML)

    private class TestXmlXDefAttribute(name: String, value: XValue?) :
        XDefAttribute(name, value, emptyList(), emptyList())
}
