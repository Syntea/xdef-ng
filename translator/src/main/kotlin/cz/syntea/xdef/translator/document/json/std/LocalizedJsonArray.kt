package cz.syntea.xdef.translator.document.json.std

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import javax.json.*

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedJsonArray(
    private val valueList: List<JsonValue>,
    override val lineNumber: Int,
    override val columnNumber: Int
) : java.util.AbstractList<JsonValue>(), JsonArray, Localizable {

    constructor(valueList: List<JsonValue>) : this(
        valueList,
        Location.NO_LOCATION.lineNumber,
        Location.NO_LOCATION.columnNumber
    )

    override val size: Int get() = valueList.size

    @Throws(IndexOutOfBoundsException::class)
    override fun getJsonObject(index: Int): JsonObject {
        return valueList[index] as JsonObject
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getJsonArray(index: Int): JsonArray {
        return valueList[index] as JsonArray
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getJsonNumber(index: Int): JsonNumber {
        return valueList[index] as JsonNumber
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getJsonString(index: Int): JsonString {
        return valueList[index] as JsonString
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : JsonValue> getValuesAs(clazz: Class<T>): List<T> {
        return valueList as List<T>
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getString(index: Int): String {
        return getJsonString(index).string
    }

    override fun getString(index: Int, defaultValue: String): String {
        return try {
            getString(index)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getInt(index: Int): Int {
        return getJsonNumber(index).intValue()
    }

    override fun getInt(index: Int, defaultValue: Int): Int {
        return try {
            getInt(index)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(IndexOutOfBoundsException::class, ClassCastException::class)
    override fun getBoolean(index: Int): Boolean {
        val value = get(index)
        return when (value.valueType) {
            JsonValue.ValueType.TRUE -> true
            JsonValue.ValueType.FALSE -> false
            else -> throw ClassCastException()
        }
    }

    override fun getBoolean(index: Int, defaultValue: Boolean): Boolean {
        return try {
            getBoolean(index)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun isNull(index: Int): Boolean {
        return valueList[index].valueType == JsonValue.ValueType.NULL
    }

    override fun getValueType(): JsonValue.ValueType {
        return JsonValue.ValueType.ARRAY
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun get(index: Int): JsonValue {
        return valueList[index]
    }

//    override fun toString(): String {
//        val sw = StringWriter()
//        JsonWriterImpl(sw, bufferPool).use { jw -> jw.write(this) }
//        return sw.toString()
//    }

    override fun asJsonArray() = this
}
