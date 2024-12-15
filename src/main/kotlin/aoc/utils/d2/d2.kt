package aoc.utils.d2

import aoc.utils.Resource
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.extension

open class Matrix<V : Any> protected constructor(
    val dims: Dimensions,
) {

    private val matrix: MutableList<V?> = MutableList(Math.toIntExact(dims.area)) { null }

    open fun allPositionsOfValue(value: V): Set<Position> =
        matrix.withIndex()
            .filter { it.value == value }
            .map { dims.matrixIndexToPosition(it.index) }
            .toSet()

    open fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        matrix.withIndex()
            .filter { (_, value) -> value?.let(valueFilter) == true }
            .groupBy({ it.value!! }, { it.index })
            .mapValues { (_, indices) -> indices.map { dims.matrixIndexToPosition(it) }.toSet() }

    val entries: Sequence<Pair<Position, V>>
        get() = positions.map { it to this[it]!! }

    fun entriesInDirection(startInclusive: Position, direction: Direction): Sequence<Pair<Position, V>> =
        generateSequence(startInclusive) { it + direction }
            .takeWhile { it in this } // only for coordinates within matrix
            .map { it to this[it]!! }

    val positions: Sequence<Position>
        get() = dims.matrixPositions

    fun putAll(other: Matrix<V>) =
        other.entries.forEach { (position, value) -> this[position] = value }

    open operator fun set(position: Position, value: V) {
        require(position in this) { "$position is not in matrix${dims}" }
        matrix[dims.matrixIndex(position)] = value
    }

    operator fun get(position: Position): V? =
        if (position in this) matrix[dims.matrixIndex(position)] else null

    operator fun contains(position: Position): Boolean =
        position.x > -1 && position.y > -1
          && position.x <= dims.maxX && position.y <= dims.maxY

    fun copy(): Matrix<V> =
        Matrix<V>(dims).also {
            it.matrix.clear()
            it.matrix.addAll(this.matrix)
        }

    fun draw(file: Path, valueToPixel: (V) -> java.awt.Color) {
        ImageIO.write(draw(valueToPixel), file.extension.lowercase(), file.toFile())
    }

    fun draw(valueToPixel: (V) -> java.awt.Color): BufferedImage {
        val image = BufferedImage(dims.w.toInt(), dims.h.toInt(), BufferedImage.TYPE_INT_ARGB)

        entries.forEach { (pos, value) ->
            image.setRGB(pos.x.toInt(), pos.y.toInt(), valueToPixel(value).rgb)
        }

        return image
    }

    fun withValuesIndex(): WithValuesIndex<V> =
        WithValuesIndex<V>(dims).apply { putAll(this@apply) }

    override fun toString(): String =
        positions
            .map { "${this[it]}" + (if (it.x == dims.maxX) "\n" else "") }
            .joinToString("")

    companion object {

        fun <V : Any> empty(dims: Dimensions): Matrix<V> =
            Matrix<V>(dims)

        fun <V : Any> of(dims: Dimensions, initialValue: () -> V): Matrix<V> =
            Matrix<V>(dims).apply {
                positions.forEach { this[it] = initialValue() }
            }

        fun ofChars(cells: Resource.CharMatrix2d): Matrix<Char> =
            of(cells, { it })

        fun ofInts(cells: Resource.CharMatrix2d): Matrix<Int> =
            of(cells, { it.digitToInt() })

        fun <V : Any> of(cells: Resource.CharMatrix2d, toValue: (Char) -> V): Matrix<V> =
            Matrix<V>(cells.dims).apply {
                cells.entries.forEach { (pos, char) -> this[pos] = toValue(char) }
            }

    }

    class WithValuesIndex<V : Any> internal constructor(dims: Dimensions) : Matrix<V>(dims) {

        private val positionsByValue: MutableMap<V, MutableSet<Position>> = mutableMapOf()

        val uniqueValues: Set<V>
            get() = positionsByValue.keys.toSet()

        override fun allPositionsOfValue(value: V): Set<Position> =
            positionsByValue[value] ?: emptySet()

        override fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
            positionsByValue.filterKeys(valueFilter)

        override fun set(position: Position, value: V) {
            val oldValue = this[position]

            super.set(position, value)

            positionsByValue[oldValue]?.remove(position)
            positionsByValue.getOrPut(value) { mutableSetOf() }.add(position)
        }
    }
}

enum class Direction(val vector: Distance) {
    RIGHT(Distance(1, 0)),
    LEFT(Distance(-1, 0)),
    UP(Distance(0, -1)),
    DOWN(Distance(0, 1)),

    RIGHT_UP(Distance(1, -1)),
    RIGHT_DOWN(Distance(1, 1)),
    LEFT_UP(Distance(-1, -1)),
    LEFT_DOWN(Distance(-1, 1)),
    ;

