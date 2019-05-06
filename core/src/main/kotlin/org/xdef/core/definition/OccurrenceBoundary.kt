package org.xdef.core.definition

import org.xdef.core.lang.Occurrence

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
