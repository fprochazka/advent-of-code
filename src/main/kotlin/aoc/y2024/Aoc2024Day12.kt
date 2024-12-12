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
            val fenceBetweenPositions = mutableSetOf<Pair<Position, Direction>>()
            val uniquePositionsInArea = mutableSetOf<Position>()

            val startingNode = fieldGraph[remainingPositions.popFirst()]!!

            val neighboursQueue = ArrayDeque<Graph<Char>.Node>()
            neighboursQueue.add(startingNode)

            while (neighboursQueue.isNotEmpty()) { // O(n + n*4 + n*4 + n*4) for subset of the graph, there are no overlaps with the other subsets
                val node = neighboursQueue.removeFirst()

                // remove from candidates to find separate areas
                remainingPositions.remove(node.position)

                if (!uniquePositionsInArea.add(node.position)) {
                    continue // already visited
                }

                val neighbours = node.connections() // O(4)
                neighbours // O(4)
                    .filter { neighbour -> neighbour.position !in uniquePositionsInArea }
                    .forEach { neighbour -> neighboursQueue.addLast(neighbour) }

                if (neighbours.size < 4) {
                    for (vacantSide in node.vacantSidesIncludingOutOfMatrix()) { // O(4)
                        fenceBetweenPositions.add(node.position to vacantSide.direction)
                    }
                }
            }

            totalFencingCostTask1 += (uniquePositionsInArea.size * fenceBetweenPositions.size).toLong()

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

    fun rawFieldToGraph(): Graph<Char> {
        val graph = Graph<Char>(rawField.width, rawField.height)

        for ((position, value) in rawField.allEntries()) { // O(n)
            graph[position] = value
        }

        for (position in rawField.allPositions()) { // O(n*4)
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

            fun connections(): Set<Node> = // O(1)
                edges[this] ?: emptySet()

            fun vacantSidesIncludingOutOfMatrix(): Iterable<OrientedPosition> { // O(4 + 4)
                val result = mutableSetOf<OrientedPosition>()
                val neighbours = connections().map { it.position }.toSet()  // O(4)

                for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix()) {  // O(4)
                    if (neighbourPosition.position !in neighbours) {
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

        }

    }

}

