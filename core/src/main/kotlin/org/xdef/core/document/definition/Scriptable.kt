package org.xdef.core.document.definition

/**
 * Interface symbolizes that descendant can be scripted
 * can contain script of X-definition
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
interface Scriptable {

    /**
     * Property with the script value
     */
    val script: String?
}
