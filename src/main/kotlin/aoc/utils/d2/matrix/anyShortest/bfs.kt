package aoc.utils.d2.matrix.anyShortest

import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.PositionSet
import aoc.utils.d2.graph.path.GraphPathParents

fun <V : Any> Matrix<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
    edge: (Position, Position) -> Boolean,
): List<Position>? {
    val queue = ArrayDeque<Position>().apply { add(start) }
    val visited = PositionSet(dims).apply { add(start) }
    val cameFrom = GraphPathParents(dims)

    fun connectionsFrom(pos: Position): List<Position> = buildList(4) {
        for (connection in pos.neighboursCardinalIn(dims)) {
            if (connection in visited) continue
            if (!edge(pos, connection)) continue
            add(connection)
        }
    }

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        visited.add(current)

        if (current == end) {
            return cameFrom.getPathOf(current)
        }

        val neighbours = connectionsFrom(current)
        for (neighbour in neighbours) {
            visited.add(neighbour)
            cameFrom[neighbour] = current
            queue.add(neighbour)
        }
    }

    return null
}
