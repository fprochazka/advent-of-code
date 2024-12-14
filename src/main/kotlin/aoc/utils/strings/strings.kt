package aoc.utils.strings

import org.intellij.lang.annotations.Language

fun String.toInts(limit: Int = 0): List<Int> =
    "[^\\d-]+".toRegex().split(this.trim(), limit)
        .map { it.toInt() }

fun String.toLongs(limit: Int = 0): List<Long> =
    "[^\\d-]+".toRegex().split(this.trim(), limit)
        .map { it.toLong() }

fun String.half(): Pair<String, String> =
    (length / 2)
        .let { halfLength -> substring(0, halfLength) to substring(halfLength, length) }

fun <R> String.matchEntire(@Language("RegExp") regex: String, map: (MatchResult) -> R): R =
    matchEntire(regex.toRegex(), map)

fun <R> String.matchEntire(regex: Regex, map: (MatchResult) -> R): R =
    tryMatchEntire(regex, map) ?: error("No match for '$regex' in '$this'")

fun <R> String.tryMatchEntire(@Language("RegExp") regex: String, map: (MatchResult) -> R): R? =
    tryMatchEntire(regex.toRegex(), map)

fun <R> String.tryMatchEntire(regex: Regex, map: (MatchResult) -> R): R? =
    regex.matchEntire(this)?.let(map)
