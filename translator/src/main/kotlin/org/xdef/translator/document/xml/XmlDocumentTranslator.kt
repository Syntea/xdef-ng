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
import org.xdef.translator.MISSING_ROOT_ELEMENT_NAME
import org.xdef.translator.document.DocumentTranslator
import org.xdef.translator.document.xml.model.*
import org.xdef.translator.document.xml.stream.XmlXReader
import org.xdef.translator.document.xml.stream.XmlXWriter
import org.xdef.translator.extractLocation
import org.xdef.translator.indexedName
import org.xdef.translator.normalizeName
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

    private val reader = SAXBuilder().apply { jdomFactory = LocatedJDOMFactory() }
    private val writer = XMLOutputter().apply { format = Format.getPrettyFormat() }

    override fun translate(dom: Element) = translate(Document(dom))

    override fun translate(document: XDocument) = translate(document.root)

    override fun translate(documentTree: XTree): Element {
        return when (val content = xTree2Content(documentTree)) {
            is Element -> content
            else -> Element(MISSING_ROOT_ELEMENT_NAME).addContent(content)
        }
    }

    override fun readDocument(input: InputStream) = translate(reader.build(input))

    override fun readDocument(input: Reader) = translate(reader.build(input))

    override fun writeDocument(document: XDocument, output: OutputStream) {
        writer.output(xDocument2Document(document), output)
    }

    override fun writeDocument(document: XDocument, output: Writer) {
        writer.output(xDocument2Document(document), output)
    }

    override fun createTranslationReader(input: InputStream) = XmlXReader(input)

    override fun createTranslationReader(input: Reader) = XmlXReader(input)

    override fun createTranslationWriter(output: OutputStream) = XmlXWriter(output)

    override fun createTranslationWriter(output: Writer) = XmlXWriter(output)

    private fun translate(document: Document): XDocument {
        return XmlXDocument(
            document = document,
            root = element2XNode(document.rootElement)
        )
    }

    private fun element2XNode(element: Element): XNode {
        val elementName = element.qualifiedName
        return XmlXNode(
            element = element, // Persist raw form
            name = elementName,
            attributes = element.attributes.map {
                XmlXAttribute(
                    name = it.qualifiedName,
                    value = XmlTextXValue(it.value)
                )
            },
            children = element.content.filter { it is Element || it is Text }.mapIndexed { index, child ->
                when (child) {
                    is Element -> element2XNode(child)
                    is Text -> XmlXLeaf(
                        text = child,
                        name = indexedName(elementName, index),
                        value = XmlTextXValue(child.text),
                        location = child.extractLocation()
                    )
                    else -> throw IllegalArgumentException("Missing specified type: ${child::class.simpleName}")
                }
            },
            location = element.extractLocation()
        )
    }

    private fun xDocument2Document(document: XDocument): Document {
        return when (document) {
            is XmlXDocument -> document.document //FIXME: Maybe bug
            else -> Document(translate(document.root))
        }
    }

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
