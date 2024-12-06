package utils.d2

data class Matrix<V : Any>(val cells: Map<Position, V>) {

    val maxX: Int = cells.keys.maxOf { it.first }
    val maxY: Int = cells.keys.maxOf { it.second }

    fun allPositionsOfValue(value: V): Sequence<Position> =
        allPositions().filter { cells[it] == value }

    fun allPositions(): Sequence<Position> = sequence {
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                yield(x to y)
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
            .map { "${cells[it]}" + (if (it.first == maxX) "\n" else "") }
            .joinToString("")

}

enum class Direction(val vector: Position) {
    RIGHT(1 to 0),
    LEFT(-1 to 0),
    UP(0 to -1),
    DOWN(0 to 1),

    RIGHT_UP(1 to -1),
    RIGHT_DOWN(1 to 1),
    LEFT_UP(-1 to -1),
    LEFT_DOWN(-1 to 1),
}

typealias Position = Pair<Int, Int>

operator fun Position.plus(other: Position): Position = (this.first + other.first) to (this.second + other.second)

operator fun Position.plus(other: Direction): Position = this + other.vector

fun Position.vectorInDirection(direction: Direction, length: Int): List<Position> = (1 until length).scan(this) { pos, _ -> pos + direction }

data class PositionWithDirection(var position: Position, var direction: Direction)
