package org.xdef.core.lang

/**
 * Enum defined all existing type of occurrence, which used for specify occurrence same structures by processing
 * It's part of r-script
 *
 * Each Occurrence has defined main form representation and alternative forms if exists
 *
 * @param regex Main form
 * @param alternatives Alternative forms
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
enum class Occurrence(
    regex: Regex,
    alternatives: List<Regex>
) {

    REQUIRED(Regex("required"), listOf(Regex("1"), Regex("1\\.\\.1"))),
    OPTIONAL(Regex("optional"), listOf(Regex("\\?"), Regex("0\\.\\.1"))),
    IGNORE(Regex("ignore"), emptyList()),
    ILLEGAL(Regex("illegal"), emptyList()),

    /**
     * Special type which symbols range e.g. m..n
     */
    RANGE(
        Regex("\\*"),
        listOf(
            Regex("\\+"),
            Regex("\\d+"),
            Regex("(\\d+)\\.\\.(\\d+)"),
            Regex("(\\d+)\\.\\.\\*")
        )
    );

    /**
     * Property for returning all forms
     */
    @Suppress("unused")
    val allRegexes = listOf(regex).plus(alternatives)
}
