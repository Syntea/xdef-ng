package org.xdef.core.document.definition

import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDefinitionSpecifier {
    val allowedOccurrences: List<Occurrence>
    val allowedEvents: List<Event>
}
