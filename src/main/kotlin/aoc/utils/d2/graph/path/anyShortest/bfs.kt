package aoc.utils.d2.graph.path.anyShortest

import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.path.GraphPathParents

fun <V : Any> MatrixGraph<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
): List<Position>? {
    val queue = ArrayDeque<Position>().apply { add(start) }
    val visited = mutableSetOf<Position>().apply { add(start) }
    val cameFrom = GraphPathParents()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        visited.add(current)

        if (current == end) {
            return cameFrom.getPathOf(current)
        }

        val neighbours = connectionsFrom(current)
            .filter { it !in visited }

        for (neighbour in neighbours) {
            visited.add(neighbour)
            cameFrom[neighbour] = current
            queue.add(neighbour)
        }
    }

    return null
}
