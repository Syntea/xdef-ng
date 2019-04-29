package cz.syntea.xdef.translator

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Translator {

    fun createXmlTranslator(): XmlTranslator
    fun createJsonTranslator(): JsonTranslator
}
