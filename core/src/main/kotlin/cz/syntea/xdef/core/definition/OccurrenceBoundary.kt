package cz.syntea.xdef.core.definition

import cz.syntea.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface OccurrenceBoundary : Boundary {

    /**
     * Type of occurrence
     */
    val occurrence: Occurrence
}
