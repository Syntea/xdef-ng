package org.xdef.sample.construction

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
        .build()

    @Suppress("UNUSED_VARIABLE")
    val jsonValue = xdef.createJSONtoJSON("example", "exampleModel", File("example.json").inputStream())
}
