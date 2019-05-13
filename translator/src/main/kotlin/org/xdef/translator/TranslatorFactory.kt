package org.xdef.translator

/**
 * Implementation of Translator component interface
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 *
 * @see [Translator]
 */
class TranslatorFactory : Translator {

    override fun createXmlTranslator() = XmlTranslator()

    override fun createJsonTranslator() = JsonTranslator()
}
