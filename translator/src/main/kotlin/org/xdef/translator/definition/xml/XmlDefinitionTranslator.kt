package org.xdef.translator.definition.xml

import org.jdom2.*
import org.jdom2.input.SAXBuilder
import org.jdom2.located.LocatedJDOMFactory
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import org.xdef.core.document.definition.XDefDocument
import org.xdef.core.document.definition.XDefLeaf
import org.xdef.core.document.definition.XDefNode
import org.xdef.core.document.definition.XDefTree
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence
import org.xdef.translator.MISSING_ROOT_ELEMENT_NAME
import org.xdef.translator.definition.DefinitionTranslator
import org.xdef.translator.definition.DefinitionTranslatorIO
import org.xdef.translator.definition.xml.model.XmlXDefAttribute
import org.xdef.translator.definition.xml.model.XmlXDefDocument
import org.xdef.translator.definition.xml.model.XmlXDefLeaf
import org.xdef.translator.definition.xml.model.XmlXDefNode
import org.xdef.translator.document.json.indexedName
import org.xdef.translator.document.xml.model.XmlTextXValue
import org.xdef.translator.extractLocation
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
class XmlDefinitionTranslator : DefinitionTranslator<Element>, DefinitionTranslatorIO {

    companion object {
        const val X_SCRIPT_NAMESPACE = "http://www.syntea.cz/xdef/3.1"
        const val X_SCRIPT_PREFIX = "xd"
        const val X_SCRIPT_NAME = "script"

        // TODO Determine it
        // Same for all
        private val ELEMENT_ALLOWED_OCCURRENCES = listOf<Occurrence>()
        private val ELEMENT_ALLOWED_EVENTS = listOf<Event>()

        private val ATTRIBUTE_ALLOWED_OCCURRENCES = listOf<Occurrence>()
        private val ATTRIBUTE_ALLOWED_EVENTS = listOf<Event>()

        private val TEXT_ALLOWED_OCCURRENCES = listOf<Occurrence>()
        private val TEXT_ALLOWED_EVENTS = listOf<Event>()
    }

    private val reader = SAXBuilder().apply { jdomFactory = LocatedJDOMFactory() }
    private val writer = XMLOutputter().apply { format = Format.getPrettyFormat() }

    override fun translate(dom: Element) = translate(Document(dom))

    override fun translate(definition: XDefDocument): Element {
        return xDefDocument2Document(definition).rootElement
    }

    override fun readDefinition(input: InputStream) = translate(reader.build(input))

    override fun readDefinition(input: Reader) = translate(reader.build(input))

    override fun writeDefinition(definition: XDefDocument, output: OutputStream) {
        writer.output(xDefDocument2Document(definition), output)
    }

    override fun writeDefinition(definition: XDefDocument, output: Writer) {
        writer.output(xDefDocument2Document(definition), output)
    }

    private fun translate(document: Document): XDefDocument {
        return XmlXDefDocument(
            document = document,
            root = element2XDefNode(document.rootElement)
        )
    }

    private fun xDefDocument2Document(definition: XDefDocument): Document {
        return when (definition) {
            is XmlXDefDocument -> definition.document
            else -> Document(
                when (val content = xDefTree2Content(definition.root)) {
                    is Element -> content
                    else -> Element(MISSING_ROOT_ELEMENT_NAME).addContent(content)
                }
            )
        }
    }

    private fun element2XDefNode(element: Element): XDefNode {
        val elementName = element.qualifiedName
        val (scripts, attributes) = element.attributes.partition { attribute ->
            X_SCRIPT_NAMESPACE == attribute.namespaceURI && X_SCRIPT_NAME == attribute.name
        }
        return XmlXDefNode(
            element = element, // Persist raw form
            name = elementName,
            script = scripts.firstOrNull()?.value,
            attributes = attributes.map {
                // XML parser not support location for attribute
                XmlXDefAttribute(
                    name = it.qualifiedName,
                    value = XmlTextXValue(it.value),
                    allowedOccurrences = ATTRIBUTE_ALLOWED_OCCURRENCES,
                    allowedEvents = ATTRIBUTE_ALLOWED_EVENTS
                )
            },
            children = element.content.filter { it is Element || it is Text }.mapIndexed { index, child ->
                when (child) {
                    is Element -> element2XDefNode(child)
                    is Text -> XmlXDefLeaf(
                        name = indexedName(elementName, index),
                        value = XmlTextXValue(child.text),
                        allowedOccurrences = TEXT_ALLOWED_OCCURRENCES,
                        allowedEvents = TEXT_ALLOWED_EVENTS,
                        location = child.extractLocation()
                    )
                    else -> throw IllegalArgumentException("Missing specified type: ${child::class.simpleName}")
                }
            },
            allowedOccurrences = ELEMENT_ALLOWED_OCCURRENCES,
            allowedEvents = ELEMENT_ALLOWED_EVENTS,
            location = element.extractLocation()
        )
    }

    // @param tree not type of XmlXDefTree
    private fun xDefTree2Content(tree: XDefTree): Content {
        return when (tree) {
            is XDefLeaf -> Text(tree.value?.value?.plus('\n'))
            is XDefNode -> tree.let { node ->
                return when (node) {
                    is XmlXDefNode -> node.element
                    else -> {
                        val element = Element(node.name.normalizeName())
                        val attributes = if (node.script != null) listOf(
                            Attribute(
                                X_SCRIPT_NAME,
                                node.script,
                                Namespace.getNamespace(X_SCRIPT_PREFIX, X_SCRIPT_NAMESPACE)
                            )
                        ) else emptyList()
                        element.setAttributes(attributes + node.attributes.map {
                            Attribute(
                                it.name.normalizeName(),
                                it.value?.value.orEmpty()
                            )
                        })
                        element.setContent(node.children.map { xDefTree2Content(it) })
                    }
                }
            }
        }
    }
}
