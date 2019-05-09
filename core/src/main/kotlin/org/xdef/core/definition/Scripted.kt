package org.xdef.core.definition

import org.xdef.core.lang.Event
import org.xdef.core.lang.Option

/**
 * Interface defines properties for accessing parts of X-definition script
 * without part for defined type of value
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Scripted : OccurrenceBoundary {

    /**
     * Map of event-value
     * It contains all event a their compiled x-script action, which were contained in X-definition script
     */
    val actions: Map<Event, CompiledScript>

    /**
     * Set of [Option], which ware contained in script of X-definition
     */
    val options: Set<Option>

    /**
     * Compiled part of x-script, which contains definition of variables and functions
     */
    val setupScopeCommand: CompiledScript?
}
