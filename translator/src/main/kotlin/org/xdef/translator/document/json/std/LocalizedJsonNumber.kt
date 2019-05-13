package org.xdef.translator.document.json.std

import org.xdef.core.Localizable
import org.xdef.core.Location
import java.math.BigDecimal
import java.math.BigInteger
import javax.json.JsonNumber
import javax.json.JsonValue

/**
 * Implementation of [JsonNumber] with support of localization in source data
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
data class LocalizedJsonNumber(
    private val number: String,
    override val lineNumber: Int,
    override val columnNumber: Int
) : JsonNumber, Localizable {

    constructor(number: String) : this(number, Location.NO_LOCATION.lineNumber, Location.NO_LOCATION.columnNumber)

    private val bigDecimal by lazy { BigDecimal(number) }

    override fun bigDecimalValue(): BigDecimal {
        return bigDecimal
    }

    override fun isIntegral(): Boolean {
        return bigDecimalValue().scale() == 0
    }

    override fun intValue(): Int {
        return bigDecimalValue().toInt()
    }

    override fun intValueExact(): Int {
        return bigDecimalValue().intValueExact()
    }

    override fun longValue(): Long {
        return bigDecimalValue().toLong()
    }

    override fun longValueExact(): Long {
        return bigDecimalValue().longValueExact()
    }

    override fun doubleValue(): Double {
        return bigDecimalValue().toDouble()
    }

    override fun bigIntegerValue(): BigInteger {
        return bigDecimalValue().toBigInteger()
    }

    override fun bigIntegerValueExact(): BigInteger {
        return bigDecimalValue().toBigIntegerExact()
    }

    override fun getValueType(): JsonValue.ValueType {
        return JsonValue.ValueType.NUMBER
    }

    override fun toString() = number
}
