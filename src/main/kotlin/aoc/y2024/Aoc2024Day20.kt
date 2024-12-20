package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.graph.path.reconstructPathFromParentsMap

fun Resource.day20(): Day20 = Day20.parse(nonBlankLines())

data class Day20(
    val saveAtLeastPicoseconds: Long,
    val racetrack: Matrix<Char>
) {

    val racetrackStartAndEnd by lazy { racetrack.startAndEnd() }

    val result1 by lazy {
        // input is 141 x 141 and the honorable path is 9335
        racetrackStartAndEnd
            .let { (start, end) -> racetrack.howManyCheatsWouldSaveAtLeastNTime(start, end, saveAtLeastPicoseconds) }
    }
//    val result2: String by lazy { TODO() }

    fun Matrix<Char>.howManyCheatsWouldSaveAtLeastNTime(start: Position, end: Position, saveAtLeast: Long): String {

        val shortestPathState = ShortestPathContinuable(this, end)
        val honorableShortestPath = shortestPathState.findPathNormally(start) ?: error("No honorable path found")

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
                if (shortestPathState.isVisited(step1)) {
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
                    if (shortestPathState.isVisited(step2)) {
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
                val pathWithCheats = shortestPathState.findPathWithCheats(current, reasonableCheat)
                val savedSteps = if (pathWithCheats == null) -1 else honorableShortestPath.size - pathWithCheats.size

//                racetrack.printPath(pathWithCheats ?: shortestPathState.partialPath, reasonableCheat)
//                println("Saved $savedSteps steps")
//                println()

                if (savedSteps <= 0) {
                    continue
                }

                triedCheatsCounter.merge(savedSteps, 1, Int::plus)
            }

            shortestPathState.recordStep(current)
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

    fun Matrix<Char>.printPath(path: List<Position>, cheats: Collection<Position> = emptySet()) {
        racetrack.copy().also { matrix ->
            path.forEach { pos -> matrix[pos] = 'o' }
            cheats.forEach { pos -> matrix[pos] = 'C' }
            print(matrix.toString())
        }
    }

    data class ShortestPathContinuable(
        val racetrack: Matrix<Char>,
        val end: Position,
        val partialVisited: MutableSet<Position> = mutableSetOf(),
        val partialPath: MutableList<Position> = mutableListOf(),
    ) {

        private val remainingQueue = ArrayDeque<Position>();
        private val remainingCameFrom = HashMap<Position, Position>()
        private val remainingVisited = HashSet<Position>();

        fun recordStep(from: Position) {
            partialPath.add(from)
            partialVisited.add(from)
        }

        private fun resetRemaining() {
            remainingQueue.clear()
            remainingCameFrom.clear()
            remainingVisited.clear()
        }

        fun findPathNormally(start: Position): List<Position>? {
            return findPath(start).also { resetRemaining() }
        }

        fun findPathWithCheats(start: Position, cheats: List<Position>): List<Position>? {
            var currentCheat = start
            for (nextCheat in cheats) {
                remainingVisited.add(currentCheat)
                remainingCameFrom[nextCheat] = currentCheat
                currentCheat = nextCheat
            }

            return findPath(currentCheat).also { resetRemaining() }
        }

        private fun findPath(start: Position): List<Position>? {
            remainingQueue.add(start)

            while (remainingQueue.isNotEmpty()) {
                val current = remainingQueue.removeFirst()
                remainingVisited.add(current)

                if (current == end) {
                    return reconstructPath(current)
                }

                val neighbours = connectionsFrom(current)
                for (neighbour in neighbours) {
                    remainingVisited.add(neighbour) // eagerly add to not step on it from other nodes
                    remainingCameFrom[neighbour] = current
                    remainingQueue.add(neighbour)
                }
            }

            return null
        }

        private fun reconstructPath(end: Position): List<Position> =
            partialPath + remainingCameFrom.reconstructPathFromParentsMap(end)

        private fun connectionsFrom(current: Position): List<Position> {
            val result = ArrayList<Position>()

            for (dir in Direction.entriesCardinal) {
                val neighbours = current + dir
                if (isVisited(neighbours)) continue
                if (racetrack[neighbours]!! == WALL) continue
                result.add(neighbours)
            }

            return result
        }

        fun isVisited(pos: Position): Boolean =
            pos in partialVisited || pos in remainingVisited

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
