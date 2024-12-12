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
    Day12.Graph.from(charMatrix()) { a, b -> a.value == b.value }
)

data class Day12(val fieldGraph: Graph<Char>) {

    val areasByCrop by lazy { fieldGraph.groupToConnectedComponents() }

    val fencedAreas by lazy {
        buildList {
            for (area in areasByCrop) {
                val fenceBetweenPositions = area.nodes
                    .flatMap { node ->
                        node.vacantSidesIncludingOutOfMatrix()
                            .map { vacantSide -> node.position to vacantSide.direction }
                    }
                    .toSet()

                add(area to fenceBetweenPositions)
            }
        }
    }

    val result1 by lazy {
        fencedAreas.sumOf { (area, fenceBetweenPositions) ->
            (area.size * fenceBetweenPositions.size).toLong()
        }
    }

    val result2 by lazy {
        fencedAreas.sumOf { (area, fenceBetweenPositions) ->
            (area.size * uniqueFenceSides(fenceBetweenPositions)).toLong()
        }
    }

    fun uniqueFenceSides(fenceBetweenPositions: Set<Pair<Position, Direction>>): Int {
        var fences = fenceBetweenPositions.toMutableSet()

        var uniqueFenceSides = 0
        while (fences.isNotEmpty()) {
            for (fence in fences) {
                uniqueFenceSides++
                fences.remove(fence)

                val (nodePosition, fenceSide) = fence
                for (directionAlongTheFence in listOf(fenceSide.turnRight90(), fenceSide.turnLeft90())) {
                    var siblingNode = nodePosition + directionAlongTheFence
                    while (fences.remove(siblingNode to fenceSide)) {
                        siblingNode = siblingNode + directionAlongTheFence
                    }
                }

                break; // fresh iterator
            }
        }

        return uniqueFenceSides
    }

    class Graph<V : Any>(width: Int, height: Int) {

        val nodes = Matrix.empty<Node>(width, height)

        operator fun set(position: Position, value: V) {
            nodes[position] = Node(value, position)
        }

        operator fun get(position: Position): Node? =
            nodes[position]

        fun groupToConnectedComponents(): List<Component> {
            fun <V> MutableSet<V>.popFirst(): V = first().also { remove(it) }

            val result = mutableListOf<Component>()

            val remainingPositions = nodes.allPositions().toMutableSet()
            while (remainingPositions.isNotEmpty()) {
                val connectedComponent = mutableSetOf<Position>()

                val neighboursQueue = ArrayDeque<Position>()
                neighboursQueue.add(remainingPositions.popFirst())

                while (neighboursQueue.isNotEmpty()) {
                    val position = neighboursQueue.removeFirst()

                    remainingPositions.remove(position) // remove from candidates to find separate areas

                    if (!connectedComponent.add(position)) {
                        continue // already visited
                    }

                    nodes[position]!!.connections
                        .filter { it !in connectedComponent }
                        .forEach { neighboursQueue.addLast(it) }
                }

                result.add(Component(connectedComponent))
            }

            return result
        }

        inner class Component(val positions: Set<Position>) {

            val nodes: List<Node>
                get() = positions.map { this@Graph.nodes[it]!! }

            val size: Int
                get() = positions.size

        }

        inner class Node(
            val value: V,
            val position: Position,
        ) {

            val connections = mutableSetOf<Position>()

            val connectedNodes: List<Node>
                get() = connections.map { nodes[it]!! }

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

            fun <R : Any> from(cells: Map<Position, R>, edgeFilter: (Graph<R>.Node, Graph<R>.Node) -> Boolean): Graph<R> {
                val width = cells.keys.maxOf { it.x } + 1
                val height = cells.keys.maxOf { it.y } + 1

                val graph = Graph<R>(width, height)

                for ((position, value) in cells.entries) {
                    graph[position] = value
                }

                for (position in graph.nodes.allPositions()) {
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
