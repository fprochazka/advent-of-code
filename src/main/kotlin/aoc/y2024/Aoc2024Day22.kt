package aoc.y2024

import aoc.utils.Resource

fun Resource.day22(): Day22 = Day22(
    nonBlankLines().map { it.trim().toLong() }
)

data class Day22(val firstSecretNumbers: List<Long>) {

    val result1 by lazy { sumOfThe2000ThSecretNumberGeneratedByEachBuyer(firstSecretNumbers) }
//    val result2: String by lazy { TODO() }

    // 1st and 2000th numbers in example:
    //
    // 1: 8685429
    // 10: 4700978
    // 100: 15273692
    // 2024: 8667524

    fun sumOfThe2000ThSecretNumberGeneratedByEachBuyer(iteration0: List<Long>): Long {
        var numbers = iteration0
        for (i in 1..2000) {
            numbers = when (i % 3) {
                1 -> numbers.map { evolve1(it) }
                2 -> numbers.map { evolve2(it) }
                0 -> numbers.map { evolve3(it) }
                else -> error("Invalid i")
            }
        }

        iteration0.withIndex().forEach { (index, originalNumber) -> println("$originalNumber: ${numbers[index]}") }

        return numbers.sum()
    }

    // secret number evolves:
    // - Calculate the result of multiplying the secret number by 64. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of multiplying the secret number by 2048. Then, mix this result into the secret number. Finally, prune the secret number.
    fun evolve1(secretNumber: Long): Long {
        val result = secretNumber * 64
        return pruneNumber(mixNumber(secretNumber, result))
    }

    fun evolve2(secretNumber: Long): Long {
        val result = secretNumber / 32
        return pruneNumber(mixNumber(secretNumber, result))
    }

    fun evolve3(secretNumber: Long): Long {
        val result = secretNumber * 2048
        return pruneNumber(mixNumber(secretNumber, result))
    }

    // To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number.
    // Then, the secret number becomes the result of that operation.
    // (If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)
    fun mixNumber(secretNumber: Long, mixin: Long): Long {
        return secretNumber xor mixin
    }

    // To prune the secret number, calculate the value of the secret number modulo 16777216.
    // Then, the secret number becomes the result of that operation.
    // (If the secret number is 100000000 and you were to prune the secret number, the secret number would become 16113920.)
    fun pruneNumber(secretNumber: Long): Long {
        return secretNumber % 16777216L
    }


}
