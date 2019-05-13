package org.xdef.translator.document

import io.kotlintest.*
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.string.shouldBeEmpty
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.core.Attribute
import org.xdef.core.document.data.XTree
import org.xdef.translator.JSON_ROOT_NODE_NAME
import org.xdef.translator.SupportedDataType
import org.xdef.translator.dataset.json.*
import org.xdef.translator.document.json.JsonDocumentTranslator
import org.xdef.translator.document.json.model.*
import org.xdef.translator.indexedName
import java.io.StringWriter
import java.math.BigDecimal

/**
 * Tests for [JsonDocumentTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class JsonDocumentTranslatorTest : FreeSpec() {

    private val translator = JsonDocumentTranslator()

    private lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    init {
        "read document" - {
            "empty" {
                shouldThrow<IllegalArgumentException> { "".toDocument() }
            }
            "minimal" - {
                "object" {
                    minimalObject.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonObjectXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "array" {
                    minimalArray.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonArrayXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes.shouldBeEmpty()
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "string" {
                    minimalString.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                            leaf.name shouldBe JSON_ROOT_NODE_NAME
                            leaf.value.shouldBeInstanceOf<JsonXString> { string ->
                                string.value.shouldBeEmpty()
                                string.typedValue.shouldBeEmpty()
                            }
                        }
                    }
                }
                "number" {
                    minimalNumber.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                            leaf.name shouldBe JSON_ROOT_NODE_NAME
                            leaf.value.shouldBeInstanceOf<JsonXNumber> { number ->
                                BigDecimal(number.value) shouldBe BigDecimal("-1e15")
                                number.typedValue shouldBe BigDecimal("-1e15")
                            }
                        }
                    }
                }
                "boolean" {
                    minimalBoolean.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                            leaf.name shouldBe JSON_ROOT_NODE_NAME
                            leaf.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
                                boolean.value shouldBe true.toString()
                                boolean.typedValue.shouldBeTrue()
                            }
                        }
                    }
                }
                "null" {
                    minimalUndefined.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                            leaf.name shouldBe JSON_ROOT_NODE_NAME
                            leaf.value.shouldBeNull()
                        }
                    }
                }
            }
            "simple" - {
                "object" {
                    simpleObject.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonObjectXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes shouldHaveSize 4
                            node.attributes should { attributes ->
                                attributes[0] should { attribute ->
                                    attribute.name shouldBe "A"
                                    attribute.value.shouldBeInstanceOf<JsonXString> { string ->
                                        string.value shouldBe "A-value"
                                        string.typedValue shouldBe "A-value"
                                    }
                                }
                                attributes[1] should { attribute ->
                                    attribute.name shouldBe "B"
                                    attribute.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
                                        boolean.value shouldBe false.toString()
                                        boolean.typedValue.shouldBeFalse()
                                    }
                                }
                                attributes[2] should { attribute ->
                                    attribute.name shouldBe "C"
                                    attribute.value.shouldBeInstanceOf<JsonXNumber> { number ->
                                        number.value shouldBe 12345.toString()
                                        number.typedValue.toInt() shouldBe 12345
                                    }
                                }
                                attributes[3] should { attribute ->
                                    attribute.name shouldBe "U"
                                    attribute.value.shouldBeNull()
                                }
                            }
                            node.children.shouldBeEmpty()
                        }
                    }
                }
                "number array" {
                    simpleNumberArray.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonArrayXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 5
                            node.children.forEachIndexed { index, child ->
                                child.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.name shouldBe indexedName(node.name, index)
                                    leaf.value.shouldBeInstanceOf<JsonXNumber> { number ->
                                        number.typedValue.toInt() shouldBe index + 1
                                    }
                                }
                            }
                        }
                    }
                }
                "string array" {
                    simpleStringArray.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonArrayXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 5
                            node.children.forEachIndexed { index, child ->
                                child.shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.name shouldBe indexedName(node.name, index)
                                    leaf.value.shouldBeInstanceOf<JsonXString> { string ->
                                        string.typedValue shouldBe 'A'.plus(index).toString()
                                    }
                                }
                            }
                        }
                    }
                }
                "mixed array" {
                    simpleMixedArray.toDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonArrayXNode> { node ->
                            node.name shouldBe JSON_ROOT_NODE_NAME
                            node.attributes.shouldBeEmpty()
                            node.children shouldHaveSize 7
                            node.children.forEachIndexed { index, child ->
                                child.shouldBeInstanceOf<XTree> { tree ->
                                    tree.name shouldBe indexedName(node.name, index)
                                }
                            }
                            node.children should { children ->
                                children[0].shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.value.shouldBeInstanceOf<JsonXString> { string ->
                                        string.typedValue shouldBe "A"
                                    }
                                }
                                children[1].shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.value.shouldBeInstanceOf<JsonXNumber> { number ->
                                        number.typedValue.toInt() shouldBe 1
                                    }
                                }
                                children[2].shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.value.shouldBeNull()
                                }
                                children[3].shouldBeInstanceOf<JsonArrayXNode> { node ->
                                    node.attributes.shouldBeEmpty()
                                    node.children.shouldBeEmpty()
                                }
                                children[4].shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
                                        boolean.typedValue.shouldBeFalse()
                                    }
                                }
                                children[5].shouldBeInstanceOf<JsonObjectXNode> { node ->
                                    node.attributes.shouldBeEmpty()
                                    node.children.shouldBeEmpty()
                                }
                                children[6].shouldBeInstanceOf<JsonXLeaf> { leaf ->
                                    leaf.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
                                        boolean.typedValue.shouldBeTrue()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "complex" - {
                "homogenous array" {
                    "complexHomogenousArray".resToDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonArrayXNode> { rootNode ->
                            rootNode.name shouldBe JSON_ROOT_NODE_NAME
                            rootNode.attributes.shouldBeEmpty()
                            rootNode.children.forEachIndexed { index, child ->
                                child.shouldBeInstanceOf<JsonObjectXNode> { node ->
                                    node.name shouldBe indexedName(rootNode.name, index)
                                    node.attributes shouldHaveSize 4
                                    node.attributes should { attributes ->
                                        attributes[0] should { attribute ->
                                            attribute.name shouldBe "A"
                                            attribute.value.shouldBeInstanceOf<JsonXString> { string ->
                                                string.value shouldBe "A-value"
                                                string.typedValue shouldBe "A-value"
                                            }
                                        }
                                        attributes[1] should { attribute ->
                                            attribute.name shouldBe "B"
                                            attribute.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
                                                boolean.value shouldBe true.toString()
                                                boolean.typedValue.shouldBeTrue()
                                            }
                                        }
                                        attributes[2] should { attribute ->
                                            attribute.name shouldBe "C"
                                            attribute.value.shouldBeInstanceOf<JsonXNumber> { number ->
                                                number.value shouldBe 12345.toString()
                                                number.typedValue.toInt() shouldBe 12345
                                            }
                                        }
                                        attributes[3] should { attribute ->
                                            attribute.name shouldBe "U"
                                            attribute.value.shouldBeNull()
                                        }
                                    }
                                    node.children.shouldBeEmpty()
                                }
                            }
                        }
                    }
                }
                "mixed" {
                    "complexMixedDocument".resToDocument() should { doc ->
                        doc.type.name shouldBe SupportedDataType.JSON.name
                        doc.shouldBeInstanceOf<JsonXDocument>()
                        doc.root.shouldBeInstanceOf<JsonObjectXNode> { rootNode ->
                            rootNode.name shouldBe JSON_ROOT_NODE_NAME
                            rootNode.attributes shouldHaveSize 14
                            rootNode.attributes should { attributes ->
                                attributes[0].shouldHaveStringValue("_id", "dsfkjlkad516ds")
                                attributes[1].shouldHaveNumberValue("index", 1345)
                                attributes[2].shouldHaveStringValue("guid", "a7354b5e-1547-4d80-857b-074ec78e3868")
                                attributes[3].shouldHaveBooleanValue("isActive", false)
                                attributes[4].shouldHaveStringValue("balance", ",790.49")
                                attributes[5].shouldHaveStringValue("picture", "http://placehold.it/32x32")
                                attributes[6].shouldHaveNumberValue("age", 37)
                                attributes[7].shouldHaveStringValue("eyeColor", "green")
                                attributes[8].shouldHaveStringValue("company", "INDEXIA")
                                attributes[9].shouldHaveStringValue("email", "avila.johnston@indexia.io")
                                attributes[10].shouldHaveStringValue("phone", "+1 (847) 477-2146")
                                attributes[11].shouldHaveStringValue(
                                    "address",
                                    "682 Elton Street, Newkirk, Nebraska, 3946"
                                )
                                attributes[12].shouldHaveStringValue(
                                    "greeting",
                                    "Hello, Avila! You have 5 unread messages."
                                )
                                attributes[13].shouldHaveStringValue("favoriteFruit", "strawberry")
                            }
                            rootNode.children shouldHaveSize 4
                            rootNode.children should { children ->
                                children[0].shouldBeInstanceOf<JsonObjectXNode> { node ->
                                    node.name shouldBe "name"
                                    node.attributes should { attributes ->
                                        attributes[0].shouldHaveStringValue("first", "Avila")
                                        attributes[1].shouldHaveStringValue("last", "Johnston")
                                    }
                                    node.children.shouldBeEmpty()
                                }
                                children[1].shouldBeInstanceOf<JsonArrayXNode> { node ->
                                    node.name shouldBe "tags"
                                    node.attributes.shouldBeEmpty()
                                    node.children shouldHaveSize 5
                                    @Suppress("UNCHECKED_CAST")
                                    (node.children as List<JsonXLeaf>).map { it.value?.value } shouldContainExactly listOf(
                                        "ad",
                                        "duis",
                                        "occaecat",
                                        "incididunt",
                                        "ut"
                                    )
                                }
                                children[2].shouldBeInstanceOf<JsonArrayXNode> { node ->
                                    node.name shouldBe "range"
                                    node.attributes.shouldBeEmpty()
                                    node.children shouldHaveSize 10
                                    @Suppress("UNCHECKED_CAST")
                                    (node.children as List<JsonXLeaf>).map { (it.value as JsonXNumber).typedValue.toInt() } shouldContainExactly IntRange(
                                        0, 9
                                    ).toList()
                                }
                                children[3].shouldBeInstanceOf<JsonArrayXNode> { node ->
                                    node.name shouldBe "friends"
                                    node.attributes.shouldBeEmpty()
                                    node.children shouldHaveSize 1
                                    @Suppress("UNCHECKED_CAST")
                                    node.children[0] as JsonObjectXNode should {
                                        it.attributes shouldHaveSize 2
                                        it.attributes[0].shouldHaveNumberValue("id", 0)
                                        it.attributes[1].shouldHaveStringValue("name", "Davis Montgomery")
                                        it.children.shouldBeEmpty()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        "write document" - {
            "minimal" - {
                "object" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonObjectXNode(
                                name = JSON_ROOT_NODE_NAME,
                                attributes = emptyList(),
                                children = emptyList()
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(minimalObject, outputWriter.toString(), false)
                    }
                }
                "array" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonArrayXNode(
                                name = JSON_ROOT_NODE_NAME,
                                children = emptyList()
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(minimalArray, outputWriter.toString(), false)
                    }
                }
                "string" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonXLeaf(
                                name = JSON_ROOT_NODE_NAME,
                                value = JsonXString("")
                            )
                        ),
                        outputWriter
                    )
                    outputWriter.toString() shouldBe minimalString
                }
                "number" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonXLeaf(
                                name = JSON_ROOT_NODE_NAME,
                                value = JsonXNumber(1548)
                            )
                        ),
                        outputWriter
                    )
                    outputWriter.toString() shouldBe 1548.toString()
                }
                "boolean" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonXLeaf(
                                name = JSON_ROOT_NODE_NAME,
                                value = JsonXBoolean(true)
                            )
                        ),
                        outputWriter
                    )
                    outputWriter.toString() shouldBe true.toString()
                }
                "null" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonXLeaf(
                                name = JSON_ROOT_NODE_NAME,
                                value = null
                            )
                        ),
                        outputWriter
                    )
                    outputWriter.toString() shouldBe null.toString()
                }
            }
            "simple" - {
                "object" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonObjectXNode(
                                name = JSON_ROOT_NODE_NAME,
                                attributes = listOf(
                                    JsonXAttribute(
                                        name = "A",
                                        value = JsonXString("A-value")
                                    ),
                                    JsonXAttribute(
                                        name = "B",
                                        value = JsonXBoolean(false)
                                    ),
                                    JsonXAttribute(
                                        name = "C",
                                        value = JsonXNumber(12345)
                                    ),
                                    JsonXAttribute(
                                        name = "U",
                                        value = null
                                    )
                                ),
                                children = emptyList()
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleObject, outputWriter.toString(), false)
                    }
                }
                "number array" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonArrayXNode(
                                name = JSON_ROOT_NODE_NAME,
                                children = IntRange(1, 5).map {
                                    JsonXLeaf(
                                        name = "", // it is ignored
                                        value = JsonXNumber(it)
                                    )
                                }
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleNumberArray, outputWriter.toString(), false)
                    }
                }
                "string array" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonArrayXNode(
                                name = JSON_ROOT_NODE_NAME,
                                children = CharRange('A', 'E').map {
                                    JsonXLeaf(
                                        name = "", // it is ignored
                                        value = JsonXString(it.toString())
                                    )
                                }
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleStringArray, outputWriter.toString(), false)
                    }
                }
                "mixed array" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonArrayXNode(
                                name = JSON_ROOT_NODE_NAME,
                                children = listOf(
                                    JsonXString("A"),
                                    JsonXNumber(1),
                                    null
                                ).map {
                                    JsonXLeaf(
                                        name = "", // it is ignored
                                        value = it
                                    )
                                } + listOf(
                                    JsonArrayXNode(
                                        name = "",
                                        children = emptyList()
                                    ),
                                    JsonXLeaf(
                                        name = "", // it is ignored
                                        value = JsonXBoolean(false)
                                    ),
                                    JsonObjectXNode(
                                        name = "",
                                        attributes = emptyList(),
                                        children = emptyList()
                                    ),
                                    JsonXLeaf(
                                        name = "", // it is ignored
                                        value = JsonXBoolean(true)
                                    )
                                )
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleMixedArray, outputWriter.toString(), false)
                    }
                }
            }
            "complex" - {
                "homogenous array" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonArrayXNode(
                                name = JSON_ROOT_NODE_NAME,
                                children = (1..4).map {
                                    JsonObjectXNode(
                                        name = JSON_ROOT_NODE_NAME,
                                        attributes = listOf(
                                            JsonXAttribute(
                                                name = "A",
                                                value = JsonXString("A-value")
                                            ),
                                            JsonXAttribute(
                                                name = "B",
                                                value = JsonXBoolean(true)
                                            ),
                                            JsonXAttribute(
                                                name = "C",
                                                value = JsonXNumber(12345)
                                            ),
                                            JsonXAttribute(
                                                name = "U",
                                                value = null
                                            )
                                        ),
                                        children = emptyList()
                                    )
                                }
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals("complexHomogenousArray".resToString(), outputWriter.toString(), false)
                    }
                }
                "mixed" {
                    translator.writeDocument(
                        JsonXDocument(
                            root = JsonObjectXNode(
                                name = JSON_ROOT_NODE_NAME,
                                attributes = listOf(
                                    JsonXAttribute("_id", JsonXString("dsfkjlkad516ds")),
                                    JsonXAttribute("index", JsonXNumber(1345)),
                                    JsonXAttribute("guid", JsonXString("a7354b5e-1547-4d80-857b-074ec78e3868")),
                                    JsonXAttribute("isActive", JsonXBoolean(false)),
                                    JsonXAttribute("balance", JsonXString(",790.49")),
                                    JsonXAttribute("picture", JsonXString("http://placehold.it/32x32")),
                                    JsonXAttribute("age", JsonXNumber(37)),
                                    JsonXAttribute("eyeColor", JsonXString("green")),
                                    JsonXAttribute("company", JsonXString("INDEXIA")),
                                    JsonXAttribute("email", JsonXString("avila.johnston@indexia.io")),
                                    JsonXAttribute("phone", JsonXString("+1 (847) 477-2146")),
                                    JsonXAttribute("address", JsonXString("682 Elton Street, Newkirk, Nebraska, 3946")),
                                    JsonXAttribute(
                                        "greeting",
                                        JsonXString("Hello, Avila! You have 5 unread messages.")
                                    ),
                                    JsonXAttribute("favoriteFruit", JsonXString("strawberry"))
                                ),
                                children = listOf(
                                    JsonObjectXNode(
                                        name = "name",
                                        attributes = listOf(
                                            JsonXAttribute("first", JsonXString("Avila")),
                                            JsonXAttribute("last", JsonXString("Johnston"))
                                        ),
                                        children = emptyList()
                                    ),
                                    JsonArrayXNode(
                                        name = "tags",
                                        children = listOf(
                                            "ad",
                                            "duis",
                                            "occaecat",
                                            "incididunt",
                                            "ut"
                                        ).map { JsonXLeaf("", JsonXString(it)) }
                                    ),
                                    JsonArrayXNode(
                                        name = "range",
                                        children = (0..9).map {
                                            JsonXLeaf(
                                                name = "",
                                                value = JsonXNumber(it)
                                            )
                                        }
                                    ),
                                    JsonArrayXNode(
                                        name = "friends",
                                        children = listOf(
                                            JsonObjectXNode(
                                                name = "", // it is ignored
                                                attributes = listOf(
                                                    JsonXAttribute("id", JsonXNumber(0)),
                                                    JsonXAttribute("name", JsonXString("Davis Montgomery"))
                                                ),
                                                children = emptyList()
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals("complexMixedDocument".resToString(), outputWriter.toString(), false)
                    }
                }
            }
        }
    }

    private fun String.toDocument() = translator.readDocument(autoClose(byteInputStream()))

    private fun String.resToDocument() = translator.readDocument(
        autoClose(
            JsonDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this.json")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            JsonDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this.json").reader()
        )
    )
}

private fun <V> Attribute<V>.shouldHaveStringValue(name: String, value: String) = should { attribute ->
    attribute.name shouldBe name
    attribute.shouldBeInstanceOf<JsonXAttribute> { jsonAttribute ->
        jsonAttribute.value.shouldBeInstanceOf<JsonXString> { string ->
            string.value shouldBe value
        }
    }
}

private fun <V> Attribute<V>.shouldHaveNumberValue(name: String, value: Number) = should { attribute ->
    attribute.name shouldBe name
    attribute.shouldBeInstanceOf<JsonXAttribute> { jsonAttribute ->
        jsonAttribute.value.shouldBeInstanceOf<JsonXNumber> { number ->
            number.value shouldBe value.toString()
            BigDecimal(number.value) shouldBe BigDecimal(value.toString())
        }
    }
}

private fun <V> Attribute<V>.shouldHaveBooleanValue(name: String, value: Boolean) = should { attribute ->
    attribute.name shouldBe name
    attribute.shouldBeInstanceOf<JsonXAttribute> { jsonAttribute ->
        jsonAttribute.value.shouldBeInstanceOf<JsonXBoolean> { boolean ->
            boolean.value shouldBe value.toString()
            boolean.typedValue shouldBe value
        }
    }
}
