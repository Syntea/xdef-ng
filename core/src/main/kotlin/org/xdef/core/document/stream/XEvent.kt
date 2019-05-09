package org.xdef.core.document.stream

import org.xdef.core.Localizable
import org.xdef.core.Location
import org.xdef.core.document.data.XAttribute
import org.xdef.core.document.data.XValue

/**
 * File contains events used for stream document processing
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 * Basic class defines hierarchy of supported events
 */
sealed class XEvent : Localizable

/**
 * Signaling start of document
 */
open class StartDocumentEvent(
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(location: Location) : this(location.lineNumber, location.columnNumber)
    constructor() : this(Location.NO_LOCATION)

    override fun toString() = javaClass.simpleName!!
}

/**
 * Signaling end of document
 */
open class EndDocumentEvent(
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(location: Location) : this(location.lineNumber, location.columnNumber)
    constructor() : this(Location.NO_LOCATION)

    override fun toString() = javaClass.simpleName!!
}

/**
 * Signaling start of document's node
 *
 * @param name Name of node
 */
open class StartNodeEvent(
    val name: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(name: String, location: Location) : this(name, location.lineNumber, location.columnNumber)
    constructor(name: String) : this(name, Location.NO_LOCATION)

    override fun toString() = javaClass.simpleName!!
}

/**
 * Signaling end of document's node
 * It is sent if all node§s content was processed
 *
 * @param name Name of node
 */
open class EndNodeEvent(
    val name: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(name: String, location: Location) : this(name, location.lineNumber, location.columnNumber)
    constructor(name: String) : this(name, Location.NO_LOCATION)

    override fun toString() = javaClass.simpleName!!
}

/**
 * Signaling the attribute and contains his data like name and value
 *
 * @param attribute Structure of name-value pair
 */
open class AttributeEvent(
    val attribute: XAttribute,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(attribute: XAttribute, location: Location) : this(attribute, location.lineNumber, location.columnNumber)
    constructor(attribute: XAttribute) : this(attribute, Location.NO_LOCATION)

    override fun toString() = "${javaClass.simpleName}(attribute=$attribute)"
}

/**
 * Signaling the value and contains his data
 *
 * @param value The value of event
 */
open class ValueEvent(
    val value: XValue?,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(value: XValue?, location: Location) : this(value, location.lineNumber, location.columnNumber)
    constructor(value: XValue?) : this(value, Location.NO_LOCATION)

    override fun toString() = "${javaClass.simpleName}(value=$value)"
}

/**
 * Special type fo event
 * It used if document contains any information which not used in processing e.g. comments
 */
open class NotProcessedEvent : XEvent() {
    override val lineNumber = Location.NO_LOCATION.lineNumber
    override val columnNumber = Location.NO_LOCATION.columnNumber
}
