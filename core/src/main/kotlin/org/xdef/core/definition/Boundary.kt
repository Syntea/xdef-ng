package org.xdef.core.definition

import java.io.Serializable

/**
 * Interface symbolizes objects with boundaries
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Boundary : Serializable {

    /**
     * Lower bound
     */
    val minBound: Long

    /**
     * Upper bound
     */
    val maxBound: Long
}
