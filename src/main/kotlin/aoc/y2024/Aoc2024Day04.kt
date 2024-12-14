package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position

fun Resource.day04(): Day04 = Day04(
    Matrix.ofChars(matrix2d())
)

data class Day04(val matrix: Matrix<Char>) {

    val result1 by lazy {
        matrix
            .allWordsOfLength("XMAS".length)
            .count { "XMAS" == it }
    }

    val result2 by lazy {
        matrix
            .allPositionsOfValue('A')
            .count { aPos ->
                isCrossShaped(
                    "MS",
                    matrix.getWord(
                        aPos + Direction.RIGHT_UP,
                        aPos + Direction.LEFT_DOWN,
                    ),
                    matrix.getWord(
                        aPos + Direction.RIGHT_DOWN,
                        aPos + Direction.LEFT_UP,
                    )
                )
            }
    }

    fun isCrossShaped(needle: String, firstWord: String?, secondWord: String?): Boolean {
        if (firstWord != needle && firstWord?.reversed() != needle) return false
        if (secondWord != needle && secondWord?.reversed() != needle) return false
        return true
    }

    fun Matrix<Char>.allWordsOfLength(length: Int): Sequence<String> = sequence {
        val movesOnAllAxes = listOf(Direction.RIGHT, Direction.RIGHT_DOWN, Direction.DOWN, Direction.LEFT_DOWN)

        positions.forEach { startFrom ->
            movesOnAllAxes.forEach { direction ->
                getWord(startFrom, direction, length)?.let {
                    yield(it)
                    yield(it.reversed())
                }
            }
        }
    }

    fun Matrix<Char>.getWord(startFrom: Position, direction: Direction, length: Int): String? =
        getWord(*startFrom.vectorInDirection(direction, length).toTypedArray())

    fun Matrix<Char>.getWord(vararg positions: Position): String? =
        positions
            .mapNotNull { this[it] }
            .takeIf { it.size == positions.size }
            ?.joinToString("")

}
