package cz.syntea.xdef.core

/**
 * Interface supply location object in source data
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Localizable {

    /**
     * Number of line
     */
    val lineNumber: Int

    /**
     * Number of column
     */
    val columnNumber: Int
}
