package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.middle
import aoc.utils.strings.toInts

fun Resource.day05(): Day05 {
    val rules = mutableSetOf<Pair<Int, Int>>()
    val updates = mutableListOf<List<Int>>()

    nonBlankLines().forEach { line ->
        if (line.contains("|")) {
            rules.add(line.toInts(2).let { it[0] to it[1] })
        } else {
            updates.add(line.toInts())
        }
    }

    return Day05(rules, updates)
}

data class Day05(
    val rules: Set<Pair<Int, Int>>,
    val updates: List<List<Int>>
) {

    val updatesInTheRightOrder by lazy {
        updates
            .filter { conformsToRules(it) }
    }

    val result1: Int by lazy {
        updatesInTheRightOrder.sumOf { it.middle() }
    }

    val incorrectlyOrderedUpdatesAfterFixing: List<List<Int>> by lazy {
        updates
            .filter { !conformsToRules(it) }
            .map { fixUpdateOrdering(it) }
    }

    val result2: Int by lazy {
        incorrectlyOrderedUpdatesAfterFixing.sumOf { it.middle() }
    }

    fun conformsToRules(update: List<Int>): Boolean {
        for ((i, j) in update.uniqueOrderedIndexPairs()) {
            val reversed = update[j] to update[i]
            if (reversed in rules) return false
        }
        return true
    }

    fun fixUpdateOrdering(update: List<Int>): List<Int> {
        var fixedUpdate = update.toMutableList()

        var changes = true
        while (changes) {
            changes = false

            for ((i, j) in update.uniqueOrderedIndexPairs()) {
                val reversed = fixedUpdate[j] to fixedUpdate[i]
                if (reversed in rules) {
                    fixedUpdate[i] = reversed.first
                    fixedUpdate[j] = reversed.second
                    changes = true
                }
            }
        }

        return fixedUpdate
    }

    fun <T> List<T>.uniqueOrderedIndexPairs(): Sequence<Pair<Int, Int>> {
        val list = this
        return sequence {
            for (i in list.indices) {
                for (j in i + 1 until list.size) {
                    yield(i to j)
                }
            }
        }
    }

}
