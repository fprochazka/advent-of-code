package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.MatrixGraph.PathStep
import aoc.utils.d2.Position

fun Resource.day16(): Day16 = Day16(
    Day16.toGraph(matrix2d())
)

data class Day16(val maze: MatrixGraph<Char>) {

    init {
        eliminateDeadEnds()
    }

    val mazeStartAndEnd by lazy { maze.startAndEnd() }

    val result1 by lazy { maze.shortestPathCost() }
    val result2 by lazy { maze.countOfAllPositionsOnAllShortestPaths() }

    fun eliminateDeadEnds(): MatrixGraph<Char> {
        val nodesByConnections = mutableMapOf<Int, MutableSet<Position>>().apply {
            put(1, mutableSetOf())
            put(2, mutableSetOf())
            put(3, mutableSetOf())
            put(4, mutableSetOf())
        }
        for ((pos, node) in maze.nodes.entries) {
            if (node.weightedConnections.isNotEmpty()) {
                nodesByConnections[node.weightedConnections.size]!!.add(pos)
            }
        }

        while (nodesByConnections[1]!!.isNotEmpty()) {
            val nodesWithOneConnection = nodesByConnections[1]!!

            for (deadEndPos in nodesWithOneConnection.toMutableList()) {
                val deadEndNode = maze.nodes[deadEndPos]!!
                val previousNode = maze.nodes[deadEndNode.connections.first()]!!

                if (deadEndNode.value == START || deadEndNode.value == END) {
                    nodesWithOneConnection.remove(deadEndPos)
                    continue
                }

                deadEndNode.value = DEAD_END

                // remove from cache
                nodesByConnections[previousNode.weightedConnections.size]!!.remove(previousNode.position)
                nodesWithOneConnection.remove(deadEndPos)

                // update connections
                deadEndNode.weightedConnections.clear()
                previousNode.weightedConnections.remove(deadEndPos)

                // put prev node back in cache
                nodesByConnections[previousNode.weightedConnections.size]!!.add(previousNode.position)
            }
        }

        return maze
    }

    fun MatrixGraph<Char>.shortestPathCost(): Long =
        mazeStartAndEnd
            .let { (start, end) -> anyShortestPath(start, Direction.RIGHT, end, ::edgeCost) }
            ?.pathCost
            ?: error("No path found")

    fun MatrixGraph<Char>.countOfAllPositionsOnAllShortestPaths(): Long {
        val positionsOnShortestPaths = mazeStartAndEnd
            .let { (start, end) -> allShortestPaths(start, Direction.RIGHT, end, ::edgeCost) }
            .flatten()
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
                        START, EMPTY, END -> true to 1
                        else -> false to MatrixGraph.INFINITE_COST
                    }

                    else -> false to MatrixGraph.INFINITE_COST
                }
            }

    }

}
