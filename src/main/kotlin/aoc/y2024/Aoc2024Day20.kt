package aoc.y2024

import aoc.utils.Resource
import aoc.utils.concurrency.parMap
import aoc.utils.containers.headTail
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.Matrix
import aoc.utils.d2.matrix.anyShortest.anyShortestPathBfs
import aoc.utils.d2.path.GraphConnection
import aoc.utils.d2.path.GraphPathStep
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Executors
import kotlin.math.absoluteValue

fun Resource.day20(): Day20 = Day20.parse(nonBlankLines())

@OptIn(ExperimentalCoroutinesApi::class)
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
            .let { (start, end) -> racetrack.howManyUpTo20StepCheatsWouldSaveAtLeastNTime(start, end, saveAtLeastPicoseconds) }
    }

    fun Matrix<Char>.howMany2StepCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Int): Int {
        val honorableShortestPath = findShortestPathPositions(start, end)
        // start => 0, first step => 1, ...
        val honorablePathPosTime = honorableShortestPath.withIndex().associate { it.value to it.index }

        fun findCheats(currentIndex: Int): List<Position> {
            val current = honorableShortestPath[currentIndex]
            val previous = if (currentIndex > 0) honorableShortestPath[currentIndex - 1] else null

            val result = ArrayList<Position>()
            for (step1 in current.neighboursCardinalIn(dims)) {
                if (this[step1]!! != WALL) {
                    continue // we want to walk into a wall with the first step
                }
                if (isOnRim(step1) || step1 == previous) {
                    continue
                }

                for (step2 in step1.neighboursCardinalIn(dims)) {
                    if (step2 == step1) {
                        continue // no walking back
                    }
                    if (this[step2] == WALL) {
                        continue // cannot end in a wall
                    }
                    if (isOnRim(step2)) {
                        continue
                    }

                    result.add(step2)
                }
            }

            return result
        }

        var cheatsCounter = 0
        for (currentIndex in 0..(honorableShortestPath.lastIndex - 1)) {
            val walkedPathLength = currentIndex

            for (cheatJumpTo in findCheats(currentIndex)) {
                val timeAtCheat = walkedPathLength + 2 // 2 = cheats size

                val originalTimeAtPosition = honorablePathPosTime[cheatJumpTo]!!
                val savedSteps = originalTimeAtPosition - timeAtCheat

//                racetrack.printPath(walkedPath + current, reasonableCheat)
//                println("Saved $savedSteps steps")
//                println()

                if (savedSteps < saveAtLeast) {
                    continue
                }

                cheatsCounter++
            }
        }

        return cheatsCounter
    }

    fun Matrix<Char>.howManyUpTo20StepCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Int): Int {
        fun Position.cheatingDistanceTo(other: Position): Int =
            this.distanceTo(other).let { (xDiff, yDiff) -> (xDiff.absoluteValue + yDiff.absoluteValue).toInt() }

        val honorableShortestPath = findShortestPathPositions(start, end)

        val parallelism = 8
        val executor = Executors.newFixedThreadPool(parallelism, Thread.ofVirtual().factory())

        return honorableShortestPath.indices
            .parMap(executor) { currentIndex ->
                val current = honorableShortestPath[currentIndex]
                val walkedPathLength = currentIndex

                var cheatsCounter = 0
                for (cheatEndIndex in (currentIndex + saveAtLeast - 1)..honorableShortestPath.lastIndex) {
                    val cheatEnd = honorableShortestPath[cheatEndIndex]

                    val cheatDistance = current.cheatingDistanceTo(cheatEnd)
                    if (cheatDistance > 20) continue

                    val timeAtCheat = walkedPathLength + cheatDistance
                    val originalTimeAtPosition = cheatEndIndex
                    val savedSteps = originalTimeAtPosition - timeAtCheat

//                         racetrack.printPath(walkedPath + current, anyShortestPathBfs(current, cheatEnd) { _, _ -> true }!!)
//                         println("Saved $savedSteps steps")
//                         println()

                    if (savedSteps < saveAtLeast) {
                        continue
                    }

                    cheatsCounter++
                }

                return@parMap cheatsCounter
            }
            .sum()
            .also { executor.shutdown() }
    }

    fun printDetails(relevantCheats: List<Pair<Int, Int>>) {
        val cheatsSummary = relevantCheats
            .sortedBy { it.first }
            .joinToString(separator = "\n", prefix = "\n") { (savedSteps, variants) ->
                "# $variants cheats save $savedSteps"
            }
        println(cheatsSummary)
    }

    fun Matrix<Char>.findShortestPathPositions(start: Position, end: Position): List<Position> =
        findShortestPath(start, end).toPositions()

    fun Matrix<Char>.findShortestPath(start: Position, end: Position): GraphPathStep =
        anyShortestPathBfs(start, end) { a, b -> GraphConnection.edgeIf(this[b]!! != WALL) } ?: error("No honorable path found")

    fun Matrix<Char>.isOnRim(pos: Position): Boolean {
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
