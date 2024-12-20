package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.anyShortestPathBfs

fun Resource.day20(): Day20 = Day20.parse(nonBlankLines())

data class Day20(
    val saveAtLeastPicoseconds: Long,
    val racetrack: Matrix<Char>
) {

    val racetrackStartAndEnd by lazy { racetrack.startAndEnd() }

    val result1 by lazy {
        // input is 141 x 141 and the honorable path is 9335
        racetrackStartAndEnd
            .let { (start, end) -> racetrack.howMany2StepCheatsWouldSaveAtLeastNTime(start, end, saveAtLeastPicoseconds) }
    }
//    val result2: String by lazy { TODO() }

    fun Matrix<Char>.howMany2StepCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Long): String {

        val honorableShortestPath = anyShortestPathBfs(start, end) { a, b -> this[b]!! != WALL } ?: error("No honorable path found")
        // start => 0, first step => 1, ...
        val honorablePathPosTime = honorableShortestPath.withIndex().associate { it.value to it.index }

        val walkedPath = HashSet<Position>()
        val triedCheatsCounter = HashMap<Int, Int>() // saved steps => variants

        fun isInDimsRin(pos: Position): Boolean {
            if (pos.x == 0L || pos.y == 0L) return true
            if (pos.x == dims.maxX || pos.y == dims.maxY) return true
            return false
        }

        fun findCheats(current: Position, next: Position): List<List<Position>> {
            val result = ArrayList<List<Position>>()

            for (dir1 in Direction.entriesCardinal) {
                val step1 = current + dir1
                if (step1 == next) {
                    continue // no reason to try cheating on a valid path
                }
                if (isInDimsRin(step1)) {
                    continue // pointless
                }
                if (this[step1]!! != WALL) {
                    continue
                }
                if (step1 in walkedPath) {
                    continue // no walking back
                }

                for (dir2 in Direction.entriesCardinal) {
                    val step2 = step1 + dir2
                    if (step2 == current) {
                        continue
                    }
                    if (step2 !in dims) {
                        continue // outside of map
                    }
                    if (isInDimsRin(step2)) {
                        continue // pointless
                    }
                    if (this[step2]!! == WALL) {
                        continue // cannot end in a wall
                    }
                    if (step2 in walkedPath) {
                        continue // no walking back
                    }

                    result.add(listOf(step1, step2))
                }
            }

            return result
        }

        var current = start
        for (next in honorableShortestPath.drop(1)) {
            if (next == end) break

            val reasonableCheats = findCheats(current, next)
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
            .sortedBy { it.key }
            .filter { it.key >= saveAtLeast }

        val cheatsSummary = relevantCheats.joinToString(separator = "\n- ", prefix = "- ", postfix = "\n\n") { (savedSteps, variants) ->
            "There is/are $variants cheats that save $savedSteps picoseconds."
        }

        val cheatsTotal = relevantCheats.sumOf { it.value.toLong() }.let { sum ->
            "There is total of $sum cheats that save at least $saveAtLeast picoseconds.\n"
        }

        return cheatsSummary + cheatsTotal
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
                saveAtLeast.toLong(),
                Matrix.ofChars(Resource.CharMatrix2d.fromLines(rawRacetrack.toList()))
            )
        }

    }

}
