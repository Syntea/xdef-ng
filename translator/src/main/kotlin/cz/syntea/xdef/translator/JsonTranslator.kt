package cz.syntea.xdef.translator

import cz.syntea.xdef.core.document.data.XDocument
import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.translator.definition.DefinitionTranslatorIO
import cz.syntea.xdef.translator.definition.json.JsonDefinitionTranslator
import cz.syntea.xdef.translator.document.DocumentTranslator
import cz.syntea.xdef.translator.document.json.JsonDocumentTranslator
import java.io.Reader
import java.io.Writer
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonTranslator : DocumentTranslator<JsonValue>, DefinitionTranslatorIO {

    private val documentTranslator = JsonDocumentTranslator()
    private val definitionTranslator = JsonDefinitionTranslator()

    override fun translate(dom: JsonValue) = documentTranslator.translate(dom)

    override fun translate(document: XDocument) = documentTranslator.translate(document)

    override fun readDocument(input: Reader) = documentTranslator.readDocument(input)

    override fun writeDocument(document: XDocument, output: Writer) = documentTranslator.writeDocument(document, output)

    override fun createTranslationReader(input: Reader) = documentTranslator.createTranslationReader(input)

    override fun createTranslationWriter(output: Writer) = documentTranslator.createTranslationWriter(output)

    override fun readDefinition(input: Reader) = definitionTranslator.readDefinition(input)

    override fun writeDefinition(definition: XDefDocument, output: Writer) =
        definitionTranslator.writeDefinition(definition, output)
}
