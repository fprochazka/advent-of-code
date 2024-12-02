package aoc2024

import utils.Resource
import kotlin.math.absoluteValue

fun main() {
    solve(Resource.named("aoc2024/day01/example.txt"))
    solve(Resource.named("aoc2024/day01/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = Day1.from(input).sorted()

    input.assertResult("task1") { problem.distancesSum }
    input.assertResult("task2") { problem.similaritiesSum }
}

data class Day1(
    val leftNumbers: List<Int>,
    val rightNumbers: List<Int>,
) {

    val distances: List<Int> by lazy {
        leftNumbers.zip(rightNumbers)
            .map { (left, right) -> (left - right).absoluteValue }
    }

    val distancesSum by lazy { distances.sum() }

    val rightNumbersOccurrences by lazy {
        rightNumbers.groupingBy { it }.eachCount()
    }

    val similarities by lazy {
        leftNumbers.map { left -> left * (rightNumbersOccurrences[left] ?: 0) }
    }

    val similaritiesSum by lazy { similarities.sum() }

    fun sorted(): Day1 = Day1(leftNumbers.sorted(), rightNumbers.sorted())

    companion object {

        fun from(resource: Resource): Day1 {
            val leftNumbers = mutableListOf<Int>();
            val rightNumbers = mutableListOf<Int>();

            resource.nonBlankLines().forEach { line ->
                val (left, right) = line.trim().split("\\s+".toRegex(), limit = 2)
                leftNumbers.add(left.toInt())
                rightNumbers.add(right.toInt())
            }

            return Day1(leftNumbers, rightNumbers)
        }

    }

}
