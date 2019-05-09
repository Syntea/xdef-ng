package org.xdef.runtime

import org.xdef.core.definition.XDefinition
import org.xdef.core.document.data.XTree
import org.xdef.core.document.stream.XReader
import org.xdef.core.document.stream.XWriter

/**
 * Interface of Runtime component
 * It contains method for processing data
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Runtime {

    /**
     * Processing [XTree] in validation mode
     *
     * @param definition X-definition
     * @param input Data for validation
     * @param output Output for continuous writing
     * @param reporter Reporter for logs information about running
     *
     * @return Document from input or fragment of the document
     */
    fun parse(definition: XDefinition, input: XTree, output: XWriter?, reporter: Appendable): XTree

    /**
     * Processing [XTree] in validation mode
     *
     * @param definition X-definition
     * @param input Data for validation
     * @param output Output for continuous writing
     * @param reporter Reporter for logs information about running
     *
     * @return Document from input or fragment of the document
     */
    fun parse(definition: XDefinition, input: XReader, output: XWriter?, reporter: Appendable): XTree

    /**
     * Processing creating mode
     *
     * @param definition X-definition
     * @param modelName Name of started model
     * @param input Data for transformation
     * @param output Output for continuous writing
     * @param reporter Reporter for logs information about running
     *
     * @return Created document or fragment of the document
     */
    fun create(definition: XDefinition, modelName: String, input: XTree?, output: XWriter?, reporter: Appendable): XTree

    /**
     * Processing creating mode
     *
     * @param definition X-definition
     * @param modelName Name of started model
     * @param input Data for transformation
     * @param output Output for continuous writing
     * @param reporter Reporter for logs information about running
     *
     * @return Created document or fragment of the document
     */
    fun create(
        definition: XDefinition,
        modelName: String,
        input: XReader?,
        output: XWriter?,
        reporter: Appendable
    ): XTree
}
