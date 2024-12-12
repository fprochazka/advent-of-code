package aoc.utils.d2

open class Matrix<V : Any> protected constructor(
    val width: Int,
    val height: Int
) {

    val maxX = width - 1
    val maxY = height - 1

    private val matrix: MutableList<V?> = MutableList(width * height) { null }

    open fun allPositionsOfValue(value: V): Set<Position> =
        matrix.withIndex()
            .filter { it.value == value }
            .map { indexToPosition(it.index) }
            .toSet()

    open fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        matrix.withIndex()
            .filter { (_, value) -> value?.let(valueFilter) == true }
            .groupBy({ it.value!! }, { it.index })
            .mapValues { (_, indices) -> indices.map { indexToPosition(it) }.toSet() }

    fun allEntries(): Sequence<Pair<Position, V>> =
        allPositions().map { it to this[it]!! }

    fun allPositions(): Sequence<Position> =
        allPositions(maxY, maxX)

    fun putAll(other: Matrix<V>) =
        other.allEntries().forEach { (position, value) -> this[position] = value }

    fun putAll(cells: Map<Position, V>) =
        cells.forEach { (position, value) -> this[position] = value }

    open operator fun set(position: Position, value: V) {
        require(position in this) { "$position is not in matrix($width x $height)" }
        matrix[position.matrixIndex()] = value
    }

    operator fun get(position: Position): V? =
        if (position in this) matrix[position.matrixIndex()] else null

    operator fun contains(position: Position): Boolean =
        position.x > -1 && position.y > -1
          && position.x <= maxX && position.y <= maxY

    fun copy(): Matrix<V> =
        Matrix<V>(width, height).also {
            it.matrix.clear()
            it.matrix.addAll(this.matrix)
        }

    fun withValuesIndex(): WithValuesIndex<V> =
        WithValuesIndex<V>(width, height).apply { putAll(this@apply) }

    override fun toString(): String =
        allPositions()
            .map { "${this[it]}" + (if (it.x == maxX) "\n" else "") }
            .joinToString("")

    fun Position.matrixIndex(): Int = y * width + x

    protected fun indexToPosition(index: Int): Position = Position(index % width, index / width)

    companion object {

        fun <V : Any> empty(template: Matrix<*>): Matrix<V> =
            Matrix<V>(template.width, template.height)

        fun <V : Any> empty(width: Int, height: Int): Matrix<V> =
            Matrix<V>(width, height)

        fun <V : Any> from(cells: Map<Position, V>): Matrix<V> =
            Matrix<V>(cells.keys.maxOf { it.x } + 1, cells.keys.maxOf { it.y } + 1)
                .apply { putAll(cells) }

        private fun allPositions(maxY: Int, maxX: Int): Sequence<Position> = sequence {
            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    yield(Position(x, y))
                }
            }
        }

    }

    class WithValuesIndex<V : Any> internal constructor(width: Int, height: Int) : Matrix<V>(width, height) {

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
data class Position(val x: Int, val y: Int) {

    operator fun plus(other: Position): Position =
        Position(this.x + other.x, this.y + other.y)

    operator fun plus(other: Direction): Position =
        this.plus(other.vector)

    operator fun plus(other: Distance): Position =
        Position(this.x + other.xDiff, this.y + other.yDiff)

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
            Comparator.comparing(Position::x, Int::compareTo)
                .thenComparing(Position::y, Int::compareTo)

        fun comparatorYThenX(): Comparator<Position> =
            Comparator.comparing(Position::y, Int::compareTo)
                .thenComparing(Position::x, Int::compareTo)

    }

}

data class Distance(val xDiff: Int, val yDiff: Int) {

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
