package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.allShortest.allShortestPathsDfs

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
    val keyPadNumeric by lazy {
        Matrix.ofChars(Resource.CharMatrix2d.fromContent("789\n456\n123\n 0A"))
    }

    //     +---+---+
    //     | ^ | A |
    // +---+---+---+
    // | < | v | > |
    // +---+---+---+
    val keyPadArrows by lazy {
        Matrix.ofChars(Resource.CharMatrix2d.fromContent(" ^A\n<v>"))
    }

    val keyPadsButtonPositions: Map<Matrix<Char>, Map<Char, Position>> by lazy {
        mapOf(
            keyPadNumeric to keyPadNumeric.allPositionsByValues({ true }).mapValues { it.value.single() },
            keyPadArrows to keyPadArrows.allPositionsByValues({ true }).mapValues { it.value.single() },
        )
    }

    val result1 by lazy { sumOfComplexitiesForSecurityCodes(securityCodes, directionalKeypadsThatRobotsAreUsing = 2) }
    val result2 by lazy { sumOfComplexitiesForSecurityCodes(securityCodes, directionalKeypadsThatRobotsAreUsing = 25) }

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

    fun sumOfComplexitiesForSecurityCodes(codes: List<String>, directionalKeypadsThatRobotsAreUsing: Int): Long {
        var robot: Robot? = null
        for (i in 1..directionalKeypadsThatRobotsAreUsing) {
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

    data class Code(val raw: String) {
        val length: Int get() = raw.length

        val subSequences by lazy(LazyThreadSafetyMode.PUBLICATION) {
            raw.toKeyPad2Sequences().map { of(it) }
        }

        override fun toString(): String = "'$raw' ($length, ${subSequences.size})"

        fun String.toKeyPad2Sequences(): List<String> =
            this.split('A').dropLast(1).map { it + 'A' }

        companion object {

            private val cache = HashMap<String, Code>()

            fun of(raw: Char): Code =
                of(raw.toString())

            fun of(raw: String): Code =
                cache.getOrPut(raw) { Code(raw) }

        }
    }

    inner class Door(val controlledBy: Robot) {

        val input: Matrix<Char> = keyPadNumeric

        fun shortestLengthToType(code: Code): Long {
            val lengths = mutableListOf<Long>()

            for (arrowsCode in allShortestWaysToTypeOn(code.raw, input).map { Code.of(it) }) {
                var length = 0L
                for (subSequence in arrowsCode.subSequences) {
                    length += controlledBy.shortestLengthToType(subSequence)
                }
                lengths.add(length)
            }

            return lengths.minOf { it }
        }

        override fun toString(): String = "Door"

    }

    inner class Robot(val controlledBy: Robot?, val id: Int) {

        val input: Matrix<Char> = keyPadArrows

        private val movesCache = mutableMapOf<String, Long>()

        fun shortestLengthToType(code: Code): Long {
            require(code.raw.count { it == 'A' } == 1) { "Expected exactly one 'A' in $code for $this" }

            return movesCache.getOrPut(code.raw) {
                val alternatives = arrowCodeExpandsTo(code)

                if (controlledBy == null) {
                    return@getOrPut alternatives.minOf { it.length.toLong() }

                } else {
                    val lengths = mutableListOf<Long>()
                    for (alternative in alternatives) {
                        var length = 0L
                        for (subSequence in alternative.subSequences) {
                            length += controlledBy.shortestLengthToType(subSequence)
                        }
                        lengths.add(length)
                    }

                    return@getOrPut lengths.minOf { it }
                }
            }
        }

        override fun toString(): String = "Robot $id"

    }

    val arrowCodeExpandsTo: MutableMap<String, List<Code>> = HashMap()

    fun arrowCodeExpandsTo(code: Code): List<Code> =
        arrowCodeExpandsTo.getOrPut(code.raw) {
            allShortestWaysToTypeOn(code.raw, keyPadArrows).map { Code.of(it) }
        }

    fun allShortestWaysToTypeOn(code: String, remoteKeyPad: Matrix<Char>): List<String> {
        val result = mutableListOf<String>()

        val remoteKeyPadButtons = keyPadsButtonPositions[remoteKeyPad]!!
        val remoteKeyPadStart = remoteKeyPadButtons['A']!!

        fun dfs(remoteCurrentPos: Position, symbols: List<Char>, alreadyPressed: String = "") {
            if (symbols.isEmpty()) {
                result.add(alreadyPressed)
                return
            }

            val nextRemoteSymbols = symbols[0]
            val remainingSymbols = symbols.drop(1)
            val remoteNextPos = remoteKeyPadButtons[nextRemoteSymbols]!!

            // localKeyPad is always keyPad2
            for (pressedForButton in howToTypeSymbolOnKeyPad2(symbol = nextRemoteSymbols, startPos = remoteCurrentPos, remoteKeyPad = remoteKeyPad)) {
                dfs(remoteCurrentPos = remoteNextPos, symbols = remainingSymbols, alreadyPressed = alreadyPressed + pressedForButton)
            }
        }

        dfs(remoteCurrentPos = remoteKeyPadStart, symbols = code.toList())

        return result.allMinOf { it.length }.sorted()
    }

    fun <V> Collection<V>.allMinOf(selector: (V) -> Int): List<V> =
        this.groupBy { selector(it) }.entries.minBy { it.key }.value

    val howToTypeSymbolCache = mutableMapOf<Matrix<Char>, MutableMap<Pair<Char, Char>, List<String>>>()

    fun howToTypeSymbolOnKeyPad2(symbol: Char, startPos: Position, remoteKeyPad: Matrix<Char>): List<String> {
        val symbolAtStart = remoteKeyPad[startPos]!!

        val keyPadCache = howToTypeSymbolCache.getOrPut(remoteKeyPad) { mutableMapOf() }
        keyPadCache[symbolAtStart to symbol]?.let { return it }

        val keyPadButtons = keyPadsButtonPositions[remoteKeyPad]!!
        val endPos = keyPadButtons[symbol]!!

        val result = mutableListOf<String>()
        for (shortestPath in remoteKeyPad.allShortestPathsDfs(startPos, endPos, { a, b -> remoteKeyPad[b] != ' ' && remoteKeyPad[a] != ' ' })) {
            val shortestPathDirections = shortestPath.zipWithNext().map { (a, b) -> a.relativeDirectionTo(b) ?: error("Cannot determine direction from $a to $b") }
            val howToTypeOnKeyPad2 = shortestPathDirections.map { it.toSymbol() }.joinToString(separator = "", postfix = "A")

            result.add(howToTypeOnKeyPad2)
        }

        keyPadCache[symbolAtStart to symbol] = result

        return result
    }

    fun Direction.toSymbol(): Char = when (this) {
        Direction.UP -> '^'
        Direction.RIGHT -> '>'
        Direction.DOWN -> 'v'
        Direction.LEFT -> '<'
        else -> throw IllegalArgumentException("Unexpected direction $this")
    }

}
