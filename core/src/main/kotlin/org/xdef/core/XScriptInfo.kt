package org.xdef.core

import java.io.Serializable

/**
 * Class holds information about used XScript
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XScriptInfo(

    /**
     * Name of XScript
     */
    val name: String,

    /**
     * Unique identifier of XScript, e.g. package name
     */
    val scriptId: String,

    /**
     * Name of version, e.g. 1.0.0
     * Not have to be unique
     */
    val versionName: String,

    /**
     * Unique identifier of version
     */
    val versionCode: Int
) : Serializable
