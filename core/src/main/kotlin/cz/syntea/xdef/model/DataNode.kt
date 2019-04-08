package cz.syntea.xdef.model

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface DataNode {

    val name: String
    var value: String?

    val attributes: Map<String, String>
    val parent: DataNode?
    val children: List<DataNode>
}
