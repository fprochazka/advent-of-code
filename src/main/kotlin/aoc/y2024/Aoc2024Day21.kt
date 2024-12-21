package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.allMinOf
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.path.GraphPathStep
import aoc.utils.d2.matrix.allShortest.allShortestPathsModifiedDijkstra

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
    val keyPadNumeric by lazy { KeyPad("789\n456\n123\n 0A") }

    //     +---+---+
    //     | ^ | A |
    // +---+---+---+
    // | < | v | > |
    // +---+---+---+
    val keyPadArrows by lazy { KeyPad(" ^A\n<v>") }

    val result1 by lazy { sumOfComplexitiesForSecurityCodes(securityCodes, arrowKeypadsThatRobotsAreUsing = 2) }
    val result2 by lazy { sumOfComplexitiesForSecurityCodes(securityCodes, arrowKeypadsThatRobotsAreUsing = 25) }

    // level 1
    // type 029A:
    //    <A^A>^^AvvvA
    //    <A^A^>^AvvvA
    //    <A^A^^>AvvvA

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
    //    29 * 68  = 1972
    //   980 * 60  = 58800
    //   179 * 68  = 12172
    //   456 * 64  = 29184
    //   379 * 64  = 24256
    // Adding these together produces 126384

    fun sumOfComplexitiesForSecurityCodes(codes: List<String>, arrowKeypadsThatRobotsAreUsing: Int): Long {
        var robot: Robot? = null
        for (i in 1..arrowKeypadsThatRobotsAreUsing) {
            robot = Robot(robot, i)
        }
        var door = Door(robot!!)

        val expandedCodes = codes
            .map { it to door.shortestLengthToType(Code.of(it)) }

        val complexities = expandedCodes
            .map { (code, expanded) -> code.trimStart('0').trimEnd('A').toLong() to expanded }
            .map { (codeInt, complexity) -> codeInt * complexity }

        return complexities.sum()
    }

    inner class Door(val controlledBy: Robot) {

        val input: KeyPad = keyPadNumeric

        fun shortestLengthToType(code: Code): Long =
            input.getShortestLengthToType(code, controlledBy)

        override fun toString(): String = "Door"

    }

    inner class Robot(val controlledBy: Robot?, val id: Int) {

        val input: KeyPad = keyPadArrows

        private val movesCache = mutableMapOf<String, Long>()

        fun shortestLengthToType(code: Code): Long =
            movesCache.getOrPut(code.raw) { input.getShortestLengthToType(code, controlledBy) }

        override fun toString(): String = "Robot $id"

    }

    fun KeyPad.getShortestLengthToType(code: Code, controlledBy: Robot?): Long {
        require(code.raw.count { it == 'A' } == 1) { "Expected exactly one 'A' in $code for $this" }

        val alternatives = getAllShortestWaysToType(code)

        if (controlledBy == null) {
            return alternatives.minOf { it.length.toLong() }
        }

        val lengths = mutableListOf<Long>()
        for (alternative in alternatives) {
            var length = 0L
            for (subSequence in alternative.subSequences) {
                length += controlledBy.shortestLengthToType(subSequence)
            }
            lengths.add(length)
        }

        return lengths.minOf { it }
    }

    class KeyPad(buttons: String) {

        private val layout = Matrix.ofChars(Resource.CharMatrix2d.fromContent(buttons))
        private val buttonPositions: Map<Char, Position> = layout.allPositionsByValues({ true }).mapValues { it.value.single() }

        operator fun get(pos: Position): Char? = layout[pos]

        val shortestWaysToTypeCache: MutableMap<String, List<Code>> = HashMap()

        fun getAllShortestWaysToType(code: Code): List<Code> =
            shortestWaysToTypeCache.getOrPut(code.raw) {
                findAllShortestWaysToType(code)
            }

        private fun findAllShortestWaysToType(code: Code): List<Code> {
            val result = mutableListOf<String>()

            fun dfs(remoteCurrentPos: Position, symbols: List<Char>, alreadyPressed: String = "") {
                if (symbols.isEmpty()) {
                    result.add(alreadyPressed)
                    return
                }

                val nextRemoteSymbols = symbols[0]
                val remainingSymbols = symbols.drop(1)
                val remoteNextPos = buttonPositions[nextRemoteSymbols]!!

                // localKeyPad is always keyPad2
                for (pressedForButton in howToTypeSymbolOnArrowKeyPad(symbol = nextRemoteSymbols, startPos = remoteCurrentPos)) {
                    dfs(remoteCurrentPos = remoteNextPos, symbols = remainingSymbols, alreadyPressed = alreadyPressed + pressedForButton)
                }
            }

            dfs(remoteCurrentPos = buttonPositions['A']!!, symbols = code.toChars())

            return result.allMinOf { it.length }.sorted().map { Code.of(it) }
        }

        private val howToTypeSymbolCache = HashMap<Pair<Char, Char>, List<String>>()

        private fun howToTypeSymbolOnArrowKeyPad(symbol: Char, startPos: Position): List<String> =
            howToTypeSymbolCache
                .getOrPut(this[startPos]!! to symbol) {
                    val keyPadButtons = this.buttonPositions
                    val endPos = keyPadButtons[symbol]!!

                    val result = mutableListOf<String>()
                    for (shortestPath in this.shortestPaths(startPos, endPos)) {
                        val howToTypeOnKeyPad2 = shortestPath.toDirections()
                            .map { it.toSymbol() }
                            .joinToString(separator = "", postfix = "A")

                        result.add(howToTypeOnKeyPad2)
                    }

                    return@getOrPut result
                }

        private fun shortestPaths(start: Position, end: Position): Sequence<GraphPathStep> =
            layout.allShortestPathsModifiedDijkstra(start, end, { a, b -> if (layout[b] == ' ') MatrixGraph.INFINITE_COST else 1L })

    }

    data class Code(val raw: String) {
        val length: Int get() = raw.length

        val subSequences by lazy(LazyThreadSafetyMode.PUBLICATION) {
            raw.toKeyPad2Sequences().map { of(it) }
        }

        fun String.toKeyPad2Sequences(): List<String> =
            this.split('A').dropLast(1).map { it + 'A' }

        fun toChars(): List<Char> =
            raw.toList()

        override fun toString(): String = "'$raw' ($length, ${subSequences.size})"

        companion object {

            private val cache = HashMap<String, Code>()

            fun of(raw: Char): Code =
                of(raw.toString())

            fun of(raw: String): Code =
                cache.getOrPut(raw) { Code(raw) }

        }
    }

    companion object {

        fun Direction.toSymbol(): Char = when (this) {
            Direction.UP -> '^'
            Direction.RIGHT -> '>'
            Direction.DOWN -> 'v'
            Direction.LEFT -> '<'
            else -> throw IllegalArgumentException("Unexpected direction $this")
        }

    }

}
