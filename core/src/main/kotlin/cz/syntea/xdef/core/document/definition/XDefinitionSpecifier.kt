package cz.syntea.xdef.core.document.definition

import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface XDefinitionSpecifier {
    val allowedOccurrence: List<Occurrence>
    val allowedEvent: List<Event>
}
