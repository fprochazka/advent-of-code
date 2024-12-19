package aoc.y2024

import aoc.utils.Resource
import kotlin.collections.count

fun Resource.day19(): Day19 = Day19.parse(content())

data class Day19(
    val availablePatterns: List<String>,
    val desiredPatterns: List<String>
) {

    val maxAvailableLength by lazy { availablePatterns.maxOf { it.length } }
    val possiblePatterns by lazy { possiblePatterns() }

    val result1 by lazy {
        possiblePatterns.count()
    }
    val result2 by lazy {
        howManyWaysYouCanMakeEachPattern()
    }

    fun possiblePatterns(): List<String> {
        val possible = HashMap<String, Boolean>().apply {
            put("", true)
            availablePatterns.forEach { put(it, true) }
        }

        fun isPossible(desired: String): Boolean {
            if (possible.contains(desired)) return true

            val maxLengthToCheck = maxAvailableLength.coerceAtMost(desired.length - 1)
            for (l in (1..maxLengthToCheck).reversed()) {
                val head = desired.take(l)
                val isHeadPossible = possible.getOrPut(head) { isPossible(head) }
                if (!isHeadPossible) continue

                val tail = desired.drop(l)
                val isTailPossible = possible.getOrPut(tail) { isPossible(tail) }
                if (!isTailPossible) continue

                possible[desired] = true

                return true
            }

            return false
        }

        val possiblePatterns = desiredPatterns.map {
            it to isPossible(it)
        }
        return possiblePatterns.filter { it.second }.map { it.first }
    }

    fun howManyWaysYouCanMakeEachPattern(): Long {
        val available = availablePatterns.toSet()
        val waysToMakeCache = HashMap<String, Long>()

        fun countPossible(desired: String): Long {
            waysToMakeCache[desired]?.let { return it }

            var possibleWaysToMake = if (desired in available) 1L else 0L

            val maxLengthToCheck = maxAvailableLength.coerceAtMost(desired.length - 1)
            for (l in (1..maxLengthToCheck).reversed()) {
                val head = desired.take(l)
                if (head !in available) continue
                val tail = desired.drop(l)

                possibleWaysToMake += countPossible(tail)
            }

            waysToMakeCache[desired] = possibleWaysToMake

            return possibleWaysToMake
        }

        val waysToMake = possiblePatterns.map {
            it to countPossible(it)
        }
        return waysToMake.sumOf { it.second }.toLong()
    }

    companion object {

        fun parse(content: String): Day19 {
            val (available, desired) = content.split("\n\n", limit = 2)
                .map { it.split("[^a-z]+".toRegex()).filter { it.isNotBlank() } }

            return Day19(available, desired)
        }
    }

}
