package aoc.y2024

import aoc.utils.Resource

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
        // If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
        // If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
        //       The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone.
        //       (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        // If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.

        // [(number, iterations) => parts]
        val cache = mutableMapOf<Pair<Long, Int>, Long>()
        fun countAfterExpansion(number: Long, remaining: Int): Long =
            cache.getOrPut(number to remaining) {
                when {
                    remaining == 0 -> 1
                    number == 0L -> countAfterExpansion(1L, remaining - 1)
                    number.toString().length % 2 == 0 -> number.halfDigits().let { (left, right) -> countAfterExpansion(left, remaining - 1) + countAfterExpansion(right, remaining - 1) }
                    else -> countAfterExpansion(number * 2024, remaining - 1)
                }
            }

        return numbers.sumOf { countAfterExpansion(it, iterations) }
    }

    fun Long.halfDigits(): Pair<Long, Long> =
        toString().half()
            .let { (left, right) -> left.toLong() to right.toLong() }

    fun String.half(): Pair<String, String> =
        (length / 2)
            .let { halfLength -> substring(0, halfLength) to substring(halfLength, length) }

}
