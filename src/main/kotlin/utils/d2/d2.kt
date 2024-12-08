package utils.d2

class Matrix<V : Any>(val cells: Map<Position, V>) {

    val maxX: Int = cells.keys.maxOf { it.x }
    val maxY: Int = cells.keys.maxOf { it.y }

    val positionsByValue: Map<V, Set<Position>> by lazy {
        cells.entries
            .groupBy({ it.value }, { it.key })
            .mapValues { entry -> entry.value.toSet() }
    }

    val uniqueValues by lazy {
        positionsByValue.keys.toSet()
    }

    fun allPositionsOfValue(value: V): Set<Position> =
        positionsByValue[value] ?: emptySet()

    fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        positionsByValue.filterKeys(valueFilter)

    fun allPositions(): Sequence<Position> = sequence {
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                yield(Position(x, y))
            }
        }
    }

    operator fun get(position: Position): V? {
        return cells[position]
    }

    operator fun contains(position: Position): Boolean {
        return position in cells
    }

    override fun toString(): String =
        allPositions()
            .map { "${cells[it]}" + (if (it.x == maxX) "\n" else "") }
            .joinToString("")

}

enum class Direction(val vector: Position) {
    RIGHT(Position(1, 0)),
    LEFT(Position(-1, 0)),
    UP(Position(0, -1)),
    DOWN(Position(0, 1)),

    RIGHT_UP(Position(1, -1)),
    RIGHT_DOWN(Position(1, 1)),
    LEFT_UP(Position(-1, -1)),
    LEFT_DOWN(Position(-1, 1)),
}

data class Position(val x: Int, val y: Int) {

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

    override fun toString(): String = "(x=$xDiff, y=$yDiff)"

}

operator fun Position.plus(other: Position): Position =
    Position(this.x + other.x, this.y + other.y)

operator fun Position.plus(other: Direction): Position =
    this + other.vector

operator fun Position.plus(other: Distance): Position =
    Position(this.x + other.xDiff, this.y + other.yDiff)

fun Position.distanceTo(other: Position): Distance =
    Distance((x - other.x), (y - other.y))

fun Position.vectorInDirection(direction: Direction, length: Int): List<Position> =
    (1 until length).scan(this) { pos, _ -> pos + direction }

data class OrientedPosition(var position: Position, var direction: Direction) {

    override fun toString(): String = "$position -> $direction"
}

fun OrientedPosition.step(count: Int = 1): OrientedPosition =
    OrientedPosition(
        position.vectorInDirection(direction, count + 1).last(),
        direction
    )
