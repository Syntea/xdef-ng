package org.xdef.core.lang

/**
 * TODO CLASS_DESCRIPTION
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

    val allRegexes = listOf(regex).plus(alternatives)
}
