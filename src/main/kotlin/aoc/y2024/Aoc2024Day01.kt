package aoc.y2024

import utils.Resource
import kotlin.math.absoluteValue

fun main() {
    solve(Resource.named("aoc2024/day01/example.txt"))
    solve(Resource.named("aoc2024/day01/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day01()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day01(): Day01 {
    val leftNumbers = mutableListOf<Int>();
    val rightNumbers = mutableListOf<Int>();

    nonBlankLines().forEach { line ->
        val (left, right) = line.trim().split("\\s+".toRegex(), limit = 2)
        leftNumbers.add(left.toInt())
        rightNumbers.add(right.toInt())
    }

    return Day01(leftNumbers.sorted(), rightNumbers.sorted())
}

data class Day01(
    val leftNumbers: List<Int>,
    val rightNumbers: List<Int>,
) {

    val distances: List<Int> by lazy {
        leftNumbers.zip(rightNumbers)
            .map { (left, right) -> (left - right).absoluteValue }
    }

    val result1 by lazy { distances.sum() }

    val rightNumbersOccurrences by lazy {
        rightNumbers.groupingBy { it }.eachCount()
    }

    val similarities by lazy {
        leftNumbers.map { left -> left * (rightNumbersOccurrences[left] ?: 0) }
    }

    val result2 by lazy { similarities.sum() }

}
