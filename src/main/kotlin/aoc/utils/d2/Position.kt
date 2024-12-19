package aoc.utils.d2

import aoc.utils.math.remEuclid
import kotlin.math.abs

/**
 * zero indexed
 */
data class Position(val x: Long, val y: Long, private val areaDims: AreaDimensions? = null) {

    constructor(x: Int, y: Int, areaDims: AreaDimensions? = null) : this(x.toLong(), y.toLong(), areaDims)

    constructor(x: String, y: String, areaDims: AreaDimensions? = null) : this(x.toLong(), y.toLong(), areaDims)

    operator fun plus(other: Position): Position =
        areaDims.positionFlyweight(x + other.x, y + other.y)

    operator fun plus(other: Direction): Position =
        this.plus(other.vector)

    operator fun plus(other: Distance): Position =
        areaDims.positionFlyweight(x + other.xDiff, y + other.yDiff)

    /**
     * Fits the position into given dimensions which represent a (w * h) matrix
     */
    operator fun rem(dims: AreaDimensions): Position =
        dims.positionFlyweight(
            x.remEuclid(dims.w),
            y.remEuclid(dims.h)
        )

    fun relativeDirectionTo(other: Position): Direction? =
        other.distanceTo(this).asDirection()

    fun distanceTo(other: Position): Distance =
        Distance(this.x - other.x, this.y - other.y)

    fun vectorInDirection(direction: Direction, length: Int): List<Position> =
        List(length) { if (it == 0) this else stepInDirection(direction, it) }

    fun stepInDirection(direction: Direction, length: Int): Position = when (length) {
        1 -> this.plus(direction)
        else -> areaDims.positionFlyweight(
            this.x + (direction.vector.xDiff * length),
            this.y + (direction.vector.yDiff * length)
        )
    }

    fun manhattanDistanceTo(end: Position): Long {
        val dx = abs(this.x - end.x)
        val dy = abs(this.y - end.y)
        return dx + dy
    }

    val left: Position
        get() = plus(Direction.LEFT)

    val right: Position
        get() = plus(Direction.RIGHT)

    val up: Position
        get() = plus(Direction.UP)

    val down: Position
        get() = plus(Direction.DOWN)

    override fun toString(): String = "(x=$x, y=$y)"

    companion object {

        fun comparatorXThenY(): Comparator<Position> =
            Comparator.comparing(Position::x, Long::compareTo)
                .thenComparing(Position::y, Long::compareTo)

        fun comparatorYThenX(): Comparator<Position> =
            Comparator.comparing(Position::y, Long::compareTo)
                .thenComparing(Position::x, Long::compareTo)

    }

}
