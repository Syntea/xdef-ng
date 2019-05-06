package org.xdef.translator.document.xml

import org.jdom2.*
import org.jdom2.input.SAXBuilder
import org.jdom2.located.LocatedJDOMFactory
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import org.xdef.core.document.data.XDocument
import org.xdef.core.document.data.XLeaf
import org.xdef.core.document.data.XNode
import org.xdef.core.document.data.XTree
import org.xdef.translator.document.DocumentTranslator
import org.xdef.translator.document.xml.model.XmlXDocument
import org.xdef.translator.document.xml.model.XmlXNode
import org.xdef.translator.document.xml.stream.XmlXReader
import org.xdef.translator.document.xml.stream.XmlXWriter
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlDocumentTranslator : DocumentTranslator<Element> {

    companion object Constants {
        const val ROOT_ELEMENT_NAME = "org.xdef.root"

        // TODO not all illegal characters
        private val WS_REGEX = "\\s".toRegex()

        private fun String.normalizeName() = split(WS_REGEX)
            .joinToString(separator = "_")
            .ifEmpty { "__WS__" }
    }

    private val reader = SAXBuilder().apply { jdomFactory = LocatedJDOMFactory() }
    private val writer = XMLOutputter().apply { format = Format.getPrettyFormat() }

    override fun translate(dom: Element) = translate(Document(dom))

    override fun translate(document: XDocument) = translate(document.root)

    override fun translate(documentTree: XTree): Element {
        return when (val content = xTree2Content(documentTree)) {
            is Element -> content
            else -> Element(ROOT_ELEMENT_NAME).addContent(content)
        }
    }

    override fun readDocument(input: InputStream) = translate(reader.build(input))

    override fun readDocument(input: Reader) = translate(reader.build(input))

    //FIXME: Duplicated code
    override fun writeDocument(document: XDocument, output: OutputStream) {
        writer.output(
            when (document) {
                is XmlXDocument -> document.document //FIXME: Maybe bug
                else -> Document(translate(document.root))
            },
            output
        )
    }

    override fun writeDocument(document: XDocument, output: Writer) {
        writer.output(
            when (document) {
                is XmlXDocument -> document.document //FIXME: Maybe bug
                else -> Document(translate(document.root))
            },
            output
        )
    }

    override fun createTranslationReader(input: InputStream) = XmlXReader(input)

    override fun createTranslationReader(input: Reader) = XmlXReader(input)

    override fun createTranslationWriter(output: OutputStream) = XmlXWriter(output)

    override fun createTranslationWriter(output: Writer) = XmlXWriter(output)

    private fun translate(document: Document) = XmlXDocument(document)

    private fun xTree2Content(tree: XTree): Content {
        return when (tree) {
            is XLeaf -> Text(tree.value?.value?.plus('\n'))
            is XNode -> tree.let { node ->
                return when (node) {
                    is XmlXNode -> node.element
                    else -> Element(node.name.normalizeName()).apply {
                        setAttributes(node.attributes.map {
                            Attribute(
                                it.name.normalizeName(),
                                it.value?.value.orEmpty()
                            )
                        })
                        setContent(node.children.map { xTree2Content(it) })
                    }
                }
            }
        }
    }
}
