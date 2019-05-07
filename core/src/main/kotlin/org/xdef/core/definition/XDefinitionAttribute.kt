package org.xdef.core.definition

import org.xdef.core.Attribute
import org.xdef.core.Localizable
import org.xdef.core.lang.Event
import org.xdef.core.lang.Occurrence
import org.xdef.core.lang.Option
import java.io.Serializable

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XDefinitionAttribute(
    override val name: String,
    override val value: CompiledScript?,
    override val occurrence: Occurrence,
    override val minBound: Long,
    override val maxBound: Long,
    override val actions: Map<Event, CompiledScript>,
    override val options: Set<Option>,
    override val setupScopeCommand: CompiledScript?,
    override val lineNumber: Int,
    override val columnNumber: Int
) : Attribute<CompiledScript>, Scripted, Localizable, Serializable