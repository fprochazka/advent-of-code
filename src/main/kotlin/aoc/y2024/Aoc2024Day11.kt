package aoc.y2024

import aoc.utils.Resource
import aoc.utils.numbers.digitHalfs
import aoc.utils.strings.toLongs

fun Resource.day11(): Day11 = Day11(
    content().toLongs()
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
        val cache = HashMap<Pair<Long, Int>, Long>()
        fun countAfterExpansion(number: Long, remaining: Int): Long =
            cache.getOrPut(number to remaining) {
                when {
                    remaining == 0 -> 1
                    number == 0L -> countAfterExpansion(1L, remaining - 1)
                    number.toString().length % 2 == 0 -> number.digitHalfs().let { (left, right) -> countAfterExpansion(left, remaining - 1) + countAfterExpansion(right, remaining - 1) }
                    else -> countAfterExpansion(number * 2024, remaining - 1)
                }
            }

        return numbers.sumOf { countAfterExpansion(it, iterations) }
    }

}
