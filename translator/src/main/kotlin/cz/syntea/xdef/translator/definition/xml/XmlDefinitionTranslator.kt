package cz.syntea.xdef.translator.definition.xml

import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.translator.definition.DefinitionTranslator
import cz.syntea.xdef.translator.definition.DefinitionTranslatorIO
import org.jdom2.Element
import java.io.Reader
import java.io.Writer

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlDefinitionTranslator : DefinitionTranslator<Element>, DefinitionTranslatorIO {

    override fun translate(dom: Element): XDefDocument {
        TODO("not implemented")
    }

    override fun translate(definition: XDefDocument): Element {
        TODO("not implemented")
    }

    override fun readDefinition(input: Reader): XDefDocument {
//        org.jdom2.input.StAXStreamBuilder
//        JsonFactory().createParser("").also {
//            while (it.nextToken() != JsonToken.END_ARRAY)
//        }

        TODO("not implemented")
    }

    override fun writeDefinition(definition: XDefDocument, output: Writer) {
        TODO("not implemented")
    }
}
