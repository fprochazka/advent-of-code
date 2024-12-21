package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.anyShortest.anyShortestPathBfs

fun Resource.day21(): Day21 = Day21(
    nonBlankLines()
)

data class Day21(val securityCodes: List<String>) {

    // +---+---+---+
    // | 7 | 8 | 9 |
    // +---+---+---+
    // | 4 | 5 | 6 |
    // +---+---+---+
    // | 1 | 2 | 3 |
    // +---+---+---+
    //     | 0 | A |
    //     +---+---+
    val keyPad1 by lazy {
        Matrix.ofChars(Resource.CharMatrix2d.fromContent("789\n456\n123\n 0A"))
    }

    //     +---+---+
    //     | ^ | A |
    // +---+---+---+
    // | < | v | > |
    // +---+---+---+
    val keyPad2 by lazy {
        Matrix.ofChars(Resource.CharMatrix2d.fromContent(" ^A\n<v>"))
    }

    val keyPadsButtonPositions: Map<Matrix<Char>, Map<Char, Position>> by lazy {
        mapOf(
            keyPad1 to keyPad1.allPositionsByValues({ true }).mapValues { it.value.single() },
            keyPad2 to keyPad2.allPositionsByValues({ true }).mapValues { it.value.single() },
        )
    }

    val result1 by lazy { sumOfComplexitiesForSecurityCodes(securityCodes) }
//    val result2: String by lazy { TODO() }

    // level 1
    // type 029A:
    //    <A^A>^^AvvvA
    //    <A^A^>^AvvvA
    //    and <A^A^^>AvvvA

    // 4 <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
    // 3 v<<A>>^A<A>AvA<^AA>A<vAAA>^A
    // 2 <A^A>^^AvvvA
    // 1 029A

    // 029A: <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
    // 980A: <v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A
    // 179A: <v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A
    // 456A: <v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A
    // 379A: <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

    // The complexity of a single code (like 029A) is equal to the result of multiplying these two values:
    //
    //    The length of the shortest sequence of button presses you need to type on your directional keypad in order to cause the code to be typed on the numeric keypad;
    //         for 029A, this would be 68.
    //    The numeric part of the code (ignoring leading zeroes);
    //         for 029A, this would be 29.

    // complexity of the five codes can be found by calculating
    //   68 * 29
    //   60 * 980
    //   68 * 179
    //   64 * 456
    //   64 * 379
    // Adding these together produces 126384

    fun sumOfComplexitiesForSecurityCodes(codes: List<String>): Long {
        val expandedCodes = codes.map { it to howToTypeSecurityCodeOnFourthKeypad(it).length }

        val complexities = expandedCodes.map { (code, expanded) ->
            code.trimStart('0').trimEnd('A').toLong() to expanded
        }

        return complexities.sumOf { (codeInt, complexity) -> codeInt * complexity }
    }

    fun howToTypeSecurityCodeOnFourthKeypad(code1: String): String {
        fun directionToSymbol(direction: Direction): Char = when (direction) {
            Direction.UP -> '^'
            Direction.RIGHT -> '>'
            Direction.DOWN -> 'v'
            Direction.LEFT -> '<'
            else -> throw IllegalArgumentException("Unexpected direction $direction")
        }

        fun howToPressNextOnKeyPad2(symbol: Char, start: Position, keyPad: Matrix<Char>): Pair<String, Position> {
            val keyPadButtons = keyPadsButtonPositions[keyPad]!!

            val remoteCurrent = start
            val symbolPosition = keyPadButtons[symbol]!!
            val shortestPath = keyPad.anyShortestPathBfs(remoteCurrent, symbolPosition, { a, b -> true }) ?: error("No path found")
            val shortestPathDirections = shortestPath.zipWithNext().map { (a, b) -> a.relativeDirectionTo(b) ?: error("Cannot determine direction from $a to $b") }

            val howToTypeOnKeyPad2 = shortestPathDirections.map { directionToSymbol(it) }.joinToString(separator = "", postfix = "A")

            return howToTypeOnKeyPad2 to symbolPosition
        }

        fun shortestWayToType(remoteKeyPadCode: String, remoteKeyPad: Matrix<Char>): String {
            // localKeyPad is always keyPad2

            val remoteKeyPadSymbols = remoteKeyPadCode.toList()

            val remoteKeyPadButtons = keyPadsButtonPositions[remoteKeyPad]!!
            val remoteKeyPadStart = remoteKeyPadButtons['A']!!

            var localPressed = ""
            var remoteCurrent = remoteKeyPadStart
            for (nextRemoteButton in remoteKeyPadSymbols) {
                val (localPressedForButton, nextRemotePosition) = howToPressNextOnKeyPad2(symbol = nextRemoteButton, start = remoteCurrent, keyPad = remoteKeyPad)

                localPressed += localPressedForButton
                remoteCurrent = nextRemotePosition
            }

            return localPressed
        }

        val typing1on2 = shortestWayToType(code1, keyPad1)
        val typing2on3 = shortestWayToType(typing1on2, keyPad2)
        val typing3on4 = shortestWayToType(typing2on3, keyPad2)

        return typing3on4
    }

}
