package aoc.utils.d2

import aoc.utils.d2.Direction.entries
import aoc.utils.math.remEuclid

enum class Direction(val vector: Distance) {
    UP(Distance(0, -1)),
    RIGHT_UP(Distance(1, -1)),
    RIGHT(Distance(1, 0)),
    RIGHT_DOWN(Distance(1, 1)),
    DOWN(Distance(0, 1)),
    LEFT_DOWN(Distance(-1, 1)),
    LEFT(Distance(-1, 0)),
    LEFT_UP(Distance(-1, -1)),
    ;

    fun isHorizontal(): Boolean = when (this) {
        RIGHT, LEFT -> true
        else -> false
    }

    fun isVertical(): Boolean = when (this) {
        UP, DOWN -> true
        else -> false
    }

    fun isCardinal(): Boolean = this in entriesCardinal

    fun isDiagonal(): Boolean = this in entriesDiagonal

    fun turnClockwise(steps: Int = 1): Direction = entriesByOrdinal[(ordinal + steps).remEuclid(size)]

    fun turnCounterClockwise(steps: Int = 1): Direction = entriesByOrdinal[(ordinal - steps).remEuclid(size)]

    fun turnRight90(): Direction = turnClockwise(2)

    fun turnLeft90(): Direction = turnCounterClockwise(2)

    fun turnRight45(): Direction = turnClockwise(1)

    fun turnLeft45(): Direction = turnCounterClockwise(1)

    companion object {

        val size by lazy {
            entries.size
        }

        val entriesDiagonal = setOf(RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN)
        val entriesCardinal = setOf(UP, RIGHT, DOWN, LEFT)

        private val entriesByOrdinal: List<Direction> = entries.toList()

    }

}
