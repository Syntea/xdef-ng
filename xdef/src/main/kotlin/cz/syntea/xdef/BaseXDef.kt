// Not change - generated
package cz.syntea.xdef

import cz.syntea.xdef.translator.JsonTranslator
import cz.syntea.xdef.translator.TranslatorFactory
import cz.syntea.xdef.translator.XmlTranslator
import cz.syntea.xdef.translator.document.DocumentTranslator
import org.jdom2.Element
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import javax.json.JsonValue

@Suppress(
    "MemberVisibilityCanBePrivate",
    "unused"
)
abstract class BaseXDef(protected val translatorFactory: TranslatorFactory) {

    protected val xmlTranslator: XmlTranslator by lazy { translatorFactory.createXmlTranslator() }

    protected val jsonTranslator: JsonTranslator by lazy { translatorFactory.createJsonTranslator() }

    protected abstract fun <F, T> parse(
        xDefName: String,
        input: InputStream,
        output: OutputStream? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> parse(
        xDefName: String,
        input: Reader,
        output: Writer? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> parse(
        xDefName: String,
        input: F,
        output: OutputStream? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> parse(
        xDefName: String,
        input: F,
        output: Writer? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: F?,
        output: OutputStream? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    protected abstract fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: F?,
        output: Writer? = null,
        reporter: Appendable = System.out,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T

    fun parseXMLtoXML(
        xDefName: String,
        input: InputStream,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun parseXMLtoXML(
        xDefName: String,
        input: Reader,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun parseXMLtoXML(
        xDefName: String,
        input: Element,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun parseXMLtoXML(
        xDefName: String,
        input: Element,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun parseXMLtoJSON(
        xDefName: String,
        input: InputStream,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun parseXMLtoJSON(
        xDefName: String,
        input: Reader,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun parseXMLtoJSON(
        xDefName: String,
        input: Element,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun parseXMLtoJSON(
        xDefName: String,
        input: Element,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun parseJSONtoXML(
        xDefName: String,
        input: InputStream,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun parseJSONtoXML(
        xDefName: String,
        input: Reader,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun parseJSONtoXML(
        xDefName: String,
        input: JsonValue,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun parseJSONtoXML(
        xDefName: String,
        input: JsonValue,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun parseJSONtoJSON(
        xDefName: String,
        input: InputStream,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun parseJSONtoJSON(
        xDefName: String,
        input: Reader,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun parseJSONtoJSON(
        xDefName: String,
        input: JsonValue,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun parseJSONtoJSON(
        xDefName: String,
        input: JsonValue,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = parse(xDefName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun createXMLtoXML(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun createXMLtoXML(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun createXMLtoXML(
        xDefName: String,
        modelName: String,
        input: Element?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun createXMLtoXML(
        xDefName: String,
        modelName: String,
        input: Element?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, xmlTranslator)

    fun createXMLtoJSON(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun createXMLtoJSON(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun createXMLtoJSON(
        xDefName: String,
        modelName: String,
        input: Element?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun createXMLtoJSON(
        xDefName: String,
        modelName: String,
        input: Element?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, xmlTranslator, jsonTranslator)

    fun createJSONtoXML(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun createJSONtoXML(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun createJSONtoXML(
        xDefName: String,
        modelName: String,
        input: JsonValue?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun createJSONtoXML(
        xDefName: String,
        modelName: String,
        input: JsonValue?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, xmlTranslator)

    fun createJSONtoJSON(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun createJSONtoJSON(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun createJSONtoJSON(
        xDefName: String,
        modelName: String,
        input: JsonValue?,
        output: OutputStream? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, jsonTranslator)

    fun createJSONtoJSON(
        xDefName: String,
        modelName: String,
        input: JsonValue?,
        output: Writer? = null,
        reporter: Appendable = System.out
    ) = create(xDefName, modelName, input, output, reporter, jsonTranslator, jsonTranslator)
}
