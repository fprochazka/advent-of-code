package aoc.utils.d2.graph.path.anyShortest

import aoc.utils.containers.PriorityQueueSet
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.MatrixGraph.Companion.INFINITE_COST
import aoc.utils.d2.Position

fun <V : Any> MatrixGraph<V>.anyShortestPathAStar(
    start: Position,
    end: Position,
): List<Position>? {
    fun reconstructPath(cameFrom: Map<Position, Position>, current: Position): List<Position> {
        val path = mutableListOf<Position>()
        var node: Position? = current
        while (node != null) {
            path.add(node)
            node = cameFrom[node]
        }
        return path.reversed()
    }

    // Parent map to reconstruct path
    val cameFrom = mutableMapOf<Position, Position>()

    val distanceFromStart = Matrix.empty<Long>(nodes.dims).also { it[start] = 0L }
    val estRemainingCost = Matrix.empty<Long>(nodes.dims).also { it[start] = start.manhattanDistanceTo(end) }
    val queue = PriorityQueueSet<Position>().also { it[start] = estRemainingCost.getValue(start) }

    while (queue.isNotEmpty()) {
        val currentPos = queue.removeFirst()
        if (currentPos == end) {
            return reconstructPath(cameFrom, currentPos)
        }

        for (neighborPos in connectionsOf(currentPos)) {
            val stepCost = distanceFromStart.getOrDefault(currentPos, INFINITE_COST) + (connectionWeight(currentPos, neighborPos) ?: 1)
            if (stepCost >= distanceFromStart.getOrDefault(neighborPos, INFINITE_COST)) {
                continue
            }

            val estRemainingStepCost = stepCost + neighborPos.manhattanDistanceTo(end)

            // update distance and estimated cost
            distanceFromStart[neighborPos] = stepCost
            estRemainingCost[neighborPos] = estRemainingStepCost

            // Record the path
            cameFrom[neighborPos] = currentPos

            // Add to the open set if not already present
            if (neighborPos !in queue) {
                queue[neighborPos] = estRemainingStepCost
            }
        }
    }

    return null
}
