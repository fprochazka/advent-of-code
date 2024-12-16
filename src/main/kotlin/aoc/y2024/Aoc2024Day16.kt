package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.popAny
import aoc.utils.d2.*
import aoc.y2024.Day16.PathStep.Companion.DEAD_END

fun Resource.day16(): Day16 = Day16(
    Day16.Graph.ofChars(matrix2d()) { a, b ->
        when (a.value) {
            'S', '.', 'E' -> when (b.value) {
                'S', '.', 'E' -> true
                else -> false
            }

            else -> false
        }
    }
)

data class Day16(val maze: Graph<Char>) {

    val result1 by lazy { eliminateDeadEnds().bestCostOfMaze() }
//    val result2: String by lazy { TODO() }

    fun eliminateDeadEnds(): Graph<Char> {
        // TODO: copy?

        val nodesByConnections = mutableMapOf<Int, MutableSet<Position>>().apply {
            put(1, mutableSetOf())
            put(2, mutableSetOf())
            put(3, mutableSetOf())
            put(4, mutableSetOf())
        }
        for ((pos, node) in maze.nodes.entries) {
            if (node.connections.isNotEmpty()) {
                nodesByConnections[node.connections.size]!!.add(pos)
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
                nodesByConnections[previousNode.connections.size]!!.remove(previousNode.position)
                nodesWithOneConnection.remove(deadEndPos)

                // update connections
                deadEndNode.connections.clear()
                previousNode.connections.remove(deadEndPos)

                // put prev node back in cache
                nodesByConnections[previousNode.connections.size]!!.add(previousNode.position)
            }
        }

        maze.printPath()

        return maze
    }

    fun Graph<Char>.bestCostOfMaze(): Long {
        val startAndEnd = nodes
            .allPositionsByValues { it.value == 'S' || it.value == 'E' }
            .map { entry -> entry.key.value to entry.key }
            .toMap()
        val start = startAndEnd['S']!!.position
        val end = startAndEnd['E']!!.position

        var minCost = Long.MAX_VALUE

        val visitedPositions = mutableSetOf<Position>()

        // start on the Start Tile (marked S) facing East (RIGHT)
        var dfsPath = ArrayDeque<PathStep>()
        dfsPath.add(
            PathStep(start, Direction.RIGHT)
        )

        while (dfsPath.isNotEmpty()) {
            val currentStep = dfsPath.last()
            visitedPositions.add(currentStep.pos)

            if (currentStep.pos == end) {
                minCost = currentStep.cost
                printPath(dfsPath, currentStep.cost)

                dfsPath.removeLast()
                visitedPositions.remove(currentStep.pos)
                continue
            }

            val nextStep = currentStep.nextStep(this, visitedPositions)
            if (nextStep != null) {
                if (nextStep.cost < minCost) {
                    dfsPath.add(nextStep)
                }
            } else {
                dfsPath.removeLast()
                visitedPositions.remove(currentStep.pos)
            }
        }

        return minCost
    }

    data class PathStep(
        val pos: Position,
        val startingDir: Direction,
        val cost: Long = 0L,
        val remainingDirectionsToTry: MutableSet<Direction> = directions.toMutableSet(),
    ) {

        fun nextStep(maze: Graph<Char>, visited: MutableSet<Position>): PathStep? {
            // They can move forward one tile at a time (increasing their score by 1 point), but never into a wall (#).
            // They can also rotate clockwise or counterclockwise 90 degrees at a time (increasing their score by 1000 points).

            while (remainingDirectionsToTry.isNotEmpty()) {
                val nextDir = remainingDirectionsToTry.popAny()

                val nextPos = pos + nextDir
                if (nextPos in visited) {
                    continue
                }

                val nextNode = maze.nodes[nextPos]
                if (nextNode == null || nextNode.value == WALL || nextNode.value == DEAD_END) {
                    continue
                }

                val turnCost = turnCosts[startingDir]!![nextDir]!!

                return PathStep(
                    nextPos,
                    nextDir,
                    cost + 1 + turnCost,
                    directions.toMutableSet(),
                )
            }

            return null
        }

        companion object {

            val directions = listOf(Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.DOWN)

            val turnCosts: Map<Direction, Map<Direction, Long>> = buildMap {
                for (from in directions) {
                    put(
                        from,
                        mutableMapOf<Direction, Long>().apply {
                            put(from, 0)
                            put(from.turnRight90(), 1000)
                            put(from.turnLeft90(), 1000)
                            put(from.turnLeft90().turnLeft90(), 2000)
                        }
                    )
                }
            }

            const val WALL = '#'
            const val DEAD_END = 'x'

        }

    }

    fun Graph<Char>.printPath(path: Collection<PathStep> = setOf(), cost: Long = 0L) {
        val graphMatrix = this.nodes
        val visitedMaze = Matrix.of<Char>(nodes.dims) { '.' }.apply {
            for ((pos, node) in graphMatrix.entries) {
                this[pos] = node.value
            }
        }
        path.forEach { step ->
            when (step.startingDir) {
                Direction.RIGHT -> visitedMaze[step.pos] = '>'
                Direction.LEFT -> visitedMaze[step.pos] = '<'
                Direction.UP -> visitedMaze[step.pos] = '^'
                Direction.DOWN -> visitedMaze[step.pos] = 'v'
                else -> {}
            }
        }

        println()
        print(visitedMaze.toString())
        println("Cost: $cost")
    }

    class Graph<V : Any>(dims: Dimensions) {

        val nodes: Matrix<Node> = Matrix.empty<Node>(dims)

        operator fun set(position: Position, value: V) {
            nodes[position] = Node(value, position)
        }

        operator fun get(position: Position): Node? =
            nodes[position]

        inner class Node(
            var value: V,
            val position: Position,
        ) {

            val connections = mutableSetOf<Position>()

            fun vacantSidesIncludingOutOfMatrix(): Iterable<OrientedPosition> { // O(4 + 4)
                val result = mutableSetOf<OrientedPosition>()

                for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix()) {  // O(4)
                    if (neighbourPosition.position !in connections) {
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

            fun ofChars(matrix: Resource.CharMatrix2d, edgeFilter: (Graph<Char>.Node, Graph<Char>.Node) -> Boolean): Graph<Char> {
                val graph = Graph<Char>(matrix.dims)

                for ((position, value) in matrix.entries) {
                    graph[position] = value
                }

                for (position in graph.nodes.positions) {
                    val node = graph[position]!!

                    for (neighbourPosition in node.neighbourPositionsValid()) {
                        val candidate = graph[neighbourPosition.position]!!
                        if (edgeFilter(node, candidate)) {
                            node.connections.add(candidate.position)
                        }
                    }
                }

                return graph
            }

        }

    }


}
