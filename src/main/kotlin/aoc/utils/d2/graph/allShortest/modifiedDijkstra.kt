package aoc.utils.d2.graph.allShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.Position
import aoc.utils.d2.graph.MatrixGraph
import aoc.utils.d2.path.GraphPathOrientedMinCostsMatrix
import aoc.utils.d2.path.GraphPathStepOriented
import java.util.*

fun <V : Any> MatrixGraph<V>.allShortestOrientedPathsModifiedDijkstra(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (GraphPathStepOriented, Direction) -> Long,
): Sequence<GraphPathStepOriented> = sequence {
    val minCosts = GraphPathOrientedMinCostsMatrix(this@allShortestOrientedPathsModifiedDijkstra.dims)
    var shortestPathCost = MatrixGraph.INFINITE_COST

    val queue = PriorityQueue<GraphPathStepOriented>(compareBy { it.pathCost }).apply {
        add(GraphPathStepOriented(start, startDir, 0))
    }

    while (queue.isNotEmpty()) {
        val currentStep = queue.poll()

        if (currentStep.pathCost > shortestPathCost) {
            // the queue is sorted, therefore once it starts returning "too long" results we know we can throw away the rest
            break
        }

        if (currentStep.pathCost > minCosts[currentStep]) {
            // we've seen this path for cheaper
            continue
        }

        minCosts.update(currentStep)

        if (currentStep.pos == end) {
            shortestPathCost = minOf(shortestPathCost, currentStep.pathCost)
            yield(currentStep)
            continue
        }

        val neighbours = connectionsFrom(currentStep.pos)
            .filter { it != currentStep.prev?.pos } // no 180 flips
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
}
