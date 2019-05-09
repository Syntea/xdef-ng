package org.xdef

import org.xdef.translator.SupportedDataType
import java.io.File
import java.net.URL
import java.nio.charset.Charset

typealias FileSource = Triple<SupportedDataType, File, Charset?>
typealias UrlSource = Triple<SupportedDataType, URL, Charset?>

/**
 * Class holds all sources of X-definition
 *
 * To support other sources add list of source to the constructor
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class XDefinitionSources(
    val files: List<FileSource>,
    val urls: List<UrlSource>
)
