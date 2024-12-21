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
        val expandedCodes = codes
            .map { it to howToTypeSecurityCodeOnFourthKeypad(it, directionalKeypadsThatRobotsAreUsing).length }

        val complexities = expandedCodes
            .map { (code, expanded) -> code.trimStart('0').trimEnd('A').toLong() to expanded }
            .map { (codeInt, complexity) -> codeInt * complexity }

        return complexities.sum()
    }

    fun howToTypeSecurityCodeOnFourthKeypad(code1: String, directionalKeypadsThatRobotsAreUsing: Int): String {
//        val minimize = compareBy<String> { it.length }.thenBy { it.count { it == '<' } }
//
//        val typing1on2 = allShortestWaysToType(code1, keyPad1).minWith(minimize)
//        val typing2on3 = allShortestWaysToType(typing1on2, keyPad2).minWith(minimize)
//        val typing3on4 = allShortestWaysToType(typing2on3, keyPad2).minBy { it.length }

        val subSequenceLeadsToWaysToType = mutableMapOf<String, List<String>>()
        val subSequenceLeadsToNextLevelExpansionLengths = mutableMapOf<String, Int>()

        fun expandOnlyTheShortestPaths(allCodes: List<String>): List<String> {
            val codesAndSubSequences = allCodes.map { it to it.split('A').map { it + 'A' } }
            for ((_, subSequences) in codesAndSubSequences) {
                for (subSequence in subSequences) {
                    val computed = subSequenceLeadsToWaysToType.computeIfAbsent(subSequence) { allShortestWaysToTypeOn(subSequence, keyPad2) }
                    subSequenceLeadsToNextLevelExpansionLengths[subSequence] = computed.minOf { it.length }
                }
            }

            val leadsToNextLevelExpansion = mutableMapOf<String, Int>()
            for ((code, subSequences) in codesAndSubSequences) {
                leadsToNextLevelExpansion[code] = subSequences.sumOf { subSequenceLeadsToNextLevelExpansionLengths[it]!! }
            }

            val minExpansionLength = leadsToNextLevelExpansion.minOf { it.value }
            val onlyTheShortestExpansionCodes = leadsToNextLevelExpansion.filter { it.value == minExpansionLength }.map { it.key }

            return onlyTheShortestExpansionCodes.flatMap { allShortestWaysToTypeOn(it, keyPad2) }
        }

        // One numeric keypad (on a door) that a robot is using.
        val typing1on2 = allShortestWaysToTypeOn(code1, keyPad1)

        var typingOnNext = typing1on2
        repeat(directionalKeypadsThatRobotsAreUsing) {
            typingOnNext = expandOnlyTheShortestPaths(typingOnNext)
        }

        // I'm typing on the last one

        return typingOnNext.minBy { it.length }
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

        val minLength = result.minOf { it.length }
        return result.filter { it.length <= minLength }
    }

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
