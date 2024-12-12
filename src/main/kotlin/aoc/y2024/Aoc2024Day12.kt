package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.OrientedPosition
import aoc.utils.d2.Position

fun main() {
    solve(Resource.named("aoc2024/day12/example1.txt"))
    solve(Resource.named("aoc2024/day12/example2.txt"))
    solve(Resource.named("aoc2024/day12/example3.txt"))
    solve(Resource.named("aoc2024/day12/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day12()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day12(): Day12 = Day12(
    Matrix.from(charMatrix())
)

data class Day12(val rawField: Matrix<Char>) {

    val cost by lazy { costOfFence() }

    val result1 by lazy { cost.first }
    val result2 by lazy { cost.second }

    fun costOfFence(): Pair<Long, Long> {
        val fieldGraph = rawFieldToGraph()

        fun <V> MutableSet<V>.popFirst(): V = first().also { remove(it) }

        var totalFencingCostTask1 = 0L
        var totalFencingCostTask2 = 0L

        val remainingPositions = fieldGraph.nodes.allPositions().toMutableSet()
        while (remainingPositions.isNotEmpty()) {
            val nodePositionsForFencing = mutableMapOf<Position, Int>()
            val uniquePositionsInArea = mutableSetOf<Position>()

            val startingNode = fieldGraph[remainingPositions.popFirst()]!!

            val neighboursQueue = ArrayDeque<Graph<Char>.Node>()
            neighboursQueue.add(startingNode)

            while (neighboursQueue.isNotEmpty()) {
                val node = neighboursQueue.removeFirst()

                // remove from candidates to find separate areas
                remainingPositions.remove(node.position)

                if (!uniquePositionsInArea.add(node.position)) {
                    continue // already visited
                }

                val neighbours = node.connections()
                neighbours.forEach { neighbour ->
                    if (neighbour.position !in uniquePositionsInArea) {
                        neighboursQueue.addLast(neighbour)
                    }
                }

                if (neighbours.size < 4) {
                    nodePositionsForFencing[node.position] = 4 - neighbours.size
                }
            }

            totalFencingCostTask1 += (uniquePositionsInArea.size * nodePositionsForFencing.values.sum()).toLong()

            val fenceBetweenPositions = nodePositionsForFencing.keys
                .flatMap { nodePosition ->
                    fieldGraph[nodePosition]!!.vacantSidesIncludingOutOfMatrix()
                        .map { side -> nodePosition to side.direction }
                }
                .toMutableSet()

            var uniqueFenceSides = 0
            while (fenceBetweenPositions.isNotEmpty()) {
                for (fence in fenceBetweenPositions) {
                    uniqueFenceSides++
                    fenceBetweenPositions.remove(fence)

                    val (nodePosition, fenceSide) = fence
                    for (directionAlongTheFence in listOf(fenceSide.turnRight90(), fenceSide.turnLeft90())) {
                        var siblingNode = nodePosition + directionAlongTheFence
                        while (fenceBetweenPositions.remove(siblingNode to fenceSide)) {
                            siblingNode = siblingNode + directionAlongTheFence
                        }
                    }

                    break; // fresh iterator
                }
            }

            totalFencingCostTask2 += uniquePositionsInArea.size * uniqueFenceSides
        }

        return totalFencingCostTask1 to totalFencingCostTask2;
    }

    fun OrientedPosition.turnRight90(): OrientedPosition = OrientedPosition(position, direction.turnRight90())

    fun rawFieldToGraph(): Graph<Char> {
        val graph = Graph<Char>(rawField.width, rawField.height)

        for ((position, value) in rawField.allEntries()) {
            graph[position] = value
        }

        for (position in rawField.allPositions()) {
            val node = graph[position]!!

            for (neighbourPosition in node.neighbourPositionsValid()) {
                val candidateNode = graph[neighbourPosition.position]!!
                if (candidateNode.value == node.value) {
                    graph.edges.getOrPut(node) { mutableSetOf() }.add(candidateNode)
                }
            }
        }

        return graph
    }

    class Graph<V : Any>(width: Int, height: Int) {

        val nodes = Matrix.empty<Node>(width, height)

        // without direction
        val edges = mutableMapOf<Node, MutableSet<Node>>()

        operator fun set(position: Position, value: V) {
            nodes[position] = Node(value, position)
        }

        operator fun get(position: Position): Node? {
            return nodes[position]
        }

        inner class Node(
            val value: V,
            val position: Position,
        ) {

            fun connections(): Set<Node> =
                edges[this] ?: emptySet()

            fun vacantSidesIncludingOutOfMatrix(): Iterable<OrientedPosition> {
                val result = mutableSetOf<OrientedPosition>()
                val neighbours = connections().map { it.position }

                for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix()) {
                    if (neighbourPosition.position !in neighbours) {
                        result.add(neighbourPosition)
                    }
                }

                return result
            }

            fun vacantSidesValid(): Iterable<OrientedPosition> =
                vacantSidesIncludingOutOfMatrix().filter { it.position in nodes }

            fun neighbourPositionsIncludingOutOfMatrix(): Iterable<OrientedPosition> =
                neighbourSides.map { OrientedPosition(position + it, it) }

            fun neighbourPositionsValid(): Iterable<OrientedPosition> =
                neighbourPositionsIncludingOutOfMatrix().filter { it.position in nodes }

            override fun toString(): String = "'$value' at $position"

        }

        companion object {

            val neighbourSides = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

        }

    }

}

