package cz.syntea.xdef.translator.document.xml

import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.core.document.data.XLeaf
import cz.syntea.xdef.core.document.data.XNode
import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.translator.document.DocumentTranslator
import cz.syntea.xdef.translator.document.xml.model.XmlXDocument
import cz.syntea.xdef.translator.document.xml.model.XmlXNode
import cz.syntea.xdef.translator.document.xml.stream.XmlXReader
import cz.syntea.xdef.translator.document.xml.stream.XmlXWriter
import org.jdom2.*
import org.jdom2.input.SAXBuilder
import org.jdom2.located.LocatedJDOMFactory
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
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
        const val ROOT_ELEMENT_NAME = "cz.syntea.xdef.root"

        // TODO not all illegal characters
        private val WS_REGEX = "\\s".toRegex()

        private fun String.normalizeName() = split(WS_REGEX)
            .joinToString(separator = "_")
            .ifEmpty { "__WS__" }
    }

    override fun translate(dom: Element): XDocument {
        TODO("not implemented")
    }

    override fun translate(document: XDocument) = translate(document.root)

    override fun translate(documentTree: XTree): Element {
        return when (val content = xTree2Content(documentTree)) {
            is Element -> content
            else -> Element(ROOT_ELEMENT_NAME).addContent(content)
        }
    }

    //FIXME: Duplicated code
    override fun readDocument(input: InputStream): XDocument {
        val document = SAXBuilder()
            .apply { jdomFactory = LocatedJDOMFactory() }
            .build(input)
        return XmlXDocument(document)
    }

    override fun readDocument(input: Reader): XDocument {
        val document = SAXBuilder()
            .apply { jdomFactory = LocatedJDOMFactory() }
            .build(input)
//        return translate(document.rootElement)
        return XmlXDocument(document)
    }

    //FIXME: Duplicated code
    override fun writeDocument(document: XDocument, output: OutputStream) {
        XMLOutputter()
            .apply { format = Format.getPrettyFormat() }
            .output(
                when (document) {
                    is XmlXDocument -> document.document //FIXME: Maybe bug
                    else -> Document(
                        xTree2Content(document.root).let {
                            when (it) {
                                is Element -> it
                                else -> Element(ROOT_ELEMENT_NAME).addContent(it)
                            }
                        }
                    )
                },
                output
            )
    }

    override fun writeDocument(document: XDocument, output: Writer) {
        XMLOutputter()
            .apply { format = Format.getPrettyFormat() }
            .output(
                when (document) {
                    is XmlXDocument -> document.document //FIXME: Maybe bug
                    else -> Document(
                        xTree2Content(document.root).let {
                            when (it) {
                                is Element -> it
                                else -> Element(ROOT_ELEMENT_NAME).addContent(it)
                            }
                        }
                    )
                },
                output
            )
    }

    override fun createTranslationReader(input: InputStream) = XmlXReader(input)

    override fun createTranslationReader(input: Reader) = XmlXReader(input)

    override fun createTranslationWriter(output: OutputStream) = XmlXWriter(output)

    override fun createTranslationWriter(output: Writer) = XmlXWriter(output)

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
