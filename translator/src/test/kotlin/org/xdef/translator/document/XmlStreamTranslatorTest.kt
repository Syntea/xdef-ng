package org.xdef.translator.document

import com.sun.xml.internal.stream.events.CharacterEvent
import com.sun.xml.internal.stream.events.EndElementEvent
import com.sun.xml.internal.stream.events.StartElementEvent
import io.kotlintest.TestCase
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.string.shouldBeBlank
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import org.apache.commons.io.IOUtils
import org.xdef.core.document.data.LocalizedXAttribute
import org.xdef.core.document.data.LocalizedXValue
import org.xdef.core.document.stream.AttributeEvent
import org.xdef.core.document.stream.EndDocumentEvent
import org.xdef.core.document.stream.StartDocumentEvent
import org.xdef.translator.dataset.xml.*
import org.xdef.translator.document.xml.XmlDocumentTranslator
import org.xdef.translator.document.xml.stream.*
import org.xmlunit.assertj.XmlAssert
import java.io.StringWriter
import javax.xml.stream.XMLStreamException

/**
 * Tests for [XmlDocumentTranslator]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class XmlStreamTranslatorTest : FreeSpec() {

    private val translator = XmlDocumentTranslator()

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
                    shouldThrow<XMLStreamException> { reader.next() }
                }
            }
            "minimal" - {
                "empty element" {
                    minimalElement.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "text" {
                    minimalText.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "T" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlValueEvent> {
                            it.value.shouldNotBeNull()
                            it.value!!.value shouldBe "minimalElement text"
                        }
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "T" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "element and comment" {
                    minimalEmptyAndComment.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlNotProcessedEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "element with comment" {
                    minimalEmptyWithComment.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlNotProcessedEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "A" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
            }
            "simple" - {
                "element with attributes" {
                    simpleElement.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        ('a'..'c').forEach {
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attr ->
                                attr.attribute.name shouldBe it.toString()
                                attr.attribute.value.shouldNotBeNull()
                                attr.attribute.value!!.value shouldBe it.toString()
                            }
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "element with comments" {
                    simpleElementWithComment.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlNotProcessedEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        ('a'..'c').forEach {
                            reader.next().shouldBeInstanceOf<AttributeEvent> { attr ->
                                attr.attribute.name shouldBe it.toString()
                                attr.attribute.value.shouldNotBeNull()
                                attr.attribute.value!!.value shouldBe it.toString()
                            }
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<XmlNotProcessedEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlNotProcessedEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
                "with namespaces" {
                    simpleWithNamespace.toDocumentReader() should { reader ->
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlValueEvent> {
                            it.value.shouldNotBeNull()
                            it.value!!.value.shouldBeBlank()
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "{http://www.w3.org/TR/html4/}table" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "{http://www.w3.org/TR/html4/}tr" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "{http://www.w3.org/TR/html4/}tr" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "{http://www.w3.org/TR/html4/}table" }
                        reader.hasNext().shouldBeTrue()

                        reader.next().shouldBeInstanceOf<XmlValueEvent> {
                            it.value.shouldNotBeNull()
                            it.value!!.value.shouldBeBlank()
                        }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "{https://www.w3schools.com/furniture}table" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "{https://www.w3schools.com/furniture}name" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "{https://www.w3schools.com/furniture}name" }
                        reader.hasNext().shouldBeTrue()
                        reader.next()
                            .shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "{https://www.w3schools.com/furniture}table" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<XmlValueEvent> {
                            it.value.shouldNotBeNull()
                            it.value!!.value.shouldBeBlank()
                        }
                        reader.hasNext().shouldBeTrue()

                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "root" }
                        reader.hasNext().shouldBeTrue()
                        reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                        reader.hasNext().shouldBeFalse()
                    }
                }
            }
            "complex" {
                "complexDocument".resToReader() should { reader ->
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<StartXmlDocumentEvent>()
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "List" }
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<XmlValueEvent> { it.value!!.value.shouldBeBlank() }
                    reader.hasNext().shouldBeTrue()
                    repeat(3) {
                        reader.next().shouldBeInstanceOf<StartElementNodeEvent> { it.name shouldBe "Employee" }
                        reader.hasNext().shouldBeTrue()
                        // Not keep attributes order
                        repeat(5) {
                            reader.next().shouldBeInstanceOf<AttributeEvent>()
                            reader.hasNext().shouldBeTrue()
                        }
                        reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "Employee" }
                        reader.hasNext().shouldBeTrue()

                        reader.next().shouldBeInstanceOf<XmlValueEvent> { it.value!!.value.shouldBeBlank() }
                        reader.hasNext().shouldBeTrue()
                    }
                    reader.next().shouldBeInstanceOf<EndElementNodeEvent> { it.name shouldBe "List" }
                    reader.hasNext().shouldBeTrue()
                    reader.next().shouldBeInstanceOf<EndDocumentEvent>()
                    reader.hasNext().shouldBeFalse()
                }
            }
        }
        "write document" - {
            "minimal" - {
                "empty element" {
                    createWriter().apply {
                        writeEvent(StartXmlDocumentEvent(com.sun.xml.internal.stream.events.StartDocumentEvent()))
                        writeEvent(StartElementNodeEvent(StartElementEvent("", "", "A")))
                        writeEvent(EndElementNodeEvent(EndElementEvent("", "", "A")))
                        writeEvent(EndDocumentEvent())
                    }
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalElement)
                        .areSimilar()
                }
                "with text" {
                    createWriter().apply {
                        writeEvent(StartXmlDocumentEvent(com.sun.xml.internal.stream.events.StartDocumentEvent()))
                        writeEvent(StartElementNodeEvent(StartElementEvent("", "", "T")))
                        writeEvent(XmlValueEvent(CharacterEvent("minimalElement text")))
                        writeEvent(EndElementNodeEvent(EndElementEvent("", "", "T")))
                        writeEvent(EndDocumentEvent())
                    }
                    XmlAssert.assertThat(outputWriter.toString())
                        .and(minimalText)
                        .areSimilar()
                }
            }
            "simple" {
                createWriter().apply {
                    writeEvent(StartXmlDocumentEvent(com.sun.xml.internal.stream.events.StartDocumentEvent()))
                    writeEvent(StartElementNodeEvent(StartElementEvent("", "", "root")))
                    writeEvent(AttributeEvent(LocalizedXAttribute("a", LocalizedXValue("a"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("b", LocalizedXValue("b"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("c", LocalizedXValue("c"))))
                    writeEvent(EndElementNodeEvent(EndElementEvent("", "", "root")))
                    writeEvent(EndDocumentEvent())
                }
                XmlAssert.assertThat(outputWriter.toString())
                    .and(simpleElement)
                    .areSimilar()
            }
            "complex" {
                createWriter().apply {
                    writeEvent(StartXmlDocumentEvent(com.sun.xml.internal.stream.events.StartDocumentEvent()))
                    writeEvent(StartElementNodeEvent(StartElementEvent("", "", "List")))

                    writeEvent(StartElementNodeEvent(StartElementEvent("", "", "Employee")))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Name", LocalizedXValue("John"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("FamilyName", LocalizedXValue("Braun"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Ingoing", LocalizedXValue("2004-10-01"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Salary", LocalizedXValue("2000"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Qualification", LocalizedXValue("worker"))))
                    writeEvent(EndElementNodeEvent(EndElementEvent("", "", "Employee")))

                    writeEvent(StartElementNodeEvent(StartElementEvent("", "", "Employee")))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Name", LocalizedXValue("Mary"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("FamilyName", LocalizedXValue("White"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Ingoing", LocalizedXValue("1998-19-13"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Salary", LocalizedXValue("abcd"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Qualification", LocalizedXValue(""))))
                    writeEvent(EndElementNodeEvent(EndElementEvent("", "", "Employee")))

                    writeEvent(StartElementNodeEvent(StartElementEvent("", "", "Employee")))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Name", LocalizedXValue("Peter"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("FamilyName", LocalizedXValue("Black"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Ingoing", LocalizedXValue("1998-02-13"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Salary", LocalizedXValue("1600"))))
                    writeEvent(AttributeEvent(LocalizedXAttribute("Qualification", LocalizedXValue("electrician"))))
                    writeEvent(EndElementNodeEvent(EndElementEvent("", "", "Employee")))

                    writeEvent(EndElementNodeEvent(EndElementEvent("", "", "List")))
                    writeEvent(EndDocumentEvent())
                }
                XmlAssert.assertThat(outputWriter.toString())
                    .and("complexDocument".resToString())
                    .ignoreWhitespace()
                    .areSimilar()
            }
        }
    }

    private fun String.toDocumentReader() = autoClose(translator.createTranslationReader(byteInputStream()))

    private fun createWriter() = autoClose(translator.createTranslationWriter(outputWriter))

    private fun String.resToReader() = autoClose(
        translator.createTranslationReader(
            XmlStreamTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xml")
        )
    )

    private fun String.resToString() = IOUtils.toString(
        autoClose(
            XmlStreamTranslatorTest::class.java.getResourceAsStream("/dataset/$this.xml").reader()
        )
    )
}
