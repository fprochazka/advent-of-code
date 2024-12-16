package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.*
import java.util.*

fun Resource.day16(): Day16 = Day16(
    Day16.toGraph(matrix2d())
)

data class Day16(val maze: Graph<Char>) {

    val results by lazy {
        eliminateDeadEnds().dijkstra()
    }

    val result1 by lazy { results.first }
    val result2 by lazy { results.second }

    fun eliminateDeadEnds(): Graph<Char> {
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

                if (deadEndNode.value == 'S' || deadEndNode.value == 'E') {
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

        maze.toPlainMatrix().also {
            println()
            print(it.toString())
        }

        return maze
    }

    fun Graph<Char>.dijkstra(): Pair<Long, Long> {
        val (start, end) = this.startAndEnd()

        data class Step(val pos: Position, val inDir: Direction, val cost: Long)

        val distancesFromStart = mutableMapOf<Position, Long>()
        val shortestPaths = mutableMapOf<Position, MutableList<List<Position>>>()

        nodes.entries
            .filter { (_, node) -> node.value == EMPTY || node.value == END || node.value == START }
            .forEach { (pos, _) ->
                distancesFromStart[pos] = INFINITE_COST
                shortestPaths[pos] = mutableListOf()
            }

        val visited = mutableSetOf<Position>()

        shortestPaths[start]!!.add(listOf(start))

        val queue = PriorityQueue<Step>(compareBy { it.cost })
        queue.add(Step(start, Direction.RIGHT, 0))

        while (queue.isNotEmpty()) {
            val current = queue.poll()!!
            visited.add(current.pos)

            for (neighbour in connectionsFrom(current.pos)) {
                if (neighbour in visited) {
                    continue
                }

                val neighbourDir = current.pos.relativeDirectionTo(neighbour)!!
                val newCost = current.cost + 1 + turnCost(current.inDir, neighbourDir)
                val nextStep = Step(neighbour, neighbourDir, newCost)

                val oldCost = distancesFromStart[neighbour]!!
                if (newCost < oldCost) {
                    // new shortest path
                    distancesFromStart[neighbour] = newCost
                    shortestPaths[neighbour]!!.clear()
                    for (pathToCurrent in shortestPaths[current.pos]!!) {
                        shortestPaths[neighbour]!!.add(pathToCurrent + nextStep.pos)
                    }

                    queue.add(nextStep)

                } else if (newCost == oldCost) {
                    // additional shortest path
                    for (pathToCurrent in shortestPaths[current.pos]!!) {
                        shortestPaths[neighbour]!!.add(pathToCurrent + nextStep.pos)
                    }

                    queue.add(nextStep)
                }
            }
        }

        shortestPaths[end]!!.forEach { path ->
            maze.toPlainMatrix().also {
                path.forEach { pos -> it[pos] = 'o' }

                println()
                println("One of shortest paths:")
                print(it.toString())
            }
        }

        val positionsOnShortestPaths = shortestPaths[end]!!
            .flatten()
            .toSet()

        maze.toPlainMatrix().also {
            positionsOnShortestPaths.forEach { pos -> it[pos] = 'O' }

            println()
            println("All positions from all shortest paths:")
            print(it.toString())
        }

        return distancesFromStart[end]!! to positionsOnShortestPaths.size.toLong()
    }

    fun Graph<Char>.startAndEnd(): Pair<Position, Position> =
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

        const val INFINITE_COST = (Long.MAX_VALUE / 2)

        val allowedDirections = listOf(Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.DOWN)

        fun turnCost(from: Direction, to: Direction): Long =
            turnCosts[from]!![to]!!

        val turnCosts: Map<Direction, Map<Direction, Long>> = buildMap {
            for (from in allowedDirections) {
                val costs = mutableMapOf<Direction, Long>().apply {
                    put(from, 0)
                    put(from.turnRight90(), 1000)
                    put(from.turnLeft90(), 1000)
                    put(from.turnLeft90().turnLeft90(), 2000)
                }

                put(from, costs)
            }
        }

        fun toGraph(matrix: Resource.CharMatrix2d): Graph<Char> =
            Graph.ofChars(matrix) { a, b ->
                when (a.value) {
                    START, EMPTY, END -> when (b.value) {
                        START, EMPTY, END -> true to 1
                        else -> false to INFINITE_COST
                    }

                    else -> false to INFINITE_COST
                }
            }

    }

    class Graph<V : Any>(dims: Dimensions) {

        val nodes: Matrix<Node> = Matrix.empty<Node>(dims)

        operator fun set(position: Position, value: V) {
            nodes[position] = Node(value, position)
        }

        operator fun get(position: Position): Node? =
            nodes[position]

        fun connectionsFrom(pos: Position): List<Position> =
            nodes[pos]?.connections ?: emptyList()

        fun toPlainMatrix(): Matrix<V> = let { graph ->
            Matrix.empty<V>(nodes.dims).also { copy ->
                for ((pos, node) in graph.nodes.entries) {
                    copy[pos] = node.value
                }
            }
        }

        inner class Node(
            var value: V,
            val position: Position,
        ) {

            // position to weight
            val weightedConnections = mutableMapOf<Position, Long>()

            val connections
                get() = weightedConnections.keys.toList()

            fun vacantSidesIncludingOutOfMatrix(): Iterable<OrientedPosition> { // O(4 + 4)
                val result = mutableSetOf<OrientedPosition>()

                for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix()) {  // O(4)
                    if (neighbourPosition.position !in weightedConnections) {
                        result.add(neighbourPosition)
                    }
                }

                return result
            }

            fun vacantSidesValid(): Iterable<OrientedPosition> = // O(4)
                vacantSidesIncludingOutOfMatrix().filter { it.position in nodes }

            fun neighbourPositionsIncludingOutOfMatrix(): Iterable<OrientedPosition> = // O(4)
                neighbourSides.map { OrientedPosition(position + it, it) }

            fun neighbourPositionsValid(): Iterable<OrientedPosition> = // O(4)
                neighbourPositionsIncludingOutOfMatrix().filter { it.position in nodes }

            override fun toString(): String = "'$value' at $position"

        }

        companion object {

            val neighbourSides = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

            fun ofChars(matrix: Resource.CharMatrix2d, edgeFilter: (Graph<Char>.Node, Graph<Char>.Node) -> Pair<Boolean, Long>): Graph<Char> {
                val graph = Graph<Char>(matrix.dims)

                for ((position, value) in matrix.entries) {
                    graph[position] = value
                }

                for (position in graph.nodes.positions) {
                    val node = graph[position]!!

                    for (neighbourPosition in node.neighbourPositionsValid()) {
                        val candidate = graph[neighbourPosition.position]!!
                        val (isConnected, weight) = edgeFilter(node, candidate)
                        if (isConnected) {
                            node.weightedConnections[candidate.position] = weight
                        }
                    }
                }

                return graph
            }

        }

    }

}
