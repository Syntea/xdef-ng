package org.xdef.runtime

import org.xdef.core.definition.XDefinition
import org.xdef.core.document.data.XTree
import org.xdef.core.document.stream.XReader
import org.xdef.core.document.stream.XWriter

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Runtime {

    /**
     * @param definition
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun parse(definition: XDefinition, input: XTree, output: XWriter?, reporter: Appendable): XTree

    /**
     * @param definition
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun parse(definition: XDefinition, input: XReader, output: XWriter?, reporter: Appendable): XTree

    /**
     * @param definition
     * @param modelName
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun create(definition: XDefinition, modelName: String, input: XTree?, output: XWriter?, reporter: Appendable): XTree

    /**
     * @param definition
     * @param modelName
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun create(
        definition: XDefinition,
        modelName: String,
        input: XReader?,
        output: XWriter?,
        reporter: Appendable
    ): XTree
}
