package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.CircularBufferFixedSize4
import aoc.utils.containers.Tuple4
import java.util.*

fun Resource.day22(): Day22 = Day22(
    nonBlankLines().map { it.trim().toLong() }
)

data class Day22(val firstSecretNumbers: List<Long>) {

    val result1 by lazy { sumOfThe2000ThSecretNumberGeneratedByEachBuyer(firstSecretNumbers) }
    val result2 by lazy { whatIsTheMostBananasWeCanBuyByUsingAnOptimalFourNumberSequence(firstSecretNumbers) }

    // four consecutive changes
    // If the monkey never hears that sequence of price changes from a buyer, the monkey will never sell, and will instead just move on to the next buyer
    // you can only give the monkey a single sequence of four price changes to look for. You can't change the sequence between buyers.
    //
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
    fun whatIsTheMostBananasWeCanBuyByUsingAnOptimalFourNumberSequence(iteration0: List<Long>): Int {
        val previousBananasToSell = MutableList(iteration0.size) { (iteration0[it] % 10).toInt() }
        val sequences = List(iteration0.size) { CircularBufferFixedSize4<Int>() }

        val bananasPerSequence = HashMap<Tuple4<Int>, BananasPerMonkey>(1 shl 16, 1.0f) // sequence => [price]

        val numbers = iteration0.toMutableList()
        for (iter in 1..2000) {
            for (monkeyId in numbers.indices) {
                val evolvedNumber = evolve(numbers[monkeyId])
                val bananasToSell = (evolvedNumber % 10).toInt()

                val diff = bananasToSell - previousBananasToSell[monkeyId]

                sequences[monkeyId].add(diff)
                previousBananasToSell[monkeyId] = bananasToSell
                numbers[monkeyId] = evolvedNumber

                if (iter >= 4) { // at least 4 iterations to get first 4 diffs
                    val fourNumbersSequence = sequences[monkeyId].get()
                    bananasPerSequence
                        .getOrPut(fourNumbersSequence) { BananasPerMonkey(numbers.size) }
                        .monkeyWillSell(monkeyId, bananasToSell)
                }
            }
        }

        val maxBananas = bananasPerSequence.map { it.value.sum }.max()

//        for ((seq, buyers) in bananasPerSequence) {
//            println("${seq}: ${buyers}")
//        }

        return maxBananas
    }

    class BananasPerMonkey(monkeys: Int) {

        val buyers = BitSet(monkeys)
        var sum = 0

        fun monkeyWillSell(monkeyId: Int, bananas: Int) {
            if (buyers[monkeyId] == false) {
                sum += bananas
                buyers[monkeyId] = true
            }
        }

        override fun toString(): String = "sum=$sum"

    }

    // 1st and 2000th numbers in example:
    //
    // 1: 8685429
    // 10: 4700978
    // 100: 15273692
    // 2024: 8667524
    fun sumOfThe2000ThSecretNumberGeneratedByEachBuyer(iteration0: List<Long>): Long {
        val numbers = iteration0.toMutableList()
        repeat(2000) {
            // this instead of map() avoids allocation the list 2000 times
            for (i in numbers.indices) {
                numbers[i] = evolve(numbers[i])
            }
        }

        // iteration0.withIndex().forEach { (index, originalNumber) -> println("$originalNumber: ${numbers[index]}") }

        return numbers.sum()
    }

    // secret number evolves:
    // - Calculate the result of multiplying the secret number by 64. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer. Then, mix this result into the secret number. Finally, prune the secret number.
    // - Calculate the result of multiplying the secret number by 2048. Then, mix this result into the secret number. Finally, prune the secret number.
    //
    // To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number.
    //    Then, the secret number becomes the result of that operation.
    //    (If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)
    //
    // To prune the secret number, calculate the value of the secret number modulo 16777216.
    //    Then, the secret number becomes the result of that operation.
    //    (If the secret number is 100000000 and you were to prune the secret number, the secret number would become 16113920.)
    fun evolve(secretNumber: Long): Long {
        val evolve1 = (secretNumber xor (secretNumber * 64)) % 16777216L
        val evolve2 = (evolve1 xor (evolve1 / 32)) % 16777216L
        val evolve3 = (evolve2 xor (evolve2 * 2048)) % 16777216L
        return evolve3
    }

}
