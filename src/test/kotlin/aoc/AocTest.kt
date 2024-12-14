package aoc

import aoc.utils.Resource

open class AocTest {

    fun resource2024(day: Int, name: String): Resource =
        Resource.named("aoc2024/day${day.toString().padStart(2, '0')}/$name.txt")

}
