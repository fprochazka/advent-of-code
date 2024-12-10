package aoc.y2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day05/example1.txt"))
    solve(Resource.named("aoc2024/day05/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day05()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day05(): Day05 {
    val rules = mutableSetOf<Pair<Int, Int>>()
    val updates = mutableListOf<List<Int>>()

    nonBlankLines().forEach { line ->
        if (line.contains("|")) {
            rules.add(line.split("|", limit = 2).let { it[0].toInt() to it[1].toInt() })
        } else {
            updates.add(line.split(",").map { it.toInt() })
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

}

fun <T> List<T>.middle(): T = this[this.size / 2]

operator fun <T> List<T>.plus(other: Pair<T, T>): List<T> = this + other.toList()

operator fun <T> Pair<T, T>.plus(other: List<T>): List<T> = this.toList() + other

fun <A, B> Pair<A, B>.reversed(): Pair<B, A> = second to first

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
