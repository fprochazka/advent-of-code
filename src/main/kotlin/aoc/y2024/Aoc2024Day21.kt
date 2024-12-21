package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.RangesCounter
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

    data class Code(val raw: String) {
        val length: Int get() = raw.length

        val subSequences by lazy(LazyThreadSafetyMode.PUBLICATION) {
            raw.toKeyPad2Sequences().map { of(it) }
        }

        override fun toString(): String = "'$raw' ($length, ${subSequences.size})"

        fun String.toKeyPad2Sequences(): List<String> =
            this.split('A').dropLast(1).map { it + 'A' }

        companion object {

            // private val cache = HashMap<String, Code>()

            fun of(raw: Char): Code =
                of(raw.toString())

            fun of(raw: String): Code =
                // cache.getOrPut(raw) { Code(raw) }
                Code(raw)

        }
    }

    fun howToTypeSecurityCodeOnFourthKeypad(code1: String, directionalKeypadsThatRobotsAreUsing: Int): String {
        val expandsTo = buildMap<Code, List<Code>> {
            val tmp = this

            var nextRound = keyPad2.entries.mapTo(HashSet()) { it.second }.filter { it != ' ' }.map { Code.of(it) }.toSet()
            while (nextRound.isNotEmpty()) {
                val toExpand = HashSet<Code>()
                for (code in nextRound) {
                    if (code in tmp) continue

                    val expansions = allShortestWaysToTypeOn(code.raw, keyPad2).map { Code.of(it) }
                    tmp[code] = expansions

                    val subSequences = expansions.flatMapTo(HashSet()) { it.subSequences }
                    toExpand.addAll(subSequences.filter { it !in tmp })
                }
                nextRound = toExpand
            }
        }.toMutableMap()
        val expandsToLength: MutableMap<Code, Int> = expandsTo.mapValues { it.value.minOf { it.length } }.toMutableMap()

        fun expandsTo(code: Code): List<Code> {
            expandsTo[code]?.let { return it }

            val expansions = allShortestWaysToTypeOn(code.raw, keyPad2).map { Code.of(it) }

            expandsTo[code] = expansions
            expandsToLength[code] = expansions.minOf { it.length }

            return expansions
        }

        fun expandsToLength(code: Code): Int {
            expandsToLength[code]?.let { return it }
            expandsTo(code)
            return expandsToLength[code] ?: error("No length for $code")
        }

        fun expand(code: Code): Sequence<Code> = sequence {
            val subSequences = code.subSequences

            val alternativesOnDepth = mutableListOf<List<Code>>()
            for (subSequence in subSequences) {
                val expansions = expandsTo(subSequence)
                val expansionsByLength = expansions.groupBy { expandsToLength(it) }
                val shortestExpansions = expansionsByLength.entries.minBy { it.key }.value
                alternativesOnDepth.add(shortestExpansions)
            }

            val alternativesCounter = RangesCounter(subSequences.size) { alternativesOnDepth[it].indices }

            fun codeFromAlternatives(): Code {
                val picked = alternativesCounter.get().mapIndexed { depth, alternativeIndex -> alternativesOnDepth[depth][alternativeIndex] }
                return Code(picked.joinToString("") { it.raw })
            }

            while (alternativesCounter.hasNext()) {
                // println(alternativesCounter)
                yield(codeFromAlternatives())
                alternativesCounter.next()
            }
        }

        fun expandOptimally(codes: List<Code>, repeats: Int): List<Code> {
            var result = codes
            repeat(repeats) {
                val round = mutableListOf<Code>()
                for (code in result) {
                    for (expanded in expand(code)) {
                        round.add(expanded)
                    }
                }

                result = round.allMinOf { it.length }
            }

            return result
        }

        // One numeric keypad (on a door) that a robot is using.
        var typingOnNext = allShortestWaysToTypeOn(code1, keyPad1).map { Code.of(it) }

        // N directional keypads that robots are using.
        typingOnNext = expandOptimally(typingOnNext, directionalKeypadsThatRobotsAreUsing)

        // I'm typing on the last one

        return typingOnNext.first().raw
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
