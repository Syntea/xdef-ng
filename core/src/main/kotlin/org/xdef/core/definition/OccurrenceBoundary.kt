package org.xdef.core.definition

import org.xdef.core.lang.Occurrence

/**
 * Interface represents part of r-script, which defines occurrence of elem
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface OccurrenceBoundary : Boundary {

    /**
     * Type of occurrence
     */
    val occurrence: Occurrence
}
