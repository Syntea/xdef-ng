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
 *
 */
sealed class XEvent : Localizable

/**
 *
 */
open class StartDocumentEvent(
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(location: Location) : this(location.lineNumber, location.columnNumber)

    override fun toString() = javaClass.simpleName!!
}

/**
 *
 */
open class EndDocumentEvent(
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(location: Location) : this(location.lineNumber, location.columnNumber)

    override fun toString() = javaClass.simpleName!!
}

/**
 * @param name
 */
open class StartNodeEvent(
    val name: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(name: String, location: Location) : this(name, location.lineNumber, location.columnNumber)

    override fun toString() = javaClass.simpleName!!
}

/**
 * @param name
 */
open class EndNodeEvent(
    val name: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(name: String, location: Location) : this(name, location.lineNumber, location.columnNumber)

    override fun toString() = javaClass.simpleName!!
}

/**
 * @param attribute
 */
open class AttributeEvent(
    val attribute: XAttribute,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(attribute: XAttribute, location: Location) : this(attribute, location.lineNumber, location.columnNumber)

    override fun toString() = "${javaClass.simpleName}(attribute=$attribute)"
}

/**
 * @param value
 */
open class ValueEvent(
    val value: XValue?,
    override val lineNumber: Int,
    override val columnNumber: Int
) : XEvent() {

    constructor(value: XValue?, location: Location) : this(value, location.lineNumber, location.columnNumber)

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
