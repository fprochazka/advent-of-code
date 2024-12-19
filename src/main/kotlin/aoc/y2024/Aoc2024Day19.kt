package aoc.y2024

import aoc.utils.Resource

fun Resource.day19(): Day19 = Day19.parse(content())

data class Day19(
    val availablePatterns: Set<String>,
    val desiredPatterns: List<String>
) {

    val maxAvailableLength by lazy { availablePatterns.maxOf { it.length } }

    val result1 by lazy {
        possiblePatterns()
    }
    val result2 by lazy {
        howManyWaysYouCanMakeEachPattern()
    }

    fun possiblePatterns(): Int {
        val possible = HashMap<String, Boolean>(desiredPatterns.size * 4).apply {
            put("", true)
            availablePatterns.forEach { put(it, true) }
        }

        fun isPossible(desired: String): Boolean {
            if (possible.contains(desired)) return true

            val maxLengthToCheck = minOf(maxAvailableLength, desired.length - 1)
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
        return possiblePatterns.count { it.second }
    }

    fun howManyWaysYouCanMakeEachPattern(): Long {
        val waysToMakeCache = HashMap<String, Long>(desiredPatterns.size * 4)

        fun countPossible(desired: String): Long {
            waysToMakeCache[desired]?.let { return it }

            var possibleWaysToMake = if (desired in availablePatterns) 1L else 0L

            val maxLengthToCheck = minOf(maxAvailableLength, desired.length - 1)
            for (l in (1..maxLengthToCheck).reversed()) {
                val head = desired.take(l)
                if (head !in availablePatterns) continue
                val tail = desired.drop(l)

                possibleWaysToMake += countPossible(tail)
            }

            waysToMakeCache[desired] = possibleWaysToMake

            return possibleWaysToMake
        }

        val waysToMake = desiredPatterns.map {
            it to countPossible(it)
        }

        return waysToMake.sumOf { it.second }
    }

    companion object {

        fun parse(content: String): Day19 {
            val (available, desired) = content.split("\n\n", limit = 2)
                .map { it.split("[^a-z]+".toRegex()).filter { it.isNotBlank() } }

            return Day19(available.toSet(), desired)
        }
    }

}
