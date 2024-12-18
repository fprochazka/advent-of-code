package aoc.utils.d2.graph

import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position

fun <V : Any> MatrixGraph<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
): List<Position>? {
    val queue = ArrayDeque<Position>().apply { add(start) }
    val visited = mutableSetOf<Position>().apply { add(start) }
    val parents = mutableMapOf<Position, Position>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        visited.add(current)

        if (current == end) {
            val path = mutableListOf<Position>()
            var node: Position? = end
            while (node != null) {
                path.add(node)
                node = parents[node]
            }
            return path.reversed()
        }

        val neighbours = connectionsOf(current)
            .filter { it !in visited }

        for (neighbour in neighbours) {
            visited.add(neighbour)
            parents[neighbour] = current
            queue.add(neighbour)
        }
    }

    return null
}
