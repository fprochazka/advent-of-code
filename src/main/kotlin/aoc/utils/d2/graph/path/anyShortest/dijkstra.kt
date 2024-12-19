package aoc.utils.d2.graph.path.anyShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.path.GraphPathOrientedStep
import java.util.*

fun <V : Any> MatrixGraph<V>.anyShortestOrientedPathDijkstra(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (GraphPathOrientedStep, Direction) -> Long,
): GraphPathOrientedStep? {
    val queue = PriorityQueue<GraphPathOrientedStep>(compareBy { it.pathCost }).apply {
        add(GraphPathOrientedStep(start, startDir, 0))
    }
    val visited = HashSet<Position>()

    while (queue.isNotEmpty()) {
        val currentStep = queue.poll()!!
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        val neighbours = connectionsFrom(currentStep.pos)
            .filter { it != currentStep.prev?.pos } // no 180 flips
            .filter { it !in visited }
            .map { currentStep.pos.relativeDirectionTo(it)!! to it }

        for ((neighbourDir, neighbourPos) in neighbours) {
            val nextStep = currentStep.next(
                neighbourPos,
                neighbourDir,
                stepCost = edgeCost(currentStep, neighbourDir),
            )
            queue.add(nextStep)
        }
    }

    return null
}
