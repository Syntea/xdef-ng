package cz.syntea.xdef.translator

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class TranslatorFactory : Translator {

    override fun createXmlTranslator() = XmlTranslator()

    override fun createJsonTranslator() = JsonTranslator()
}
