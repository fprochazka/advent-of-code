package aoc2024

import kotlin.math.absoluteValue

fun main() {
    val inputStream = object {}.javaClass.getResourceAsStream("/aoc2024/day01/input.txt")!!
    val lines = inputStream.bufferedReader().use { it.readText() }
        .split("\n")
        .filter { it.isNotBlank() }

    val leftNumbers = mutableListOf<Int>();
    val rightNumbers = mutableListOf<Int>();

    lines.forEach { line ->
        val (left, right) = line.trim().split("\\s+".toRegex(), limit = 2)
        leftNumbers.add(left.toInt())
        rightNumbers.add(right.toInt())
    }

    leftNumbers.sort()
    rightNumbers.sort()

    println(leftNumbers.toString())
    println(rightNumbers.toString())

    val paired = leftNumbers.zip(rightNumbers)

    val distances = paired.map { (left, right) -> (left - right).absoluteValue }

    println(distances.toString())

    println("total: ${distances.sum()}")

    val rightNumbersOccurrences = rightNumbers.groupingBy { it }.eachCount()
    println(rightNumbersOccurrences.toString())

    val similarities = leftNumbers.map { left -> left * (rightNumbersOccurrences[left] ?: 0) }
    println(similarities.toString())

    println("similarity score: ${similarities.sum()}")
}
