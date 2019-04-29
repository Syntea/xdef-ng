package cz.syntea.xdef.translator.document.json.stream

import cz.syntea.xdef.core.Location
import cz.syntea.xdef.core.document.stream.EndNodeEvent
import cz.syntea.xdef.core.document.stream.StartNodeEvent

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

class StartJsonObjectNodeEvent(name: String, location: Location) : StartNodeEvent(name, location)

class EndJsonObjectNodeEvent(name: String, location: Location) : EndNodeEvent(name, location)

class StartJsonArrayNodeEvent(name: String, location: Location) : StartNodeEvent(name, location)

class EndJsonArrayNodeEvent(name: String, location: Location) : EndNodeEvent(name, location)
