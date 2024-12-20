package aoc.y2024

import aoc.utils.AocDebug
import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.anyShortestPathBfs
import kotlin.math.absoluteValue

fun Resource.day20(): Day20 = Day20.parse(nonBlankLines())

data class Day20(
    val saveAtLeastPicoseconds: Int,
    val racetrack: Matrix<Char>
) {

    val racetrackStartAndEnd by lazy { racetrack.startAndEnd() }

    // input is 141 x 141 and the honorable path is 9335
    val result1 by lazy {
        racetrackStartAndEnd
            .let { (start, end) -> racetrack.howMany2StepCheatsWouldSaveAtLeastNTime(start, end, saveAtLeastPicoseconds) }
    }
    val result2 by lazy {
        racetrackStartAndEnd
            .let { (start, end) -> racetrack.howMany20StepCheatsWouldSaveAtLeastNTime(start, end, saveAtLeastPicoseconds) }
    }

    fun Matrix<Char>.howMany2StepCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Int): Long {
        val walkedPath = HashSet<Position>()

        fun findCheats(current: Position): List<List<Position>> {
            val result = ArrayList<List<Position>>()

            for (step1 in current.neighboursCardinalIn(dims)) {
                if (this[step1]!! != WALL) {
                    continue // we want to walk into a wall with the first step
                }
                if (isInRim(step1) || step1 in walkedPath) {
                    continue
                }

                for (step2 in step1.neighboursCardinalIn(dims)) {
                    if (step2 == step1) {
                        continue // no walking back
                    }
                    if (this[step2]!! == WALL) {
                        continue // cannot end in a wall
                    }
                    if (isInRim(step2) || step2 in walkedPath) {
                        continue
                    }

                    result.add(listOf(step1, step2))
                }
            }

            return result
        }

        val honorableShortestPath = findShortestPath(start, end)
        // start => 0, first step => 1, ...
        val honorablePathPosTime = honorableShortestPath.withIndex().associate { it.value to it.index }

        val triedCheatsCounter = HashMap<Int, Int>() // saved steps => variants

        var current = start
        for (next in honorableShortestPath.drop(1)) {
            if (next == end) break

            val reasonableCheats = findCheats(current)
            for (reasonableCheat in reasonableCheats) {
                // TODO: validate we're not crossing paths?

                val cheatEnd = reasonableCheat.last()
                val timeAtCheat = walkedPath.size + reasonableCheat.size

                val originalTimeAtPosition = honorablePathPosTime[cheatEnd]!!
                val savedSteps = originalTimeAtPosition - timeAtCheat

//                racetrack.printPath(walkedPath + current, reasonableCheat)
//                println("Saved $savedSteps steps")
//                println()

                if (savedSteps <= 0) {
                    continue
                }

                triedCheatsCounter.merge(savedSteps, 1, Int::plus)
            }

            walkedPath.add(current)
            current = next
        }

        val relevantCheats = triedCheatsCounter.entries
            .map { it.key to it.value }
            .filter { it.first >= saveAtLeast }

        if (AocDebug.enabled) printDetails(relevantCheats)

        return relevantCheats.sumOf { it.second.toLong() }
    }

    fun Matrix<Char>.howMany20StepCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Int): Long {
        fun Position.cheatingDistanceTo(other: Position): Int =
            this.distanceTo(other).let { (xDiff, yDiff) -> (xDiff.absoluteValue + yDiff.absoluteValue).toInt() }

        val walkedPath = HashSet<Position>()

        val honorableShortestPath = findShortestPath(start, end)
        // start => 0, first step => 1, ...
        val honorablePathPosTime = honorableShortestPath.withIndex().associate { it.value to it.index }

        fun List<Position>.remainingPositionsFromIndex(index: Int): Sequence<Position> =
            this.asSequence().drop(index)

        val triedCheatsCounter = HashMap<Int, Int>() // saved steps => variants

        var current = start
        for ((index, next) in honorableShortestPath.asSequence().withIndex().drop(1)) {
            if (next == end) break

            for (cheatEnd in honorableShortestPath.remainingPositionsFromIndex(index + saveAtLeast)) {
                val cheatDistance = current.cheatingDistanceTo(cheatEnd)
                if (cheatDistance > 20) continue

                val timeAtCheat = walkedPath.size + cheatDistance

                val originalTimeAtPosition = honorablePathPosTime[cheatEnd]!!
                val savedSteps = originalTimeAtPosition - timeAtCheat

//                racetrack.printPath(walkedPath + current, anyShortestPathBfs(current, cheatEnd) { _, _ -> true }!!)
//                println("Saved $savedSteps steps")
//                println()

                if (savedSteps < saveAtLeast) {
                    continue
                }

                triedCheatsCounter.merge(savedSteps, 1, Int::plus)
            }

            walkedPath.add(current)
            current = next
        }

        val relevantCheats = triedCheatsCounter.entries
            .map { it.key to it.value }
            .filter { it.first >= saveAtLeast }

        if (AocDebug.enabled) printDetails(relevantCheats)

        return relevantCheats.sumOf { it.second.toLong() }
    }

    fun printDetails(relevantCheats: List<Pair<Int, Int>>) {
        val cheatsSummary = relevantCheats
            .sortedBy { it.first }
            .joinToString(separator = "\n", prefix = "\n") { (savedSteps, variants) ->
                "# $variants cheats save $savedSteps"
            }
        println(cheatsSummary)
    }

    fun Matrix<Char>.findShortestPath(start: Position, end: Position): List<Position> =
        anyShortestPathBfs(start, end) { a, b -> this[b]!! != WALL } ?: error("No honorable path found")

    fun Matrix<Char>.isInRim(pos: Position): Boolean {
        if (pos.x == 0L || pos.y == 0L) return true
        if (pos.x == dims.maxX || pos.y == dims.maxY) return true
        return false
    }

    fun Matrix<Char>.printPath(path: Collection<Position>, cheats: Collection<Position> = emptySet()) {
        racetrack.copy().also { matrix ->
            path.forEach { pos -> matrix[pos] = 'o' }
            cheats.forEach { pos -> matrix[pos] = 'C' }
            print(matrix.toString())
        }
    }

    fun Matrix<Char>.startAndEnd(): Pair<Position, Position> =
        allPositionsByValues { it == START || it == END }
            .let { it[START]!!.single() to it[END]!!.single() }

    companion object {

        const val EMPTY = '.'
        const val START = 'S'
        const val END = 'E'
        const val WALL = '#'

        fun parse(lines: List<String>): Day20 {
            val (saveAtLeast, rawRacetrack) = lines.headTail()

            return Day20(
                saveAtLeast.toInt(),
                Matrix.ofChars(Resource.CharMatrix2d.fromLines(rawRacetrack.toList()))
            )
        }

    }

}
