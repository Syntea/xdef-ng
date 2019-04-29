package cz.syntea.xdef.core.document

/**
 * Class holds information about type of document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class DocumentType(

    /**
     * Identifier of document, e.g. "XML", "JSON"
     */
    val name: String
)
