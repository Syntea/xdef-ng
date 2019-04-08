package cz.syntea.xdef.translator.data.xml

import cz.syntea.xdef.model.DataNode
import cz.syntea.xdef.model.Document
import cz.syntea.xdef.translator.data.DataTranslator
import cz.syntea.xdef.translator.data.xml.model.XmlDocument
import org.jdom2.Attribute
import org.jdom2.Element
import org.jdom2.input.StAXStreamBuilder
import org.jdom2.output.XMLOutputter
import java.io.InputStream
import java.io.OutputStream
import javax.xml.stream.XMLInputFactory

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlDataTranslator : DataTranslator {

    private val inputFactory = XMLInputFactory.newFactory()

    override fun readAsDocument(input: InputStream): Document {
        val doc = StAXStreamBuilder()
            .build(inputFactory.createXMLStreamReader(input))

        return XmlDocument(document = doc)
    }

    override fun writeDocument(document: Document, output: OutputStream) {
        XMLOutputter().output(
            when (document) {
                is XmlDocument -> document.document
                else -> convertToXmlDocument(document)
            },
            output
        )
    }

    private fun convertToXmlDocument(document: Document) = org.jdom2.Document(document.root.toElement())
}

private fun DataNode.toElement(): Element {
    val element = Element(name)
    element.text = value
    element.setAttributes(attributes.map { it.toAttribute() })
    element.setContent(children.map { it.toElement() })
    return element
}

private fun Map.Entry<String, String>.toAttribute() = Attribute(key, value)
