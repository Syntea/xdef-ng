package cz.syntea.xdef.core.definition

import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Option

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
//TODO Name!!!
interface Xyz {

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
