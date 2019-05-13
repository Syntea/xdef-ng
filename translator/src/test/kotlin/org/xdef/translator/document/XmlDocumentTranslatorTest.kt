package org.xdef.translator.document

import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.string.shouldBeBlank
import io.kotlintest.matchers.string.shouldInclude
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.jdom2.Attribute
import org.jdom2.Element
import org.jdom2.Text
import org.jdom2.input.JDOMParseException
import org.xdef.core.Location
import org.xdef.core.document.data.*
import org.xdef.translator.SupportedDataType
import org.xdef.translator.dataset.xml.*
import org.xdef.translator.document.xml.XmlDocumentTranslator
import org.xdef.translator.document.xml.model.*
import org.xmlunit.assertj.XmlAssert
import java.io.StringWriter

/**
 * Tests for [XmlDocumentTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class XmlDocumentTranslatorTest : FreeSpec() {

    private val translator = XmlDocumentTranslator()

    private lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

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
                "empty and comment" {
                    minimalEmptyAndComment.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "A"
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
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
                "empty with attributes" {
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
                "empty with comments" {
                    simpleElementWithComment.toDocument() should { doc ->
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
                "with namespace" {
                    simpleWithNamespace.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "root"
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 2 + 3 /*whitespace*/
                            node.children.filter { it is XmlXLeaf } shouldHaveSize 3
                            node.children.filter { it !is XmlXLeaf } should { children ->
                                children shouldHaveSize 2
                                children[0].shouldBeInstanceOf<XmlXNode> { child ->
                                    child.name shouldBe "h:table"
                                    child.attributes.shouldBeEmpty()
                                    child.children shouldHaveSize 1
                                    child.children.first().name shouldBe "h:tr"
                                }
                                children[1].shouldBeInstanceOf<XmlXNode> { child ->
                                    child.name shouldBe "f:table"
                                    child.attributes.shouldBeEmpty()
                                    child.children shouldHaveSize 1
                                    child.children.first().name shouldBe "f:name"
                                }
                            }
                        }
                    }
                }
            }
            "complex" - {
                "basic" {
                    "complexDocument".resToDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { node ->
                            node.name shouldBe "List"
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 3 + 4 /*whitespace*/
                            node.children.filter { it is XmlXLeaf } should { wss ->
                                wss shouldHaveSize 4
                                wss.forEach { ws ->
                                    ws.shouldBeInstanceOf<XmlXLeaf> {
                                        it.value.shouldNotBeNull()
                                        it.value!!.value.shouldBeBlank()
                                    }
                                }
                            }
                            node.children.filter { it !is XmlXLeaf } should { children ->
                                children shouldHaveSize 3
                                children.forEach { child ->
                                    child.shouldBeInstanceOf<XmlXNode> { xNode ->
                                        xNode.name shouldBe "Employee"
                                        xNode.attributes shouldHaveSize 5
                                        xNode.attributes.map { it.name } shouldContainExactlyInAnyOrder listOf(
                                            "Name", "FamilyName", "Ingoing", "Salary", "Qualification"
                                        )
                                        xNode.children.shouldBeEmpty()
                                    }
                                }
                            }
                        }
                    }
                }
                "mixed" {
                    "complexMixedDocument".resToDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.XML.name
                        doc.shouldBeInstanceOf<XmlXDocument>()
                        doc.root.shouldBeInstanceOf<XmlXNode> { root ->
                            root.name shouldBe "root"
                            root.attributes shouldHaveSize 1
                            root.attributes.first() should { attr ->
                                attr.name shouldBe "a"
                                attr.value.shouldNotBeNull()
                                attr.value!!.value shouldBe "aRoot"
                            }
                            root.children shouldHaveSize 5
                            root.children.filter { it is XmlXLeaf } should { textNodes ->
                                textNodes shouldHaveSize 3
                                textNodes.forEachIndexed { index, textNode ->
                                    textNode as XmlXLeaf
                                    textNode.value.shouldNotBeNull()
                                    // contains whitespaces too
                                    textNode.value!!.value shouldInclude "TextContent${index + 1}"
                                }
                            }
                            root.children.filter { it !is XmlXLeaf } should { elements ->
                                elements shouldHaveSize 2
                            }
                        }
                    }
                }
            }
        }
        "write document" - {
            "minimal" - {
                "empty element" {
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XmlXNode(
                                name = "A",
                                attributes = emptyList(),
                                children = emptyList(),
                                element = Element("A"),
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
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XmlXNode(
                                name = "T",
                                attributes = emptyList(),
                                children = listOf(
                                    XmlXLeaf(
                                        text = Text(""), // Not used
                                        name = "XXX", // Not used
                                        value = XmlTextXValue("minimalElement text"),
                                        location = Location.NO_LOCATION
                                    )
                                ),
                                element = Element("T"),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalText)
                        .areSimilar()
                }
                "empty and comment" {
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XmlXNode(
                                name = "A",
                                attributes = emptyList(),
                                children = emptyList(),
                                element = Element("A"),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalEmptyAndComment)
                        .ignoreComments()
                        .areSimilar()
                }
                "empty with comment" {
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XmlXNode(
                                name = "A",
                                attributes = emptyList(),
                                children = emptyList(),
                                element = Element("A"),
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
            "simple" - {
                "element with attributes" {
                    val element = Element("root")
                    element.setAttributes(listOf(Attribute("a", "a"), Attribute("b", "b"), Attribute("c", "c")))
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XmlXNode(
                                name = element.name,
                                attributes = emptyList(), // used information from JDOM element
                                children = emptyList(),
                                element = element,
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(simpleElement)
                        .ignoreComments()
                        .areSimilar()
                }
            }
            "complex" - {
                "basic" {
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XNode(
                                name = "List",
                                attributes = emptyList(),
                                children = listOf(
                                    XNode(
                                        name = "Employee",
                                        attributes = listOf(
                                            LocalizedXAttribute("Name", LocalizedXValue("John")),
                                            LocalizedXAttribute("FamilyName", LocalizedXValue("Braun")),
                                            LocalizedXAttribute("Ingoing", LocalizedXValue("2004-10-01")),
                                            LocalizedXAttribute("Salary", LocalizedXValue("2000")),
                                            LocalizedXAttribute("Qualification", LocalizedXValue("worker"))
                                        ),
                                        children = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    XNode(
                                        name = "Employee",
                                        attributes = listOf(
                                            LocalizedXAttribute("Name", LocalizedXValue("Mary")),
                                            LocalizedXAttribute("FamilyName", LocalizedXValue("White")),
                                            LocalizedXAttribute("Ingoing", LocalizedXValue("1998-19-13")),
                                            LocalizedXAttribute("Salary", LocalizedXValue("abcd")),
                                            LocalizedXAttribute("Qualification", LocalizedXValue(""))
                                        ),
                                        children = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    XNode(
                                        name = "Employee",
                                        attributes = listOf(
                                            LocalizedXAttribute("Name", LocalizedXValue("Peter")),
                                            LocalizedXAttribute("FamilyName", LocalizedXValue("Black")),
                                            LocalizedXAttribute("Ingoing", LocalizedXValue("1998-02-13")),
                                            LocalizedXAttribute("Salary", LocalizedXValue("1600")),
                                            LocalizedXAttribute("Qualification", LocalizedXValue("electrician"))
                                        ),
                                        children = emptyList(),
                                        location = Location.NO_LOCATION
                                    )
                                ),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and("complexDocument".resToString())
                        .ignoreWhitespace()
                        .areSimilar()
                }
                "mixed" {
                    translator.writeDocument(
                        TestXmlXDocument(
                            root = XNode(
                                name = "root",
                                attributes = listOf(LocalizedXAttribute("a", LocalizedXValue("aRoot"))),
                                children = listOf(
                                    XLeaf("XXX", LocalizedXValue("TextContent1"), Location.NO_LOCATION),
                                    XmlXNode(
                                        element = Element("table", "h", "http://www.w3.org/TR/html4/"),
                                        name = "h:table",
                                        attributes = emptyList(),
                                        children = listOf(
                                            XmlXNode(
                                                element = Element("tr", "h", "http://www.w3.org/TR/html4/"),
                                                name = "h:tr",
                                                attributes = emptyList(),
                                                children = listOf(
                                                    XmlXNode(
                                                        element = Element("td", "h", "http://www.w3.org/TR/html4/"),
                                                        name = "h:td",
                                                        attributes = emptyList(),
                                                        children = listOf(
                                                            XLeaf(
                                                                "XXX",
                                                                LocalizedXValue("Apples"),
                                                                Location.NO_LOCATION
                                                            )
                                                        ),
                                                        location = Location.NO_LOCATION
                                                    ),
                                                    XmlXNode(
                                                        element = Element("td", "h", "http://www.w3.org/TR/html4/"),
                                                        name = "h:td",
                                                        attributes = emptyList(),
                                                        children = listOf(
                                                            XLeaf(
                                                                "XXX",
                                                                LocalizedXValue("Bananas"),
                                                                Location.NO_LOCATION
                                                            )
                                                        ),
                                                        location = Location.NO_LOCATION
                                                    )
                                                ),
                                                location = Location.NO_LOCATION
                                            )
                                        ),
                                        location = Location.NO_LOCATION
                                    ),
                                    XLeaf("XXX", LocalizedXValue("TextContent2"), Location.NO_LOCATION),
                                    XmlXNode(
                                        element = Element("table", "f", "https://www.w3schools.com/furniture"),
                                        name = "f:table",
                                        attributes = emptyList(),
                                        children = listOf(
                                            XmlXNode(
                                                element = Element("name", "f", "https://www.w3schools.com/furniture"),
                                                name = "f:name",
                                                attributes = emptyList(),
                                                children = listOf(
                                                    XLeaf(
                                                        "XXX",
                                                        LocalizedXValue("African Coffee Table"),
                                                        Location.NO_LOCATION
                                                    )
                                                ),
                                                location = Location.NO_LOCATION
                                            ),
                                            XmlXNode(
                                                element = Element("width", "f", "https://www.w3schools.com/furniture"),
                                                name = "f:width",
                                                attributes = emptyList(),
                                                children = listOf(
                                                    XLeaf(
                                                        "XXX",
                                                        LocalizedXValue("80"),
                                                        Location.NO_LOCATION
                                                    )
                                                ),
                                                location = Location.NO_LOCATION
                                            ),
                                            XmlXNode(
                                                element = Element("length", "f", "https://www.w3schools.com/furniture"),
                                                name = "f:length",
                                                attributes = emptyList(),
                                                children = listOf(
                                                    XLeaf(
                                                        "XXX",
                                                        LocalizedXValue("120"),
                                                        Location.NO_LOCATION
                                                    )
                                                ),
                                                location = Location.NO_LOCATION
                                            )
                                        ),
                                        location = Location.NO_LOCATION
                                    ),
                                    XLeaf("XXX", LocalizedXValue("TextContent3"), Location.NO_LOCATION)
                                ),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    XmlAssert.assertThat(outputWriter.toString())
                        .and("complexMixedDocument".resToString())
                        .ignoreWhitespace()
                        .areSimilar()
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

    /**
     * Dummy XML document implementation
     * Only for test of writing
     */
    private class TestXmlXDocument(override val root: XTree) : BaseXDocument(SupportedDataType.XML)
}
