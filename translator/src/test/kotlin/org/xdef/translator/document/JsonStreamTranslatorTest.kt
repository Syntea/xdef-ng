package org.xdef.translator.document

import io.kotlintest.TestCase
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.string.shouldBeEmpty
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.skyscreamer.jsonassert.JSONAssert
import org.xdef.core.document.stream.AttributeEvent
import org.xdef.core.document.stream.EndDocumentEvent
import org.xdef.core.document.stream.StartDocumentEvent
import org.xdef.core.document.stream.ValueEvent
import org.xdef.translator.dataset.json.*
import org.xdef.translator.document.json.JsonDocumentTranslator
import org.xdef.translator.document.json.model.JsonXAttribute
import org.xdef.translator.document.json.model.JsonXBoolean
import org.xdef.translator.document.json.model.JsonXNumber
import org.xdef.translator.document.json.model.JsonXString
import org.xdef.translator.document.json.stream.*
import java.io.StringWriter
import java.math.BigDecimal

/**
 * Tests for [JsonXReader] and [JsonXWriter]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class JsonStreamTranslatorTest : FreeSpec() {

    private val translator = JsonDocumentTranslator()

    private lateinit var outputWriter: StringWriter

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        outputWriter = StringWriter()
    }

    init {
        "read document" - {
            "empty" {
                "".toDocumentReader() should { reader ->
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                    reader.hasNext().shouldBeFalse()
                }
            }
            "minimal" - {
                "object" {
                    minimalObject.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "array" {
                    minimalArray.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "string" {
                    minimalString.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                            valueEvent.value.shouldBeInstanceOf<JsonXString>()
                            valueEvent.value?.value.shouldBeEmpty()
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "number" {
                    minimalNumber.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                            valueEvent.value.shouldBeInstanceOf<JsonXNumber>()
                            BigDecimal(valueEvent.value?.value) shouldBe BigDecimal(minimalNumber)
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "boolean" {
                    minimalBoolean.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                            valueEvent.value.shouldBeInstanceOf<JsonXBoolean> {
                                it.typedValue shouldBe true
                            }
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "null" {
                    minimalUndefined.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                            valueEvent.value.shouldBeNull()
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
            }
            "simple" - {
                "object" {
                    simpleObject.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                            attributeEvent.attribute.name shouldBe "A"
                            attributeEvent.attribute.value?.value shouldBe "A-value"
                        }
                        reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                            attributeEvent.attribute.name shouldBe "B"
                            attributeEvent.attribute.value?.value shouldBe false.toString()
                        }
                        reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                            attributeEvent.attribute.name shouldBe "C"
                            attributeEvent.attribute.value?.value shouldBe 12345.toString()
                        }
                        reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                            attributeEvent.attribute.name shouldBe "U"
                            attributeEvent.attribute.value.shouldBeNull()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "number array" {
                    simpleNumberArray.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        (1..5).forEach {
                            reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                                valueEvent.value?.value shouldBe it.toString()
                            }
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "string array" {
                    simpleStringArray.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        ('A'..'E').forEach {
                            reader.next().shouldBeInstanceOf<ValueEvent> { valueEvent ->
                                valueEvent.value?.value shouldBe it.toString()
                            }
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "mixed array" {
                    simpleMixedArray.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { it.value?.value shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { it.value?.value shouldBe "1" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { it.value.shouldBeNull() }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { it.value?.value shouldBe false.toString() }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<ValueEvent> { it.value?.value shouldBe true.toString() }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
            }
            "complex" - {
                "homogenous array" {
                    "complexHomogenousArray".resToReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        (1..4).forEach { _ ->
                            reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                            reader.hasNext().shouldBeTrue()
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                                attributeEvent.attribute.name shouldBe "A"
                                attributeEvent.attribute.value?.value shouldBe "A-value"
                            }
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                                attributeEvent.attribute.name shouldBe "B"
                                attributeEvent.attribute.value?.value shouldBe true.toString()
                            }
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                                attributeEvent.attribute.name shouldBe "C"
                                attributeEvent.attribute.value?.value shouldBe 12345.toString()
                            }
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attributeEvent ->
                                attributeEvent.attribute.name shouldBe "U"
                                attributeEvent.attribute.value.shouldBeNull()
                            }
                            reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "mixed" {
                    "complexMixedDocument".resToReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        repeat(8) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent> { it.name shouldBe "name" }
                        reader.hasNext().shouldBeTrue()
                        repeat(2) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent> { it.name shouldBe "name" }
                        reader.hasNext().shouldBeTrue()
                        repeat(4) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent> { it.name shouldBe "tags" }
                        reader.hasNext().shouldBeTrue()
                        repeat(5) {
                            reader.next().shouldBeInstanceOf<ValueEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent> { it.name shouldBe "tags" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent> { it.name shouldBe "range" }
                        reader.hasNext().shouldBeTrue()
                        repeat(10) {
                            reader.next().shouldBeInstanceOf<ValueEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent> { it.name shouldBe "range" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonArrayNodeEvent> { it.name shouldBe "friends" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        repeat(2) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonArrayNodeEvent> { it.name shouldBe "friends" }
                        repeat(2) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndJsonObjectNodeEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
            }
        }
        "write document" - {
            "empty" {
                createWriter().apply {
                    writeEvent(StartDocumentEvent())
                    writeEvent(EndDocumentEvent())
                }
                outputWriter.toString().shouldBeEmpty()
            }
            "minimal" - {
                "object" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonObjectNodeEvent("xxx"))
                        writeEvent(EndJsonObjectNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe minimalObject
                }
                "array" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonArrayNodeEvent(""))
                        writeEvent(EndJsonArrayNodeEvent(""))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe minimalArray
                }
                "string" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(ValueEvent(JsonXString("")))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe minimalString
                }
                "number" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(ValueEvent(JsonXNumber(12345)))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe 12345.toString()
                }
                "boolean" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(ValueEvent(JsonXBoolean(false)))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe false.toString()
                }
                "null" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(ValueEvent(null))
                        writeEvent(EndDocumentEvent())
                    }
                    outputWriter.toString() shouldBe null.toString()
                }
            }
            "simple" - {
                "object" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonObjectNodeEvent("xxx"))
                        writeEvent(AttributeEvent(JsonXAttribute("A", JsonXString("A-value"))))
                        writeEvent(AttributeEvent(JsonXAttribute("B", JsonXBoolean(false))))
                        writeEvent(AttributeEvent(JsonXAttribute("C", JsonXNumber(12345))))
                        writeEvent(AttributeEvent(JsonXAttribute("U", null)))
                        writeEvent(EndJsonObjectNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleObject, outputWriter.toString(), false)
                    }
                }
                "number array" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonArrayNodeEvent("xxx"))
                        (1..5).forEach {
                            writeEvent(ValueEvent(JsonXNumber(it)))
                        }
                        writeEvent(EndJsonArrayNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleNumberArray, outputWriter.toString(), false)
                    }
                }
                "string array" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonArrayNodeEvent("xxx"))
                        ('A'..'E').forEach {
                            writeEvent(ValueEvent(JsonXString(it.toString())))
                        }
                        writeEvent(EndJsonArrayNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleStringArray, outputWriter.toString(), false)
                    }
                }
                "mixed array" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonArrayNodeEvent("xxx"))
                        writeEvent(ValueEvent(JsonXString("A")))
                        writeEvent(ValueEvent(JsonXNumber(1)))
                        writeEvent(ValueEvent(null))
                        writeEvent(StartJsonArrayNodeEvent("x"))
                        writeEvent(EndJsonArrayNodeEvent("x"))
                        writeEvent(ValueEvent(JsonXBoolean(false)))
                        writeEvent(StartJsonObjectNodeEvent("x"))
                        writeEvent(EndJsonObjectNodeEvent("x"))
                        writeEvent(ValueEvent(JsonXBoolean(true)))
                        writeEvent(EndJsonArrayNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals(simpleMixedArray, outputWriter.toString(), false)
                    }
                }
            }
            "complex" - {
                "homogenous array" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonArrayNodeEvent("xxx"))
                        repeat(4) {
                            writeEvent(StartJsonObjectNodeEvent("_"))
                            writeEvent(AttributeEvent(JsonXAttribute("A", JsonXString("A-value"))))
                            writeEvent(AttributeEvent(JsonXAttribute("B", JsonXBoolean(true))))
                            writeEvent(AttributeEvent(JsonXAttribute("C", JsonXNumber(12345))))
                            writeEvent(AttributeEvent(JsonXAttribute("U", null)))
                            writeEvent(EndJsonObjectNodeEvent("_"))
                        }
                        writeEvent(EndJsonArrayNodeEvent("xxx"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals("complexHomogenousArray".resToString(), outputWriter.toString(), false)
                    }
                }
                "mixed" {
                    createWriter().apply {
                        writeEvent(StartDocumentEvent())
                        writeEvent(StartJsonObjectNodeEvent("_"))
                        listOf(
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
                            JsonXAttribute("greeting", JsonXString("Hello, Avila! You have 5 unread messages.")),
                            JsonXAttribute("favoriteFruit", JsonXString("strawberry"))
                        ).forEach {
                            writeEvent(AttributeEvent(it))
                        }
                        writeEvent(StartJsonObjectNodeEvent("name"))
                        writeEvent(AttributeEvent(JsonXAttribute("first", JsonXString("Avila"))))
                        writeEvent(AttributeEvent(JsonXAttribute("last", JsonXString("Johnston"))))
                        writeEvent(EndJsonObjectNodeEvent("name"))
                        writeEvent(StartJsonArrayNodeEvent("tags"))
                        listOf(
                            "ad",
                            "duis",
                            "occaecat",
                            "incididunt",
                            "ut"
                        ).forEach {
                            writeEvent(ValueEvent(JsonXString(it)))
                        }
                        writeEvent(EndJsonArrayNodeEvent("tags"))
                        writeEvent(StartJsonArrayNodeEvent("range"))
                        (0..9).forEach { writeEvent(ValueEvent(JsonXNumber(it))) }
                        writeEvent(EndJsonArrayNodeEvent("range"))
                        writeEvent(StartJsonArrayNodeEvent("friends"))
                        writeEvent(StartJsonObjectNodeEvent("_"))
                        writeEvent(AttributeEvent(JsonXAttribute("id", JsonXNumber(0))))
                        writeEvent(AttributeEvent(JsonXAttribute("name", JsonXString("Davis Montgomery"))))
                        writeEvent(EndJsonObjectNodeEvent("_"))
                        writeEvent(EndJsonArrayNodeEvent("friends"))
                        writeEvent(EndJsonObjectNodeEvent("_"))
                        writeEvent(EndDocumentEvent())
                    }
                    shouldNotThrow<AssertionError> {
                        JSONAssert.assertEquals("complexMixedDocument".resToString(), outputWriter.toString(), false)
                    }
                }
            }
        }
    }

    private fun String.toDocumentReader() = autoClose(translator.createTranslationReader(byteInputStream()))

    private fun createWriter() = autoClose(translator.createTranslationWriter(outputWriter))

    private fun String.resToReader() = autoClose(
        translator.createTranslationReader(
            JsonDocumentTranslatorTest::class.java.getResourceAsStream("/dataset/$this.json")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            JsonStreamTranslatorTest::class.java.getResourceAsStream("/dataset/$this.json").reader()
        )
    )
}
