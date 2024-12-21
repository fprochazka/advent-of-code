package aoc.utils.d2.path

import aoc.utils.d2.AreaDimensions
import aoc.utils.d2.Position
import aoc.utils.d2.PositionMap

class GraphPathParents(dims: AreaDimensions) {

    val cameFrom = PositionMap<Position>(dims)

    fun getPathOf(current: Position): List<Position> =
        cameFrom.reconstructPathFromParentsMap(current)

    operator fun set(pos: Position, parent: Position) {
        cameFrom[pos] = parent
    }

}

fun Map<Position, Position>.reconstructPathFromParentsMap(current: Position): List<Position> {
    val path = mutableListOf<Position>()
    var node: Position? = current
    while (node != null) {
        path.add(node)
        node = this[node]
    }
    return path.reversed()
}
