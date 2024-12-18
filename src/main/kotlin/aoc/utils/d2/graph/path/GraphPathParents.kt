package aoc.utils.d2.graph.path

import aoc.utils.d2.Position

class GraphPathParents {

    val cameFrom = mutableMapOf<Position, Position>()

    fun getPathOf(current: Position): List<Position> {
        val path = mutableListOf<Position>()
        var node: Position? = current
        while (node != null) {
            path.add(node)
            node = cameFrom[node]
        }
        return path.reversed()
    }

    operator fun set(pos: Position, parent: Position) {
        cameFrom[pos] = parent
    }

}
