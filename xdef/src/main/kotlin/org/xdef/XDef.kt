package org.xdef

import org.apache.logging.log4j.kotlin.Logging
import org.xdef.compiler.Compiler
import org.xdef.compiler.DummyCompiler
import org.xdef.compiler.XDPool
import org.xdef.core.document.definition.XDefDocument
import org.xdef.runtime.DummyRuntime
import org.xdef.runtime.Runtime
import org.xdef.script.XScriptFactory
import org.xdef.translator.SupportedDataType
import org.xdef.translator.TranslatorFactory
import org.xdef.translator.definition.DefinitionTranslatorIO
import org.xdef.translator.document.DocumentTranslator
import java.io.*
import java.net.URL
import java.nio.charset.Charset

/**
 * Main class of whole framework
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class XDef(
    translatorFactory: TranslatorFactory,
    private val compiler: Compiler,
    private val runtime: Runtime,
    private val sources: XDefinitionSources,
    compile: Boolean = false
) : BaseXDef(translatorFactory), Logging {

    private var pool: XDPool? = null

    init {
        if (compile) compile()
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> parse(
        xDefName: String,
        input: InputStream,
        output: OutputStream?,
        reporter: Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.parse(
            definition = definition,
            input = from.createTranslationReader(input),
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> parse(
        xDefName: String, input: Reader, output: Writer?, reporter: Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.parse(
            definition = definition,
            input = from.createTranslationReader(input),
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> parse(
        xDefName: String,
        input: F,
        output: OutputStream?,
        reporter: Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.parse(
            definition = definition,
            input = from.translate(input).root,
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> parse(
        xDefName: String,
        input: F,
        output: Writer?,
        reporter: Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.parse(
            definition = definition,
            input = from.translate(input).root,
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: InputStream?,
        output: OutputStream?,
        reporter: java.lang.Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.create(
            definition = definition,
            modelName = modelName,
            input = input?.let { from.createTranslationReader(it) },
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: Reader?,
        output: Writer?,
        reporter: java.lang.Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.create(
            definition = definition,
            modelName = modelName,
            input = input?.let { from.createTranslationReader(it) },
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: F?,
        output: OutputStream?,
        reporter: java.lang.Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.create(
            definition = definition,
            modelName = modelName,
            input = input?.let { from.translate(input).root },
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    /**
     * This is general method when must be implemented
     * Others method are mapped to it
     */
    override fun <F, T> create(
        xDefName: String,
        modelName: String,
        input: F?,
        output: Writer?,
        reporter: java.lang.Appendable,
        from: DocumentTranslator<F>,
        to: DocumentTranslator<T>
    ): T {
        val pool = returnOrCompilePool(reporter)
        val definition = requireNotNull(pool.createXDefinition(xDefName)) { "Definition $xDefName not exist" }

        val result = runtime.create(
            definition = definition,
            modelName = modelName,
            input = input?.let { from.translate(input).root },
            output = output?.let { to.createTranslationWriter(it) },
            reporter = reporter
        )
        return to.translate(result)
    }

    @Throws(IOException::class)
    fun compile(reporter: Appendable = System.out) {
        if (pool != null) logger.info("XDPool has already been compiled")
        returnOrCompilePool(reporter)
    }

    /**
     * Mediated method from [Compiler] interface
     * Use for serialize [XDPool] which is in XDef instance
     */
    fun serializeXDPool(poolStream: OutputStream) =
        compiler.serializeXDPool(returnOrCompilePool(System.out), poolStream)

    /**
     * Extension of mediated method from [Compiler] interface
     */
    fun serializeXDPool(poolFile: File) = poolFile.outputStream().use { serializeXDPool(it) }

    /**
     * Mediated method from [Compiler] interface
     * Use for deserialize [XDPool] which is in XDef instance
     */
    fun deserializeXDPool(poolStream: InputStream) {
        if (pool != null) logger.warn("XDPool exists, it be overwritten")
        pool = compiler.deserializeXDPool(poolStream)
    }

    /**
     * Extension of mediated method from [Compiler] interface
     */
    fun deserializeXDPool(poolFile: File) = poolFile.inputStream().use { deserializeXDPool(it) }

    @Suppress("NAME_SHADOWING")
    private fun returnOrCompilePool(reporter: Appendable): XDPool {
        val tmpPool = pool
        if (tmpPool != null) {
            return tmpPool
        }
        return synchronized(this) {
            val tmpPool = pool
            if (tmpPool != null) {
                tmpPool
            } else {
                val tmpPool = compiler.compile(translateSources(sources), reporter)
                pool = tmpPool
                tmpPool
            }
        }
    }

    @Throws(IOException::class)
    private fun translateSources(sources: XDefinitionSources): List<XDefDocument> {
        return sources.files.map { (type, source, charset) ->
            val translator: DefinitionTranslatorIO = when (type) {
                SupportedDataType.XML -> xmlTranslator
                SupportedDataType.JSON -> jsonTranslator
            }
            if (charset == null) {
                source.inputStream().use { translator.readDefinition(it) }
            } else {
                source.reader(charset).use { translator.readDefinition(it) }
            }
        } + sources.urls.map { (type, source, charset) ->
            val translator: DefinitionTranslatorIO = when (type) {
                SupportedDataType.XML -> xmlTranslator
                SupportedDataType.JSON -> jsonTranslator
            }
            if (charset == null) {
                source.openStream().use { translator.readDefinition(it) }
            } else {
                source.openStream().reader(charset).use { translator.readDefinition(it) }
            }
        }
    }

    /**
     *
     */
    class Builder {

        private val files = mutableListOf<FileSource>()
        private val urls = mutableListOf<UrlSource>()

        private var factory: XScriptFactory? = null

        /**
         * @param type
         * @param source
         * @param others
         */
        fun addRuleSource(type: SupportedDataType, source: File, vararg others: File): Builder {
            files.add(Triple(type, source, null))
            files.addAll(others.map { Triple(type, it, null) })
            return this
        }

        /**
         * @param type
         * @param sourceCharset
         * @param source
         * @param others
         */
        fun addRuleSource(type: SupportedDataType, sourceCharset: Charset, source: File, vararg others: File): Builder {
            files.add(Triple(type, source, sourceCharset))
            files.addAll(others.map { Triple(type, it, sourceCharset) })
            return this
        }

        /**
         * @param type
         * @param source
         * @param others
         */
        fun addRuleSource(type: SupportedDataType, source: URL, vararg others: URL): Builder {
            urls.add(Triple(type, source, null))
            urls.addAll(others.map { Triple(type, it, null) })
            return this
        }

        /**
         * @param type
         * @param sourceCharset
         * @param source
         * @param others
         */
        fun addRuleSource(type: SupportedDataType, sourceCharset: Charset, source: URL, vararg others: URL): Builder {
            urls.add(Triple(type, source, sourceCharset))
            urls.addAll(others.map { Triple(type, it, sourceCharset) })
            return this
        }

        /**
         * @param factory
         */
        fun setScriptFactory(factory: XScriptFactory): Builder {
            this.factory = factory
            return this
        }

        /**
         * Create XDef framework with compilation of X-definition
         *
         * @return Instance of XDef
         */
        @Throws(IllegalStateException::class, IOException::class)
        fun buildWithCompile() = createXDef(true)

        /**
         * Create XDef framework without compilation of X-definition
         *
         * @return Instance of XDef
         */
        @Throws(IllegalStateException::class)
        fun build() = createXDef(false)

        private fun createXDef(compile: Boolean): XDef {
            checkNotNull(factory) { "Script factory required" }

            return XDef(
                translatorFactory = TranslatorFactory(),
                compiler = DummyCompiler(factory!!),
                runtime = DummyRuntime(factory!!),
                sources = XDefinitionSources(
                    files = files,
                    urls = urls
                ),
                compile = compile
            )
        }
    }
}
