package org.xdef.translator

/**
 * Interface of Translator component
 * It contains methods for creating specific translator
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Translator {

    /**
     * @return Translator for XML format
     */
    fun createXmlTranslator(): XmlTranslator

    /**
     * @return Translator for JSON format
     */
    fun createJsonTranslator(): JsonTranslator
}
