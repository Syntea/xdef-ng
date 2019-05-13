package org.xdef.translator.document.std

import com.fasterxml.jackson.core.JsonFactory
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import org.xdef.core.Localizable
import org.xdef.translator.document.json.std.LocalizedJsonTree
import javax.json.JsonValue

/**
 * Tests for [LocalizedJsonTree]
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class LocalizedJsonTreeTest : FreeSpec() {

    init {
        "empty document" { "".fromJson().shouldBeNull() }
        "primitive document" {
            "null".fromJson() should { doc ->
                doc.shouldNotBeNull()
                doc.valueType shouldBe JsonValue.ValueType.NULL
                doc.shouldBeInstanceOf<Localizable> { loc ->
                    loc.lineNumber shouldBe 1
                    loc.columnNumber shouldBe 1
                }
            }
        }
        "simple document (without formatting)" {
            """[1, 2e-10, "xxx", true, null, [], false, {}]""".fromJson() should { value ->
                value.shouldNotBeNull()
                value.valueType shouldBe JsonValue.ValueType.ARRAY
                value.asJsonArray() should { array ->
                    array shouldHaveSize 8
                    array.map { it.valueType } shouldContainExactly arrayListOf(
                        JsonValue.ValueType.NUMBER,
                        JsonValue.ValueType.NUMBER,
                        JsonValue.ValueType.STRING,
                        JsonValue.ValueType.TRUE,
                        JsonValue.ValueType.NULL,
                        JsonValue.ValueType.ARRAY,
                        JsonValue.ValueType.FALSE,
                        JsonValue.ValueType.OBJECT
                    )
                    array.map { (it as Localizable).lineNumber } shouldContainExactly IntArray(array.size) { 1 }.asList()
                    array.map { (it as Localizable).columnNumber } shouldContainExactly arrayListOf(
                        2, 5, 12, 19, 25, 31, 35, 42
                    )
                }
            }
        }
        "simple document (with bad formatting)" {
            """[
  1,
        2e-10,
  "xxx",
true,          null
]""".fromJson() should { value ->
                value.shouldNotBeNull()
                value.valueType shouldBe JsonValue.ValueType.ARRAY
                value.asJsonArray() should { array ->
                    array shouldHaveSize 5
                    array.map { it.valueType } shouldContainExactly arrayListOf(
                        JsonValue.ValueType.NUMBER,
                        JsonValue.ValueType.NUMBER,
                        JsonValue.ValueType.STRING,
                        JsonValue.ValueType.TRUE,
                        JsonValue.ValueType.NULL
                    )
                    array.map { (it as Localizable).lineNumber to it.columnNumber } shouldContainExactly arrayListOf(
                        2 to 3,
                        3 to 9,
                        4 to 3,
                        5 to 1,
                        5 to 16
                    )
                }
            }
        }
    }

    private fun String.fromJson() = reader().use { LocalizedJsonTree.readTree(JsonFactory().createParser(it)) }
}
