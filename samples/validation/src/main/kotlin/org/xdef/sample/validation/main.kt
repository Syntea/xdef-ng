package org.xdef.sample.validation

import org.xdef.XDef
import org.xdef.translator.SupportedDataType
import java.io.File
import java.nio.charset.Charset

//TODO: Add real example
fun main() {
    val xdef = XDef.Builder()
        .addRuleSource(
            SupportedDataType.JSON,
            Charset.defaultCharset(),
            File("example.xjson")
        )
        .buildWithCompile()

    val jsonValue = xdef.parseJSONtoJSON("example", File("example.json").inputStream())
}
