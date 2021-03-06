package org.xdef.translator.dataset.json

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

const val minimalDefObject = """{"xd:script": "required"}"""
const val minimalDefArray = """["optional; onTrue xyz"]"""
const val minimalDefValue = """"optional; onTrue xyz""""

const val simpleDefObject = """{"xd:script": "1..*", "a": 1, "b": "c"}"""
const val simpleDefArray = """["required", 1, null, true]"""
