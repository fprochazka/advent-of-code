package aoc2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day05/example1.txt"))
    solve(Resource.named("aoc2024/day05/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day5()

    input.assertResult("task1") { problem.middleNumbersOfCorrectlyOrderedUpdates }
    input.assertResult("task2") { problem.middleNumbersOfIncorrectlyOrderedUpdatesAfterFixing }
}

fun Resource.day5(): Day5 = Day5(
    nonBlankLines()
        .filter { it.contains("|") }
        .map { it.split("|", limit = 2).let { it[0].toInt() to it[1].toInt() } }
        .toSet(),
    nonBlankLines()
        .filter { !it.contains("|") }
        .map { it.split(",").map { it.toInt() } }
        .toList()
)

data class Day5(
    val rules: Set<Pair<Int, Int>>,
    val updates: List<List<Int>>
) {

    val updatesInTheRightOrder by lazy {
        updates
            .filter { conformsToRules(it) }
    }

    val middleNumbersOfCorrectlyOrderedUpdates: Int by lazy {
        updatesInTheRightOrder.sumOf { it[it.size / 2] }
    }

    val incorrectUpdatesInTheRightOrder: List<List<Int>> by lazy {
        updates
            .filter { !conformsToRules(it) }
            .map { fixUpdateOrdering(it) }
    }

    val middleNumbersOfIncorrectlyOrderedUpdatesAfterFixing: Int by lazy {
        incorrectUpdatesInTheRightOrder.sumOf { it[it.size / 2] }
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
