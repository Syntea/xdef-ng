package cz.syntea.xdef.model

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Document {

    val type: Type
    val root: DataNode

    data class Type(
        val name: String
    )
}
