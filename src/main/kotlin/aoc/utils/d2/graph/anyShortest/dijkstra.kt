package aoc.utils.d2.graph.anyShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.Position
import aoc.utils.d2.PositionBitSet
import aoc.utils.d2.graph.MatrixGraph
import aoc.utils.d2.path.GraphPathStepOriented
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue

fun <V : Any> MatrixGraph<V>.anyShortestOrientedPathDijkstra(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (GraphPathStepOriented, Direction) -> Long,
): GraphPathStepOriented? {
    val queue = ObjectHeapPriorityQueue<GraphPathStepOriented>(compareBy { it.pathCost }).apply {
        enqueue(GraphPathStepOriented(start, startDir, 0))
    }
    val visited = PositionBitSet(dims)

    while (queue.size() > 0) {
        val currentStep = queue.dequeue()!!
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        val neighbours = connectionsFrom(currentStep.pos).let { connections ->
            connections.mapNotNullTo(ArrayList(connections.size)) {
                if (it != currentStep.prev?.pos && it !in visited) {
                    currentStep.pos.relativeDirectionTo(it)!! to it
                } else {
                    null
                }
            }
        }

        for ((neighbourDir, neighbourPos) in neighbours) {
            val nextStep = currentStep.next(
                neighbourPos,
                neighbourDir,
                stepCost = edgeCost(currentStep, neighbourDir),
            )
            queue.enqueue(nextStep)
        }
    }

    return null
}
