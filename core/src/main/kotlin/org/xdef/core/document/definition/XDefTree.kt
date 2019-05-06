package org.xdef.core.document.definition

import org.xdef.core.*
import org.xdef.core.document.data.XValue
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

/**
 *
 */
sealed class XDefTree(
    override val name: String,
    override val allowedOccurrences: List<Occurrence>,
    override val allowedEvents: List<Event>,
    location: Location
) : XDefinitionSpecifier, Scriptable, Localizable, Tree {
    override val lineNumber = location.lineNumber
    override val columnNumber = location.columnNumber
}

/**
 *
 */
open class XDefNode(
    name: String,
    override val script: String?,
    override val attributes: List<XDefAttribute>,
    override val children: List<XDefTree>,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefTree(name, allowedOccurrences, allowedEvents, location), Node<XDefAttribute, XDefTree>

/**
 *
 */
open class XDefLeaf(
    name: String,
    override val value: XValue?,
    allowedOccurrences: List<Occurrence>,
    allowedEvents: List<Event>,
    location: Location
) : XDefTree(name, allowedOccurrences, allowedEvents, location), Leaf<XValue> {

    override val script get() = value?.toString()
}
