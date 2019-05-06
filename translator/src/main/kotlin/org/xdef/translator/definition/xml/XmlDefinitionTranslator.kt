package org.xdef.translator.definition.xml

import org.jdom2.Element
import org.xdef.core.document.definition.XDefDocument
import org.xdef.translator.definition.DefinitionTranslator
import org.xdef.translator.definition.DefinitionTranslatorIO
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

    override fun translate(dom: Element): XDefDocument {
        TODO("not implemented")
    }

    override fun translate(definition: XDefDocument): Element {
        TODO("not implemented")
    }

    override fun readDefinition(input: InputStream): XDefDocument {
        TODO("not implemented")
    }

    override fun readDefinition(input: Reader): XDefDocument {
        TODO("not implemented")
    }

    override fun writeDefinition(definition: XDefDocument, output: OutputStream) {
        TODO("not implemented")
    }

    override fun writeDefinition(definition: XDefDocument, output: Writer) {
        TODO("not implemented")
    }
}
