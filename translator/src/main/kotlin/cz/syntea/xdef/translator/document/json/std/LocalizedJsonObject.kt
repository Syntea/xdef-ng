package cz.syntea.xdef.translator.document.json.std

import cz.syntea.xdef.core.Localizable
import cz.syntea.xdef.core.Location
import javax.json.*

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedJsonObject(
    private val valueMap: Map<String, JsonValue>,
    private val valueLocationMap: Map<String, Location>,
    override val lineNumber: Int,
    override val columnNumber: Int
) : java.util.AbstractMap<String, JsonValue>(), JsonObject, Localizable {

    constructor(valueMap: Map<String, JsonValue>) : this(
        valueMap,
        emptyMap(),
        Location.NO_LOCATION.lineNumber,
        Location.NO_LOCATION.columnNumber
    )

    override fun getJsonArray(name: String): JsonArray? {
        return get(name) as? JsonArray
    }

    override fun getJsonObject(name: String): JsonObject? {
        return get(name) as? JsonObject
    }

    override fun getJsonNumber(name: String): JsonNumber? {
        return get(name) as? JsonNumber
    }

    override fun getJsonString(name: String): JsonString? {
        return get(name) as? JsonString
    }

    @Throws(NullPointerException::class)
    override fun getString(name: String): String {
        return getJsonString(name)!!.string
    }

    override fun getString(name: String, defaultValue: String): String {
        return try {
            getString(name)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(NullPointerException::class)
    override fun getInt(name: String): Int {
        return getJsonNumber(name)!!.intValue()
    }

    override fun getInt(name: String, defaultValue: Int): Int {
        return try {
            getInt(name)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(NullPointerException::class, ClassCastException::class)
    override fun getBoolean(name: String): Boolean {
        val value = get(name)
        return when (value?.valueType) {
            JsonValue.ValueType.TRUE -> true
            JsonValue.ValueType.FALSE -> false
            null -> throw NullPointerException()
            else -> throw ClassCastException()
        }
    }

    override fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        return try {
            getBoolean(name)
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(NullPointerException::class)
    override fun isNull(name: String): Boolean {
        return get(name)?.valueType == JsonValue.ValueType.NULL
    }

    override fun getValueType(): JsonValue.ValueType {
        return JsonValue.ValueType.OBJECT
    }

    override val entries get() = valueMap.toMutableMap().entries

    val localizedEntries
        get() = valueMap.entries.zip(valueLocationMap.entries) { (name, value), (_, location) ->
            LocalizedName(name, location.lineNumber, location.columnNumber) to value
        }

//    override fun toString(): String {
//        val sw = StringWriter()
//        JsonWriterImpl(sw, bufferPool).use { jw -> jw.write(this) }
//        return sw.toString()
//    }

    override fun asJsonObject() = this

    override val size: Int get() = valueMap.size

    @Throws(NullPointerException::class)
    override fun get(key: String) = valueMap[key]

    override fun containsKey(key: String) = valueMap.containsKey(key)

    override fun containsValue(value: JsonValue?) = valueMap.containsValue(value)
}
