package aoc.y2024

import aoc.utils.Resource
import org.apache.commons.collections4.queue.CircularFifoQueue

fun Resource.day22(): Day22 = Day22(
    nonBlankLines().map { it.trim().toLong() }
)

data class Day22(val firstSecretNumbers: List<Long>) {

    val result1 by lazy { sumOfThe2000ThSecretNumberGeneratedByEachBuyer(firstSecretNumbers) }
    val result2 by lazy { whatIsTheMostBananasWeCanBuyByUsingAnOptimalFourNumberSequence(firstSecretNumbers) }

    // from example:
    //      123: 3
    // 15887950: 0 (-3)
    // 16495136: 6 (6)
    //   527345: 5 (-1)
    //   704524: 4 (-1)
    //  1553684: 4 (0)
    // 12683156: 6 (2)
    // 11100544: 4 (-2)
    // 12249484: 4 (0)
    //  7753432: 2 (-2)

    // four consecutive changes
    // If the monkey never hears that sequence of price changes from a buyer, the monkey will never sell, and will instead just move on to the next buyer
    // you can only give the monkey a single sequence of four price changes to look for. You can't change the sequence between buyers.
    fun whatIsTheMostBananasWeCanBuyByUsingAnOptimalFourNumberSequence(iteration0: List<Long>): Long {
        val previousDigits = MutableList(iteration0.size) { (iteration0[it] % 10).toInt() }
        val sequences = List(iteration0.size) { CircularFifoQueue<Int>(4) }

        // sequence => [buyer => price]
        val bananasPerSequence = mutableMapOf<String, MutableMap<Int, Int>>()

        val numbers = iteration0.toMutableList()
        for (iter in 1..2000) {
            for (monkeyId in numbers.indices) {
                val evolvedNumber = evolve(numbers[monkeyId])
                val evolvedNumberLastDigit = (evolvedNumber % 10).toInt()

                val previousLastDigit = previousDigits[monkeyId]
                val diff = evolvedNumberLastDigit - previousLastDigit

                sequences[monkeyId].add(diff)
                previousDigits[monkeyId] = evolvedNumberLastDigit
                numbers[monkeyId] = evolvedNumber

                if (iter >= 4) { // at least 4 iterations to get first 4 diffs
                    val bananasToSell = evolvedNumberLastDigit

                    val fourNumbersSequence = sequences[monkeyId].joinToString(",")
                    val buyersToPrices = bananasPerSequence.getOrPut(fourNumbersSequence) { mutableMapOf() }
                    buyersToPrices.putIfAbsent(monkeyId, bananasToSell)
                }
            }
        }

        val sumOfBananasPerSequence = bananasPerSequence.mapValues { it.value.values.sum().toLong() }
        val maxBuyableBananas = sumOfBananasPerSequence.values.max()

//        for ((seq, buyers) in bananasPerSequence) {
//            println("${seq}: ${buyers.entries.sortedBy { it.key }}")
//        }

        return maxBuyableBananas
    }

    // 1st and 2000th numbers in example:
    //
    // 1: 8685429
    // 10: 4700978
    // 100: 15273692
    // 2024: 8667524
    fun sumOfThe2000ThSecretNumberGeneratedByEachBuyer(iteration0: List<Long>): Long {
        var numbers = iteration0
        for (i in 1..2000) {
            numbers = numbers.map { evolve(it) }
        }

        // iteration0.withIndex().forEach { (index, originalNumber) -> println("$originalNumber: ${numbers[index]}") }

        return numbers.sum()
    }

    // secret number evolves:
    // - Calculate the result of multiplying the secret number by 64. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of multiplying the secret number by 2048. Then, mix this result into the secret number. Finally, prune the secret number.
    fun evolve(secretNumber: Long): Long {
        return evolve3(evolve2(evolve1(secretNumber)))
    }

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
