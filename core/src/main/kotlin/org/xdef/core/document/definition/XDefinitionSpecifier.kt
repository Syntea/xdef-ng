package org.xdef.core.document.definition

import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence

/**
 * Interface defines properties for specify allowed type of parts of X-definition script
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDefinitionSpecifier {
    /**
     * Collection of allowed type of occurrences
     */
    val allowedOccurrences: List<Occurrence>

    /**
     * Collection of allowed type of events
     */
    val allowedEvents: List<Event>
}
