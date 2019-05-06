package org.xdef.translator.document.json.stream

import org.xdef.core.Location
import org.xdef.core.document.stream.EndNodeEvent
import org.xdef.core.document.stream.StartNodeEvent

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

class StartJsonObjectNodeEvent(name: String, location: Location) : StartNodeEvent(name, location)

class EndJsonObjectNodeEvent(name: String, location: Location) : EndNodeEvent(name, location)

class StartJsonArrayNodeEvent(name: String, location: Location) : StartNodeEvent(name, location)

class EndJsonArrayNodeEvent(name: String, location: Location) : EndNodeEvent(name, location)
