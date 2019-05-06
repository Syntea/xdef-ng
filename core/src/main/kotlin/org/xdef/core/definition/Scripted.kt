package org.xdef.core.definition

import org.xdef.core.lang.Event
import org.xdef.core.lang.Option

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Scripted : OccurrenceBoundary {

    /**
     *
     */
    val actions: Map<Event, CompiledScript>

    /**
     *
     */
    val options: Set<Option>

    /**
     *
     */
    val setupScopeCommand: CompiledScript?
}
