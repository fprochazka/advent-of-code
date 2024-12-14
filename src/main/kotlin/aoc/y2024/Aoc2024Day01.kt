package aoc.y2024

import aoc.utils.Resource
import aoc.utils.strings.toInts
import kotlin.math.absoluteValue

fun Resource.day01(): Day01 {
    val leftNumbers = mutableListOf<Int>();
    val rightNumbers = mutableListOf<Int>();

    nonBlankLines().forEach { line ->
        val (left, right) = line.toInts(2)
        leftNumbers.add(left)
        rightNumbers.add(right)
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
