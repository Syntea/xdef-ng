package org.xdef.translator.definition

import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.translator.SupportedDataType
import org.xdef.translator.definition.json.JsonDefinitionTranslator
import org.xdef.translator.definition.json.JsonDefinitionTranslator.Companion.ROOT_NODE_NAME
import org.xdef.translator.definition.json.model.JsonArrayXDefNode
import org.xdef.translator.definition.json.model.JsonObjectXDefNode
import org.xdef.translator.definition.json.model.JsonXDefLeaf

/**
 * Tests for [JsonDefinitionTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class JsonDefinitionTranslatorTest : FreeSpec() {

    private val translator = JsonDefinitionTranslator()

    init {
        "read definition" - {
            "minimal" - {
                "object" {
                    """{"xd:script": "required"}""".toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonObjectXDefNode> { objectNode ->
                            objectNode.script shouldBe "required"
                            objectNode.attributes.shouldBeEmpty()
                            objectNode.children.shouldBeEmpty()
                        }
                    }
                }
                "array" {
                    """["optional; onTrue xyz"]""".toDefinition() should { def ->
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
                    """"optional; onTrue xyz"""".toDefinition() should { def ->
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
                    """{"xd:script": "1..*", "a": 1, "b": "c"}""".toDefinition() should { def ->
                        def.type.name shouldBe SupportedDataType.JSON.name
                        def.root.shouldBeInstanceOf<JsonObjectXDefNode> { objectNode ->
                            objectNode.script shouldBe "1..*"
                            objectNode.attributes shouldHaveSize 2
                            objectNode.children.shouldBeEmpty()
                        }
                    }
                }
                "array" {
                    """["required", 1, null, true]""".toDefinition() should { def ->
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

        }
    }

    private fun String.toDefinition() = translator.readDefinition(autoClose(byteInputStream()))


    private fun String.resToDefinition() = translator.readDefinition(
        autoClose(
            JsonDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.jdef")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            JsonDefinitionTranslatorTest::class.java.getResourceAsStream("/dataset/$this.jdef").reader()
        )
    )
}
