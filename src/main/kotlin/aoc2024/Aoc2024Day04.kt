package aoc2024

import aoc2024.Day4.Matrix.Move
import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day04/example1.txt"))
    solve(Resource.named("aoc2024/day04/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day4()

    input.assertResult("task1") { problem.xmasWordOccurrences }
    input.assertResult("task2") { problem.xmasCrossShapeOccurrences }
}

fun Resource.day4(): Day4 = Day4(
    Day4.Matrix(
        charMatrix()
    )
)

data class Day4(val matrix: Matrix) {

    val xmasWordOccurrences by lazy {
        matrix
            .allWordsOfLength("XMAS".length)
            .count { "XMAS" == it }
    }

    val xmasCrossShapeOccurrences by lazy {
        matrix
            .allPositionsOfChar('A')
            .count { aPos ->
                isCrossShaped(
                    "MS",
                    matrix.getWord(
                        aPos + Move.RIGHT_UP,
                        aPos + Move.LEFT_DOWN,
                    ),
                    matrix.getWord(
                        aPos + Move.RIGHT_DOWN,
                        aPos + Move.LEFT_UP,
                    )
                )
            }
    }

    fun isCrossShaped(needle: String, firstWord: String?, secondWord: String?): Boolean {
        if (firstWord != needle && firstWord?.reversed() != needle) return false
        if (secondWord != needle && secondWord?.reversed() != needle) return false
        return true
    }

    data class Matrix(val cells: Map<Position, Char>) {

        val maxX: Int = cells.keys.maxOf { it.first }
        val maxY: Int = cells.keys.maxOf { it.second }

        fun allPositionsOfChar(char: Char): Sequence<Position> =
            allPositions().filter { cells[it] == char }

        fun allPositions(): Sequence<Position> = sequence {
            for (x in 0..maxX) {
                for (y in 0..maxY) {
                    yield(x to y)
                }
            }
        }

        fun allWordsOfLength(length: Int): Sequence<String> = sequence {
            val movesOnAllAxes = listOf(Move.RIGHT, Move.RIGHT_DOWN, Move.DOWN, Move.LEFT_DOWN)

            allPositions().forEach { startFrom ->
                movesOnAllAxes.forEach { direction ->
                    getWord(startFrom, direction, length)?.let {
                        yield(it)
                        yield(it.reversed())
                    }
                }
            }
        }

        fun getWord(startFrom: Position, direction: Move, length: Int): String? =
            getWord(*startFrom.vectorInDirection(direction, length).toTypedArray())

        fun getWord(vararg positions: Position): String? =
            positions
                .mapNotNull { cells[it] }
                .takeIf { it.size == positions.size }
                ?.joinToString("")

        operator fun get(position: Position): Char? {
            return cells[position]
        }

        operator fun contains(position: Position): Boolean {
            return position in cells
        }

        enum class Move(val vector: Position) {
            RIGHT(1 to 0),
            LEFT(-1 to 0),
            UP(0 to 1),
            DOWN(0 to -1),

            RIGHT_UP(1 to -1),
            RIGHT_DOWN(1 to 1),
            LEFT_UP(-1 to -1),
            LEFT_DOWN(-1 to 1),
        }

    }

}

typealias Position = Pair<Int, Int>

operator fun Position.plus(other: Position): Position = (this.first + other.first) to (this.second + other.second)

operator fun Position.plus(other: Move): Position = this + other.vector

fun Position.vectorInDirection(direction: Move, length: Int): List<Position> = (1 until length).scan(this) { pos, _ -> pos + direction }
