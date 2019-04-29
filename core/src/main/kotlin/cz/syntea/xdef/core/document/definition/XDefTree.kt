package cz.syntea.xdef.core.document.definition

import cz.syntea.xdef.core.*
import cz.syntea.xdef.core.document.data.XValue
import cz.syntea.xdef.core.lang.Event
import cz.syntea.xdef.core.lang.Occurrence

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
    override val allowedOccurrence: List<Occurrence>,
    override val allowedEvent: List<Event>,
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
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : XDefTree(name, allowedOccurrence, allowedEvent, location), Node<XDefAttribute, XDefTree>

/**
 *
 */
open class XDefLeaf(
    name: String,
    override val value: XValue?,
    allowedOccurrence: List<Occurrence>,
    allowedEvent: List<Event>,
    location: Location
) : XDefTree(name, allowedOccurrence, allowedEvent, location), Leaf<XValue> {

    override val script get() = value?.toString()
}
