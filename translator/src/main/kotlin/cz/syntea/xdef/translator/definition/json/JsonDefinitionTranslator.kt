package cz.syntea.xdef.translator.definition.json

import com.fasterxml.jackson.core.JsonFactory
import cz.syntea.xdef.core.document.definition.XDefDocument
import cz.syntea.xdef.core.document.definition.XDefTree
import cz.syntea.xdef.translator.definition.DefinitionTranslator
import cz.syntea.xdef.translator.definition.DefinitionTranslatorIO
import cz.syntea.xdef.translator.definition.json.model.JsonXDefDocument
import cz.syntea.xdef.translator.document.json.std.LocalizedJsonTree
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import javax.json.JsonValue

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
class JsonDefinitionTranslator : DefinitionTranslator<JsonValue>, DefinitionTranslatorIO {
    override fun readDefinition(input: InputStream): XDefDocument {
        TODO("not implemented")
    }

    override fun writeDefinition(definition: XDefDocument, output: OutputStream) {
        TODO("not implemented")
    }

    companion object {
        const val ROOT_NODE_NAME = "cz.syntea.xdef.root"
        const val X_SCRIPT_IDENTIFIER = "xd:script"
    }

    private val factory = JsonFactory()

    override fun translate(dom: JsonValue) = JsonXDefDocument(jsonValue2XDefTree(ROOT_NODE_NAME, dom))

    override fun translate(definition: XDefDocument) = xDefTree2JsonValue(definition.root)

    override fun readDefinition(input: Reader): XDefDocument {
        val jsonValue = LocalizedJsonTree.readTree(factory.createParser(input))
        return translate(requireNotNull(jsonValue) { "Parsed definition is empty" })
    }

    override fun writeDefinition(definition: XDefDocument, output: Writer) {
        val jsonValue = translate(definition)
        val jsonOutputter = factory.createGenerator(output)
            .useDefaultPrettyPrinter() // TODO: Only debug
        LocalizedJsonTree.writeTree(jsonOutputter, jsonValue)
        jsonOutputter.flush()
    }

    @Throws(IllegalArgumentException::class)
    private fun jsonValue2XDefTree(nodeName: String, value: JsonValue): XDefTree {
        TODO()
    }

    private fun xDefTree2JsonValue(tree: XDefTree): JsonValue {
        TODO()
    }
}
