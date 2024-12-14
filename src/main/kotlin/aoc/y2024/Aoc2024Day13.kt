package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Distance
import aoc.utils.d2.Position
import aoc.utils.strings.matchEntire

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
            .map { it.copy(prize = Position(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L)) }
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
            machine.prize == Position(a * aX + b * bX, a * aY + b * bY) -> a * 3 + b
            else -> 0L
        }
    }

    data class SlotMachine(val a: Distance, val b: Distance, val prize: Position)

    companion object {

        fun parseSlotMachine(input: List<String>): SlotMachine =
            SlotMachine(
                parseButton(input[0]),
                parseButton(input[1]),
                parsePrize(input[2]),
            )

        fun parseButton(input: String): Distance =
            input.trim().matchEntire(buttonPattern) { Distance(it.groupValues[1], it.groupValues[2]) }

        fun parsePrize(input: String): Position =
            input.trim().matchEntire(prizePattern) { Position(it.groupValues[1], it.groupValues[2]) }

        val buttonPattern = "Button [AB]: X\\+(\\d+), Y\\+(\\d+)".toRegex()
        val prizePattern = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

    }

}
