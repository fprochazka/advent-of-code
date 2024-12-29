package aoc.utils.strings

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongList
import org.intellij.lang.annotations.Language

fun String.toInts(limit: Int = 0): IntList {
    val items = "[^\\d-]+".toRegex().split(this.trim(), limit)
    val result = IntArrayList(items.size)
    for (item in items) {
        result.add(item.toInt())
    }
    return result
}

fun String.toLongs(limit: Int = 0): LongList {
    val items = "[^\\d-]+".toRegex().split(this.trim(), limit)
    val result = LongArrayList(items.size)
    for (item in items) {
        result.add(item.toLong())
    }
    return result
}

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
