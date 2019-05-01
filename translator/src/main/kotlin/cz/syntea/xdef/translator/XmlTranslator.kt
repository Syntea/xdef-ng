package cz.syntea.xdef.translator

import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.translator.definition.DefinitionTranslatorIO
import cz.syntea.xdef.translator.definition.xml.XmlDefinitionTranslator
import cz.syntea.xdef.translator.document.DocumentTranslator
import cz.syntea.xdef.translator.document.xml.XmlDocumentTranslator
import org.jdom2.Element
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlTranslator : DocumentTranslator<Element>, DefinitionTranslatorIO {

    private val documentTranslator = XmlDocumentTranslator()
    private val definitionTranslator = XmlDefinitionTranslator()

    override fun translate(dom: Element) = documentTranslator.translate(dom)

    override fun translate(document: XDocument) = documentTranslator.translate(document)

    override fun translate(documentTree: XTree) = documentTranslator.translate(documentTree)

    override fun readDocument(input: InputStream) = documentTranslator.readDocument(input)

    override fun readDocument(input: Reader) = documentTranslator.readDocument(input)

    override fun writeDocument(document: XDocument, output: OutputStream) =
        documentTranslator.writeDocument(document, output)

    override fun writeDocument(document: XDocument, output: Writer) = documentTranslator.writeDocument(document, output)

    override fun createTranslationReader(input: InputStream) = documentTranslator.createTranslationReader(input)

    override fun createTranslationReader(input: Reader) = documentTranslator.createTranslationReader(input)

    override fun createTranslationWriter(output: OutputStream) = documentTranslator.createTranslationWriter(output)

    override fun createTranslationWriter(output: Writer) = documentTranslator.createTranslationWriter(output)

    override fun readDefinition(input: InputStream) = definitionTranslator.readDefinition(input)

    override fun readDefinition(input: Reader) = definitionTranslator.readDefinition(input)

    override fun writeDefinition(definition: XDefDocument, output: OutputStream) =
        definitionTranslator.writeDefinition(definition, output)

    override fun writeDefinition(definition: XDefDocument, output: Writer) =
        definitionTranslator.writeDefinition(definition, output)
}
