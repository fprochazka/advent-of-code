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
            .allWordsOfLength4()
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

    fun Matrix<Char>.allWordsOfLength4(): Sequence<String> = sequence {
        val movesOnAllAxes = listOf(Direction.RIGHT, Direction.RIGHT_DOWN, Direction.DOWN, Direction.LEFT_DOWN)

        positions.forEach { startFrom ->
            movesOnAllAxes.forEach { direction ->
                getWord4(startFrom, direction)?.let {
                    yield(it)
                    yield(it.reversed())
                }
            }
        }
    }

    fun Matrix<Char>.getWord4(startFrom: Position, direction: Direction): String? {
        val p1 = startFrom
        val p2 = p1 + direction
        val p3 = p2 + direction
        val p4 = p3 + direction

        return getWord(p1, p2, p3, p4)
    }

    fun Matrix<Char>.getWord(vararg positions: Position): String? {
        var overflow = false
        val chars = CharArray(positions.size) { this[positions[it]] ?: (' '.also { overflow = true }) }
        return if (overflow) null else chars.joinToString("")
    }

}
