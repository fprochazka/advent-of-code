package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.*
import java.util.*

fun Resource.day16(): Day16 = Day16(
    Day16.toGraph(matrix2d())
)

data class Day16(val maze: Graph<Char>) {

    init {
        eliminateDeadEnds()
    }

    val mazeStartAndEnd by lazy { maze.startAndEnd() }

    val dijkstraShortestPath by lazy {
        maze.anyShortestPath()!!
    }

    val result1 by lazy { dijkstraShortestPath.cost }
    val result2 by lazy { maze.allShortestPaths(dijkstraShortestPath) }

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

    data class GraphPathStep(val pos: Position, val inDir: Direction, val cost: Long, val prev: GraphPathStep? = null) {
        override fun toString(): String = "($inDir -> $pos, cost=$cost, prev=${prev?.pos})"
    }

    fun Graph<Char>.anyShortestPath(): GraphPathStep? {
        val (start, end) = mazeStartAndEnd

        val queue = PriorityQueue<GraphPathStep>(compareBy { it.cost })
        queue.add(GraphPathStep(start, Direction.RIGHT, 0))

        val visited = mutableSetOf<Position>()
        var pathToEnd: GraphPathStep? = null

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()!!
            visited.add(currentStep.pos)

            if (currentStep.pos == end) {
                pathToEnd = currentStep
                break
            }

            val neighbours = connectionsFrom(currentStep.pos)
                .filter { it != currentStep.prev?.pos } // no 180 flips
                .filter { it !in visited }
                .map { currentStep.pos.relativeDirectionTo(it)!! to it }

            for ((neighbourDir, neighbourPos) in neighbours) {
                val newCost = currentStep.cost + 1 + turnCost(currentStep.inDir, neighbourDir)

                queue.add(GraphPathStep(neighbourPos, neighbourDir, newCost, prev = currentStep))
            }
        }

        return pathToEnd
    }

    fun Graph<Char>.countOfAllPositionsOnAllShortestPaths(someShortestPath: GraphPathStep): Long {
        val allShortestPaths = allShortestPaths(someShortestPath)

        allShortestPaths.forEach { path ->
            maze.toPlainMatrix().also {
                path.forEach { pos -> it[pos] = 'o' }

                println()
                println("One of shortest paths:")
                print(it.toString())
            }
        }

        val positionsOnShortestPaths = allShortestPaths.flatten().toSet()

        maze.toPlainMatrix().also {
            positionsOnShortestPaths.forEach { pos -> it[pos] = 'O' }

            println()
            println("All positions from all shortest paths:")
            print(it.toString())
        }

        return positionsOnShortestPaths.size.toLong()
    }

    fun Graph<Char>.allShortestPaths(someShortestPath: GraphPathStep): List<List<Position>> {
        val (start, end) = mazeStartAndEnd

        data class Step(val pos: Position, val inDir: Direction, val cost: Long, val prev: Step? = null) {
            override fun toString(): String = "($inDir -> $pos, cost=$cost, prev=${prev?.pos})"
        }

        data class NodeState(var cost: Long = INFINITE_COST, var parents: MutableList<Position> = mutableListOf())

        val state = Matrix.of(maze.nodes.dims) { NodeState() }

        fun shortestPathsTo(pos: Position): List<List<Position>> {
            return buildList {
                fun backtrackPaths(pos: Position, path: List<Position>) {
                    if (pos == start) {
                        add(path.reversed())
                        return
                    }
                    for (parent in state[pos]!!.parents) {
                        backtrackPaths(parent, path + parent)
                    }
                }

                backtrackPaths(pos, mutableListOf())
            }
        }

        fun printState(step: Step) {
            maze.toPlainMatrix().also { matrix ->
                generateSequence(step) { it.prev }.forEach {
                    matrix[it.pos] = 'o'
                }
                matrix[step.pos] = 'O'

                println()
                print(matrix.toString())
            }
        }

        val firstStep = Step(start, Direction.RIGHT, 0)

        state[start]!!.let {
            it.cost = 0
            it.parents.add(start)
        }

        val queue = PriorityQueue<Step>(compareBy { it.cost })
        queue.add(firstStep)

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()!!
            val currentState = state[currentStep.pos]!!

            println()
            println("================ INSPECTING ${currentStep.pos} with current cost of ${currentStep.cost} ================")
            printState(currentStep)

            if (currentStep.cost > currentState.cost) {
                println("Current step cost is greater than current state cost of ${currentState.cost} => skip")
                continue
            }

            val neighbours = connectionsFrom(currentStep.pos)
                .filter { it != currentStep.prev?.pos }
                .map { currentStep.pos.relativeDirectionTo(it)!! to it }
            println("Neighbours: $neighbours")
            for ((neighbourDir, neighbourPos) in neighbours) {
                val newCost = currentStep.cost + 1 + turnCost(currentStep.inDir, neighbourDir)

                val neighbourStep = Step(neighbourPos, neighbourDir, newCost, prev = currentStep)
                queue.add(neighbourStep)

                val neighbourState = state[neighbourPos]!!
                if (newCost < neighbourState.cost) {
                    // new single shortest path
                    neighbourState.let {
                        it.cost = newCost
                        it.parents = mutableListOf(currentStep.pos)
                    }

                } else if (newCost == neighbourState.cost) {
                    // newCost is equal => additional shortest path
                    neighbourState.parents.add(currentStep.pos)
                }
            }
        }

        println("================ DONE")

        return shortestPathsTo(end)
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