    fun isHorizontal(): Boolean = when (this) {
        RIGHT, LEFT -> true
        else -> false
    }

    fun isVertical(): Boolean = when (this) {
        UP, DOWN -> true
        else -> false
    }

    fun isDiagonal(): Boolean = when (this) {
        RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN -> true
        else -> false
    }

    fun turnRight90(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP

        RIGHT_UP -> RIGHT_DOWN
        RIGHT_DOWN -> LEFT_DOWN
        LEFT_DOWN -> LEFT_UP
        LEFT_UP -> RIGHT_UP
    }

    fun turnLeft90(): Direction = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP

        RIGHT_UP -> LEFT_UP
        LEFT_UP -> LEFT_DOWN
        LEFT_DOWN -> RIGHT_DOWN
        RIGHT_DOWN -> RIGHT_UP
    }

    fun turnRight45(): Direction = when (this) {
        UP -> RIGHT_UP
        RIGHT_UP -> RIGHT
        RIGHT -> RIGHT_DOWN
        RIGHT_DOWN -> DOWN
        DOWN -> LEFT_DOWN
        LEFT_DOWN -> LEFT
        LEFT -> LEFT_UP
        LEFT_UP -> UP
    }

    fun turnLeft45(): Direction = when (this) {
        UP -> LEFT_UP
        LEFT_UP -> LEFT
        LEFT -> LEFT_DOWN
        LEFT_DOWN -> DOWN
        DOWN -> RIGHT_DOWN
        RIGHT_DOWN -> RIGHT
        RIGHT -> RIGHT_UP
        RIGHT_UP -> UP
    }

}

/**
 * zero indexed
 */
data class Position(val x: Long, val y: Long) {

    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    constructor(x: String, y: String) : this(x.toLong(), y.toLong())

    operator fun plus(other: Position): Position =
        Position(this.x + other.x, this.y + other.y)

    operator fun plus(other: Direction): Position =
        this.plus(other.vector)

    operator fun plus(other: Distance): Position =
        Position(this.x + other.xDiff, this.y + other.yDiff)

    /**
     * Fits the position into given dimensions which represent a (w * h) matrix
     */
    operator fun rem(dims: Dimensions): Position {
        fun Long.inDimension(size: Long): Long = ((this % size) + size) % size

        return Position(
            x.inDimension(dims.w),
            y.inDimension(dims.h)
        )
    }

    fun distanceTo(other: Position): Distance =
        Distance(this.x - other.x, this.y - other.y)

    fun vectorInDirection(direction: Direction, length: Int): List<Position> =
        List(length) { if (it == 0) this else stepInDirection(direction, it) }

    fun stepInDirection(direction: Direction, length: Int): Position = when (length) {
        1 -> this.plus(direction)
        else -> Position(this.x + (direction.vector.xDiff * length), this.y + (direction.vector.yDiff * length))
    }

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

data class Dimensions(val w: Long, val h: Long) {

    constructor(w: Int, h: Int) : this(w.toLong(), h.toLong())

    constructor(w: String, h: String) : this(w.toLong(), h.toLong())

    val maxX = (w - 1)
    val maxY = (h - 1)

    val area = w * h

    fun matrixIndex(position: Position): Int =
        Math.toIntExact((position.y * w) + position.x)

    fun matrixIndexToPosition(index: Int): Position =
        matrixIndexToPosition(index.toLong())

    fun matrixIndexToPosition(index: Long): Position =
        Position(index % w, index / w)

    val matrixIndices: Sequence<Long>
        get() = (0L until area).asSequence()

    val matrixPositions: Sequence<Position>
        get() = matrixIndices.map { matrixIndexToPosition(it) }

    override fun toString(): String = "($w x $h)"

}

data class Distance(val xDiff: Long, val yDiff: Long) {

    constructor(xDiff: Int, yDiff: Int) : this(xDiff.toLong(), yDiff.toLong())

    constructor(xDiff: String, yDiff: String) : this(xDiff.toLong(), yDiff.toLong())

    operator fun times(length: Int): Distance =
        Distance(xDiff * length, yDiff * length)

    override fun toString(): String = "(x=$xDiff, y=$yDiff)"

}

data class OrientedPosition(var position: Position, var direction: Direction) {

    fun turnRight90(): OrientedPosition =
        OrientedPosition(position, direction.turnRight90())

    fun turnLeft90(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft90())

    fun turnRight45(): OrientedPosition =
        OrientedPosition(position, direction.turnRight45())

    fun turnLeft45(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft45())

    fun step(count: Int = 1): OrientedPosition =
        OrientedPosition(
            position.stepInDirection(direction, count),
            direction
        )

    override fun toString(): String = "$position -> $direction"

}
