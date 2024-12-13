package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Distance

fun main() {
    solve(Resource.named("aoc2024/day13/example1.txt"))
    solve(Resource.named("aoc2024/day13/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day13()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day13(): Day13 = Day13(
    nonBlankLines()
        .chunked(3)
        .map { Day13.parseSlotMachine(it) }
)

data class Day13(val arcades: List<SlotMachine>) {

    val result1 by lazy {
        arcades
            .sumOf<SlotMachine> { howManyMinTokensAreNeededToReachPrizeFast(it) }
    }

    val result2 by lazy {
        arcades
            .map { it.copy(prize = PositionL(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L)) }
            .sumOf<SlotMachine> { howManyMinTokensAreNeededToReachPrizeFast(it) }
    }

    fun howManyMinTokensAreNeededToReachPrizeFast(machine: SlotMachine): Long {
        val (pX, pY) = machine.prize
        val (aX, aY) = machine.a
        val (bX, bY) = machine.b

        // Starting conditions:
        //  pX = (a * aX) + (b * bX)
        //  pY = (a * aY) + (b * bY)
        //  cost = (a * 3) + b
        //  a >= 0
        //  b >= 0

        // Task: search for "a" and "b" and return minimal cost

        // extract a from first equation:
        // pX = (a * aX) + (b * bX)
        // pX - (b * bX) = (a * aX)
        // a = (pX - (b * bX)) / aX
        //
        // extract a from second equation:
        // pY = (a * aY) + (b * bY)
        // pY - (b * bY) = (a * aY)
        // a = (pY - (b * bY)) / aY
        //
        // extract b using a = a:
        // (pX - (b * bX)) / aX = (pY - (b * bY)) / aY
        // (pX - (b * bX)) * aY = (pY - (b * bY)) * aX
        // (pX * aY) - (b * bX * aY) = (pY * aX) - (b * bY * aX)
        // (pX * aY) - (pY * aX) = (b * bX * aY) - (b * bY * aX)
        // (pX * aY) - (pY * aX) = b * ((bX * aY) - (bY * aX))
        // b = ((pX * aY) - (pY * aX)) / ((bX * aY) - (bY * aX))

        val b = ((pX * aY) - (pY * aX)) / ((bX * aY) - (bY * aX))
        val a = (pX - (b * bX)) / aX

        return when {
            a < 0 || b < 0 -> 0L
            machine.prize == PositionL(a * aX + b * bX, a * aY + b * bY) -> a * 3 + b
            else -> 0L
        }
    }

    data class SlotMachine(val a: Distance, val b: Distance, val prize: PositionL)

    data class PositionL(val x: Long, val y: Long) {

        override fun toString(): String = "(x=$x, y=$y)"

    }

    companion object {

        fun parseSlotMachine(input: List<String>): SlotMachine =
            SlotMachine(
                parseButton(input[0]),
                parseButton(input[1]),
                parsePrize(input[2]),
            )

        fun parseButton(input: String): Distance =
            buttonPattern.matchEntire(input.trim())
                ?.let { Distance(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
                ?: error("Bad input: $input")

        fun parsePrize(input: String): PositionL =
            prizePattern.matchEntire(input.trim())
                ?.let { PositionL(it.groupValues[1].toLong(), it.groupValues[2].toLong()) }
                ?: error("Bad input: $input")

        val buttonPattern = "Button [AB]: X\\+(\\d+), Y\\+(\\d+)".toRegex()
        val prizePattern = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

    }

}
