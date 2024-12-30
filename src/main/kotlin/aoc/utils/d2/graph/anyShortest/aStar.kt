package aoc.utils.d2.graph.anyShortest

import aoc.utils.containers.PriorityQueueSet
import aoc.utils.d2.Position
import aoc.utils.d2.graph.MatrixGraph
import aoc.utils.d2.matrix.Matrix
import aoc.utils.d2.path.GraphPathParents

fun <V : Any> MatrixGraph<V>.anyShortestPathAStar(
    start: Position,
    end: Position,
): List<Position>? {
    val cameFrom = GraphPathParents(dims)

    val distanceFromStart = Matrix.empty<Long>(dims).also {
        it[start] = 0L
    }
    val estRemainingCost = Matrix.empty<Long>(dims).also {
        it[start] = start.manhattanDistanceTo(end)
    }
    val queue = PriorityQueueSet<Position>().also {
        it[start] = estRemainingCost.getValue(start)
    }

    while (queue.isNotEmpty()) {
        val currentPos = queue.removeFirst()
        if (currentPos == end) {
            return cameFrom.getPathOf(currentPos)
        }

        val currentDistance = distanceFromStart.getOrDefault(currentPos, MatrixGraph.INFINITE_COST)

        for (neighborPos in connectionsFrom(currentPos)) {
            val stepCost = currentDistance + (connectionWeight(currentPos, neighborPos) ?: 1)
            if (stepCost >= distanceFromStart.getOrDefault(neighborPos, MatrixGraph.INFINITE_COST)) {
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
