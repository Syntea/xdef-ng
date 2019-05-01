package cz.syntea.xdef.runtime

import cz.syntea.xdef.core.definition.XDefinition
import cz.syntea.xdef.core.document.data.XTree
import cz.syntea.xdef.core.document.stream.XReader
import cz.syntea.xdef.core.document.stream.XWriter

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
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun create(definition: XDefinition, input: XTree?, output: XWriter?, reporter: Appendable): XTree

    /**
     * @param definition
     * @param input
     * @param output
     * @param reporter
     *
     * @return
     */
    fun create(definition: XDefinition, input: XReader?, output: XWriter?, reporter: Appendable): XTree
}
