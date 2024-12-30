package aoc.utils.d2.graph.anyShortest

import aoc.utils.d2.Position
import aoc.utils.d2.PositionBitSet
import aoc.utils.d2.graph.MatrixGraph
import aoc.utils.d2.path.GraphPathStep

fun <V : Any> MatrixGraph<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
): GraphPathStep? {
    val queue = ArrayDeque<GraphPathStep>().apply { add(GraphPathStep(start, 0)) }
    val visited = PositionBitSet(dims).apply { add(start) }

    while (queue.isNotEmpty()) {
        val currentStep = queue.removeFirst()
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        for (neighbour in connectionsFrom(currentStep.pos)) {
            if (neighbour in visited) continue
            visited.add(neighbour)
            queue.add(currentStep.next(neighbour))
        }
    }

    return null
}
