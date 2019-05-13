package org.xdef.translator.definition

import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.core.Location
import org.xdef.core.document.data.XValue
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.translator.SupportedDataType
import org.xdef.translator.dataset.json.*
import org.xdef.translator.definition.json.JsonDefinitionTranslator
import org.xdef.translator.definition.json.JsonDefinitionTranslator.Companion.ROOT_NODE_NAME
import org.xdef.translator.definition.json.model.JsonArrayXDefNode
import org.xdef.translator.definition.json.model.JsonObjectXDefNode
import org.xdef.translator.definition.json.model.JsonXDefDocument
import org.xdef.translator.definition.json.model.JsonXDefLeaf
import org.xdef.translator.document.json.model.JsonXBoolean
import org.xdef.translator.document.json.model.JsonXNumber
import org.xdef.translator.document.json.model.JsonXString
import java.io.StringWriter

/**
 * Tests for [JsonDefinitionTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class JsonDefinitionTranslatorTest : FreeSpec() {

    private val translator = JsonDefinitionTranslator()

    private lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    init {
        "read definition" - {
            "minimal" - {
                "object" {
                    minimalDefObject.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonObjectXDefNode> { objectNode ->
                            objectNode.script shouldBe "required"
                            objectNode.attributes.shouldBeEmpty()
                            objectNode.children.shouldBeEmpty()
                        }
                    }
                }
                "array" {
                    minimalDefArray.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonArrayXDefNode> { arrayNode ->
                            arrayNode.children shouldHaveSize 1
                            arrayNode.children[0].shouldBeInstanceOf<JsonXDefLeaf> { leaf ->
                                leaf.script shouldBe "optional; onTrue xyz"
                                leaf.value?.value shouldBe "optional; onTrue xyz"
                            }
                        }
                    }
                }
                "value" {
                    minimalDefValue.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonXDefLeaf> { leaf ->
                            leaf.script shouldBe "optional; onTrue xyz"
                            leaf.value?.value shouldBe "optional; onTrue xyz"
                        }
                    }
                }
            }
            "simple" - {
                "object" {
                    simpleDefObject.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonObjectXDefNode> { objectNode ->
                            objectNode.script shouldBe "1..*"
                            objectNode.attributes shouldHaveSize 2
                            objectNode.children.shouldBeEmpty()
                        }
                    }
                }
                "array" {
                    simpleDefArray.toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonArrayXDefNode> { arrayNode ->
                            arrayNode.children shouldHaveSize 4
                            arrayNode.children[0].shouldBeInstanceOf<JsonXDefLeaf> { it.script shouldBe "required" }
                            arrayNode.children[1].shouldBeInstanceOf<JsonXDefLeaf> { it.script shouldBe "1" }
                            arrayNode.children[2].shouldBeInstanceOf<JsonXDefLeaf> { it.script.shouldBeNull() }
                            arrayNode.children[3].shouldBeInstanceOf<JsonXDefLeaf> { it.script shouldBe "true" }
                        }
                    }
                }
            }
            "complex" - {
                "mixed" {
                    "complexMixedDefinition".resToDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonObjectXDefNode> { objectNode ->
                            objectNode.name shouldBe ROOT_NODE_NAME
                            objectNode.script shouldBe "0..200"
                            objectNode.attributes shouldHaveSize 13
                            objectNode.attributes
                            objectNode.children shouldHaveSize 3
                            objectNode.children[0].shouldBeInstanceOf<JsonObjectXDefNode> { node ->
                                node.name shouldBe "name"
                                node.script shouldBe "required"
                                node.attributes shouldHaveSize 2
                                node.attributes.forEach {
                                    it.shouldBeInstanceOf<XDefAttribute> { leaf ->
                                        leaf.script shouldBe "string(20)"
                                    }
                                }
                                node.children.shouldBeEmpty()
                            }
                            objectNode.children[1].shouldBeInstanceOf<JsonArrayXDefNode> { node ->
                                node.name shouldBe "tags"
                                node.attributes.shouldBeEmpty()
                                node.children shouldHaveSize 1
                                node.children[0].shouldBeInstanceOf<JsonXDefLeaf> { leaf ->
                                    leaf.script shouldBe "1..* string(10, 20) onAbsence XYZ"
                                }
                            }
                            objectNode.children[2].shouldBeInstanceOf<JsonArrayXDefNode> { node ->
                                node.name shouldBe "friends"
                                node.attributes.shouldBeEmpty()
                                node.children shouldHaveSize 1
                                node.children[0].shouldBeInstanceOf<JsonObjectXDefNode> { obj ->
                                    obj.script shouldBe "1..*"
                                    obj.attributes shouldHaveSize 2
                                }
                            }
                        }
                    }
                }
            }
        }
        "write definition" - {
            "minimal" - {
                "object" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonObjectXDefNode(
                                name = ROOT_NODE_NAME,
                                script = "required",
                                attributes = emptyList(),
                                children = emptyList(),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(minimalDefObject, outputWriter.toString(), false)
                    }
                }
                "array" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonArrayXDefNode(
                                name = ROOT_NODE_NAME,
                                script = null,
                                children = listOf(
                                    JsonXDefLeaf(
                                        name = "XXX",
                                        value = JsonXString("optional; onTrue xyz"),
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
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(minimalDefArray, outputWriter.toString(), false)
                    }
                }
                "value" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonXDefLeaf(
                                name = ROOT_NODE_NAME,
                                value = JsonXString("optional; onTrue xyz"),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(minimalDefValue, outputWriter.toString(), false)
                    }
                }
            }
            "simple" - {
                "object" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonObjectXDefNode(
                                name = ROOT_NODE_NAME,
                                script = "1..*",
                                attributes = listOf(
                                    TestXDefAttribute("a", JsonXNumber(1)),
                                    TestXDefAttribute("b", JsonXString("c"))
                                ),
                                children = emptyList(),
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleDefObject, outputWriter.toString(), false)
                    }
                }
                "array" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonArrayXDefNode(
                                name = ROOT_NODE_NAME,
                                script = null,
                                children = listOf(
                                    JsonXDefLeaf(
                                        name = "XXX",
                                        value = JsonXString("required"),
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    JsonXDefLeaf(
                                        name = "XXX",
                                        value = JsonXNumber(1),
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    JsonXDefLeaf(
                                        name = "XXX",
                                        value = null,
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    JsonXDefLeaf(
                                        name = "XXX",
                                        value = JsonXBoolean(true),
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
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleDefArray, outputWriter.toString(), false)
                    }
                }
            }
            "complex" - {
                "mixed" {
                    translator.writeDefinition(
                        JsonXDefDocument(
                            root = JsonObjectXDefNode(
                                name = ROOT_NODE_NAME,
                                script = "0..200",
                                attributes = listOf(
                                    TestXDefAttribute("_id", JsonXString("required string(10)")),
                                    TestXDefAttribute("index", JsonXString("optional int()")),
                                    TestXDefAttribute("guid", JsonXString("string(20)")),
                                    TestXDefAttribute("isActive", JsonXBoolean(false)),
                                    TestXDefAttribute("balance", JsonXString(",790.49")),
                                    TestXDefAttribute("picture", JsonXString("required url() onTrue println(\"Yes\")")),
                                    TestXDefAttribute("age", JsonXNumber(37)),
                                    TestXDefAttribute("eyeColor", JsonXString("green")),
                                    TestXDefAttribute("email", JsonXString("1..1 email()")),
                                    TestXDefAttribute("phone", JsonXString("required telNumber()")),
                                    TestXDefAttribute("about", JsonXString("string()")),
                                    TestXDefAttribute("latitude", JsonXString("optional latitude()")),
                                    TestXDefAttribute("longitude", JsonXString("optional longitude()"))
                                ),
                                children = listOf(
                                    JsonObjectXDefNode(
                                        name = "name",
                                        script = "required",
                                        attributes = listOf(
                                            TestXDefAttribute("first", JsonXString("string(20)")),
                                            TestXDefAttribute("last", JsonXString("string(20)"))
                                        ),
                                        children = emptyList(),
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    JsonArrayXDefNode(
                                        name = "tags",
                                        script = null,
                                        children = listOf(
                                            JsonXDefLeaf(
                                                name = "XXX",
                                                value = JsonXString("1..* string(10, 20) onAbsence XYZ"),
                                                allowedOccurrences = emptyList(),
                                                allowedEvents = emptyList(),
                                                location = Location.NO_LOCATION
                                            )
                                        ),
                                        allowedOccurrences = emptyList(),
                                        allowedEvents = emptyList(),
                                        location = Location.NO_LOCATION
                                    ),
                                    JsonArrayXDefNode(
                                        name = "friends",
                                        script = null,
                                        children = listOf(
                                            JsonObjectXDefNode(
                                                name = "XXX",
                                                script = "1..*",
                                                attributes = listOf(
                                                    TestXDefAttribute("id", JsonXNumber(0)),
                                                    TestXDefAttribute("name", JsonXString("Davis Montgomery"))
                                                ),
                                                children = emptyList(),
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
                                allowedOccurrences = emptyList(),
                                allowedEvents = emptyList(),
                                location = Location.NO_LOCATION
                            )
                        ),
                        outputWriter
                    )
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals("complexMixedDefinition".resToString(), outputWriter.toString(), false)
                    }
                }
            }
        }
    }

    private fun String.toDefinition() = translator.readDefinition(autoClose(byteInputStream()))

    private fun String.resToDefinition() =
        translator.readDefinition(autoClose(JsonDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.jdef")))

    private fun String.resToString() =
        IOUtils.toString(autoClose(JsonDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.jdef").reader()))

    private class TestXDefAttribute(name: String, value: XValue?) : XDefAttribute(name, value, emptyList(), emptyList())
}
