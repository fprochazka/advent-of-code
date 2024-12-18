package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.createDeadEndEliminator
import aoc.utils.d2.graph.path.PathStep
import aoc.utils.d2.graph.path.allShortest.allShortestPathsModifiedDijkstra
import aoc.utils.d2.graph.path.anyShortest.anyShortestPathDijkstra

fun Resource.day16(): Day16 = Day16(
    Day16.toGraph(matrix2d())
)

data class Day16(val maze: MatrixGraph<Char>) {

    init {
        maze
            .createDeadEndEliminator(DEAD_END) { it.value != START && it.value != END }
            .runEliminationRound()
    }

    val mazeStartAndEnd by lazy { maze.startAndEnd() }

    val result1 by lazy { maze.shortestPathCost() }
    val result2 by lazy { maze.countOfAllPositionsOnAllShortestPaths() }

    fun MatrixGraph<Char>.shortestPathCost(): Long =
        mazeStartAndEnd
            .let { (start, end) -> anyShortestPathDijkstra(start, Direction.RIGHT, end, ::edgeCost) }
            ?.pathCost
            ?: error("No path found")

    fun MatrixGraph<Char>.countOfAllPositionsOnAllShortestPaths(): Long {
        val positionsOnShortestPaths = mazeStartAndEnd
            .let { (start, end) -> allShortestPathsModifiedDijkstra(start, Direction.RIGHT, end, ::edgeCost) }
            .flatMap { it.toList().map { it.pos } }
            .toSet()

        return positionsOnShortestPaths.size.toLong()
    }

    fun MatrixGraph<Char>.startAndEnd(): Pair<Position, Position> =
        allPositionsByValues { it == START || it == END }
            .let { it[START]!!.single() to it[END]!!.single() }

    companion object {

        const val EMPTY = '.'
        const val START = 'S'
        const val END = 'E'
        const val WALL = '#'
        const val DEAD_END = 'x'

        fun edgeCost(current: PathStep, inDir: Direction): Long =
            1 + turnCost(current.inDir, inDir)

        fun turnCost(from: Direction, to: Direction): Long =
            turnCosts[from]!![to]!!

        val turnCosts: Map<Direction, Map<Direction, Long>> = buildMap {
            for (from in Direction.entriesCardinal) {
                val costs = mutableMapOf<Direction, Long>().apply {
                    put(from, 0)
                    put(from.turnRight90(), 1000)
                    put(from.turnLeft90(), 1000)
                    put(from.turnLeft90().turnLeft90(), 2000)
                }

                put(from, costs)
            }
        }

        fun toGraph(matrix: Resource.CharMatrix2d): MatrixGraph<Char> =
            MatrixGraph.of(matrix, Direction.entriesCardinal, { it }) { a, b ->
                when (a.value) {
                    START, EMPTY, END -> when (b.value) {
                        START, EMPTY, END -> true
                        else -> false
                    }

                    else -> false
                }
            }

    }

}
