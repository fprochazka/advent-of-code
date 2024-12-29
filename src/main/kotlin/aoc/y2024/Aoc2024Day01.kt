package aoc.y2024

import aoc.utils.Resource
import aoc.utils.strings.toInts
import kotlin.math.absoluteValue

fun Resource.day01(): Day01 = Day01.parse(nonBlankLines())

class Day01(
    val leftNumbers: IntArray,
    val rightNumbers: IntArray,
) {

    val distances: List<Int> by lazy {
        leftNumbers.zip(rightNumbers)
            .map { (left, right) -> (left - right).absoluteValue }
    }

    val result1 by lazy { distances.sum() }

    val rightNumbersOccurrences by lazy {
        rightNumbers.toList().groupingBy { it }.eachCount()
    }

    val similarities by lazy {
        leftNumbers.map { left -> left * (rightNumbersOccurrences[left] ?: 0) }
    }

    val result2 by lazy { similarities.sum() }

    companion object {

        fun parse(lines: List<String>): Day01 {
            val leftNumbers = IntArray(lines.size);
            val rightNumbers = IntArray(lines.size);

            for ((i, line) in lines.withIndex()) {
                val (left, right) = line.toInts(2)
                leftNumbers[i] = left
                rightNumbers[i] = right
            }

            leftNumbers.sort()
            rightNumbers.sort()

            return Day01(leftNumbers, rightNumbers)
        }

    }

}
