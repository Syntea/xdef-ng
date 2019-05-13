package org.xdef.translator.document.json.stream

import org.xdef.core.Location
import org.xdef.core.document.stream.EndNodeEvent
import org.xdef.core.document.stream.StartNodeEvent

/**
 * File contains specified events for JSON document
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Event signalises start JSON object
 */
class StartJsonObjectNodeEvent(name: String, location: Location) : StartNodeEvent(name, location) {
    constructor(name: String) : this(name, Location.NO_LOCATION)
}

/**
 * Event signalises end JSON object
 */
class EndJsonObjectNodeEvent(name: String, location: Location) : EndNodeEvent(name, location) {
    constructor(name: String) : this(name, Location.NO_LOCATION)
}

/**
 * Event signalises start JSON array
 */
class StartJsonArrayNodeEvent(name: String, location: Location) : StartNodeEvent(name, location) {
    constructor(name: String) : this(name, Location.NO_LOCATION)
}

/**
 * Event signalises end JSON array
 */
class EndJsonArrayNodeEvent(name: String, location: Location) : EndNodeEvent(name, location) {
    constructor(name: String) : this(name, Location.NO_LOCATION)
}
