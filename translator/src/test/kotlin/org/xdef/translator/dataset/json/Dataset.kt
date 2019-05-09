package org.xdef.translator.dataset.json

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

const val minimalObject = "{}"
const val minimalArray = "[]"
const val minimalString = "\"\""
const val minimalNumber = "-1e15"
const val minimalBoolean = "true"
const val minimalUndefined = "null"

const val simpleObject = """
{
    "A": "A-value",
    "B": false,
    "C": 12345,
    "U": null
}
"""
const val simpleNumberArray = "[1, 2, 3, 4, 5]"
const val simpleStringArray = """["A", "B", "C", "D", "E"]"""
const val simpleMixedArray = """["A", 1, null, [], false, {}, true]"""
