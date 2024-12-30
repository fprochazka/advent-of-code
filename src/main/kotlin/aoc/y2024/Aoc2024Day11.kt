package aoc.y2024

import aoc.utils.Resource
import aoc.utils.math.digitCount
import aoc.utils.numbers.digitHalfs
import aoc.utils.strings.toLongs
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap

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

        // 1 shl 12  = 4096
        // 1 shl 13  = 8192
        // 1 shl 14  = 16384
        // 1 shl 15  = 32768
        // 1 shl 16  = 65536
        // 1 shl 17  = 131072
        // 1 shl 18  = 262144
        // 1 shl 19  = 524288

        // for input:
        // 20 iters => cache size 2187
        // 25 iters => cache size 3865 ... 1.7
        // 30 iters => cache size 6732 ... 1.7
        // 35 iters => cache size 11640 ... 1.7
        // 40 iters => cache size 19151 ... 1.6
        // 45 iters => cache size 29807 ... 1.5
        // 50 iters => cache size 43225 ... 1.4
        // 55 iters => cache size 58769 ... 1.3
        // 60 iters => cache size 75814 ... 1.3
        // 65 iters => cache size 93721 ... 1.2
        // 70 iters => cache size 112183 ... 1.2
        // 75 iters => cache size 130970 ... 1.2
        // 80 iters => cache size 149921 ... 1.1

        // 25 => shl 13, 75 => shl 18
        val initialCapacity = 1 shl (13 + maxOf(0, (iterations - 25) / 10))

        // [(number, iterations) => parts]
        val cache = Long2LongOpenHashMap(initialCapacity, 0.9999999f)
        // println("using cache size capacity=${initialCapacity}")

        fun countAfterExpansion(number: Long, remaining: Long): Long {
            // decimal 100 => 7bits
            val cacheKey = (number shl 7) or remaining

            cache.getOrDefault(cacheKey, -1L).let { if (it != -1L) return it }

            val result = when {
                remaining == 0L -> 1
                number == 0L -> countAfterExpansion(1L, remaining - 1)
                number.digitCount() % 2 == 0 -> number.digitHalfs().let { (left, right) -> countAfterExpansion(left, remaining - 1) + countAfterExpansion(right, remaining - 1) }
                else -> countAfterExpansion(number * 2024, remaining - 1)
            }

            cache.put(cacheKey, result)

            return result
        }

        val sum = numbers.sumOf { countAfterExpansion(it, iterations.toLong()) }

        return sum
    }

}
