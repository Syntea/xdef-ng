package cz.syntea.xdef.core.definition

import java.io.Serializable

/**
 * Marked interface for representing compiled part of script
 * Concrete implementation is depend on type of X-script
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface CompiledScript : Serializable {

    /**
     * For support custom serialization/deserialization
     * must explicit override toString method
     */
    override fun toString(): String
}
