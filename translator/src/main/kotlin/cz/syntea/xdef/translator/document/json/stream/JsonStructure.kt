package cz.syntea.xdef.translator.document.json.stream

/**
 * Enum defines structured types in JSON and special type [DOCUMENT]
 * which represents whole JSON document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
enum class JsonStructure {
    DOCUMENT,
    OBJECT,
    ARRAY
}
