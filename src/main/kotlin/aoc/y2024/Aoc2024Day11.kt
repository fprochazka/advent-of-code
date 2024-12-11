package aoc.y2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day11/example1.txt"))
    solve(Resource.named("aoc2024/day11/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day11()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day11(): Day11 = Day11(
    content().trim().split("\\s+".toRegex()).map { it.toLong() }
)

data class Day11(
    val stoneNumbers: List<Long>
) {

    val result1 by lazy {
        countStonesAfterIteration(stoneNumbers, 25)
    }
    val result2 by lazy {
        countStonesAfterIteration(stoneNumbers, 75)
    }

    fun countStonesAfterIteration(numbers: List<Long>, iterations: Int): Long {

        // [(number, iterations) => parts]
        var numberExpandsToItems = mutableMapOf<Pair<Long, Int>, Long>()

        // If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
        // If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
        //       The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone.
        //       (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        // If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
        fun countAfterExpansion(number: Long, remainingIterations: Int): Long {
            if (remainingIterations == 0) return 1
            numberExpandsToItems[number to remainingIterations]?.let { return it }

            val numberDigits = number.toString()
            val result = when {
                number == 0L -> countAfterExpansion(1L, remainingIterations - 1)
                numberDigits.length % 2 == 0 -> {
                    val half = numberDigits.length / 2
                    val left = countAfterExpansion(numberDigits.substring(0, half).toLong(), remainingIterations - 1)
                    val right = countAfterExpansion(numberDigits.substring(half, numberDigits.length).trimStart('0').padStart(1, '0').toLong(), remainingIterations - 1)
                    left + right
                }

                else -> countAfterExpansion(number * 2024, remainingIterations - 1)
            }

            numberExpandsToItems[number to remainingIterations] = result

            return result
        }

        return numbers.sumOf { countAfterExpansion(it, iterations) }
    }

}
