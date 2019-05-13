package org.xdef.translator.definition.xml.model

import org.xdef.core.document.data.XValue
import org.xdef.core.document.definition.XDefAttribute
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence

/**
 * XML X-definition attribute
 * Reimplementation of [XDefAttribute] for XML X-definition document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class XmlXDefAttribute(
    name: String,
    value: XValue?,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>
) : XDefAttribute(
    name,
    value,
    allowedOccurrences,
    allowedEvents
    /*NO LOCATION*/
)
