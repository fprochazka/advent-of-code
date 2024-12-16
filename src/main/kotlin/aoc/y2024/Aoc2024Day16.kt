package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.MatrixGraph.PathStep
import aoc.utils.d2.Position
import java.util.*

fun Resource.day16(): Day16 = Day16(
    Day16.toGraph(matrix2d())
)

data class Day16(val maze: MatrixGraph<Char>) {

    init {
        eliminateDeadEnds()
    }

    val mazeStartAndEnd by lazy { maze.startAndEnd() }

    val result1 by lazy { maze.anyShortestPath()!!.pathCost }
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

//        maze.toPlainMatrix().also {
//            println()
//            print(it.toString())
//        }

        return maze
    }

    fun MatrixGraph<Char>.anyShortestPath(): PathStep? {
        val (start, end) = mazeStartAndEnd

        val queue = PriorityQueue<PathStep>(compareBy { it.pathCost })
        queue.add(PathStep(start, Direction.RIGHT, 0))

        val visited = mutableSetOf<Position>()

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()!!
            visited.add(currentStep.pos)

            if (currentStep.pos == end) {
                return currentStep
            }

            val neighbours = connectionsOf(currentStep.pos)
                .filter { it != currentStep.prev?.pos } // no 180 flips
                .filter { it !in visited }
                .map { currentStep.pos.relativeDirectionTo(it)!! to it }

            for ((neighbourDir, neighbourPos) in neighbours) {
                queue.add(PathStep(neighbourPos, neighbourDir, stepCost = 1 + turnCost(currentStep.inDir, neighbourDir), prev = currentStep))
            }
        }

        return null
    }

    fun MatrixGraph<Char>.countOfAllPositionsOnAllShortestPaths(): Long {
        val positionsOnShortestPaths = allShortestPaths()
            .flatten()
            .toSet()

        return positionsOnShortestPaths.size.toLong()
    }

    fun MatrixGraph<Char>.allShortestPaths(): Sequence<List<Position>> = sequence {
        val (start, end) = mazeStartAndEnd

        val minCosts = Matrix.empty<MutableMap<Direction, PathStep>>(maze.nodes.dims)
        fun updateMinCost(step: PathStep) {
            minCosts[step.pos] = (minCosts[step.pos] ?: mutableMapOf()).also {
                it.merge(step.inDir, step, { prev, next -> if (next.pathCost < prev.pathCost) next else prev })
            }
        }

        fun getMinCostPath(step: PathStep): PathStep? =
            minCosts[step.pos]?.get(step.inDir)

        fun getMinCost(step: PathStep): Long =
            getMinCostPath(step)?.pathCost ?: MatrixGraph.INFINITE_COST

        var shortestPathCost = MatrixGraph.INFINITE_COST

        val queue = PriorityQueue<PathStep>(compareBy { it.pathCost })
        queue.add(PathStep(start, Direction.RIGHT, 0))

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()

            if (currentStep.pathCost > shortestPathCost) {
                // the queue is sorter, therefore once it starts returning "too long" results we know we can throw away the rest
                break
            }

            if (currentStep.pathCost > getMinCost(currentStep)) {
                continue
            }

            updateMinCost(currentStep)

            if (currentStep.pos == end) {
                shortestPathCost = minOf(shortestPathCost, currentStep.pathCost)

                yield(currentStep.toList().map { it.pos })
                continue
            }

            val neighbours = connectionsOf(currentStep.pos)
                .filter { it != currentStep.prev?.pos } // no 180 flips
                .map { currentStep.pos.relativeDirectionTo(it)!! to it }

            for ((neighbourDir, neighbourPos) in neighbours) {
                queue.add(PathStep(neighbourPos, neighbourDir, stepCost = 1 + turnCost(currentStep.inDir, neighbourDir), prev = currentStep))
            }
        }
    }

    fun MatrixGraph<Char>.startAndEnd(): Pair<Position, Position> =
        nodes
            .allPositionsByValues { it.value == 'S' || it.value == 'E' }
            .map { entry -> entry.key.value to entry.key }
            .toMap()
            .let { it['S']!!.position to it['E']!!.position }

    companion object {

        const val EMPTY = '.'
        const val START = 'S'
        const val END = 'E'
        const val WALL = '#'
        const val DEAD_END = 'x'

        fun edgeCost(current: PathStep, inDir: Direction): Long =
            current.pathCost + 1 + turnCost(current.inDir, inDir)

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
