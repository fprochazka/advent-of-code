package aoc.utils.d2.graph.allShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.Position
import aoc.utils.d2.graph.MatrixGraph
import aoc.utils.d2.path.GraphPathOrientedMinCostsMatrix
import aoc.utils.d2.path.GraphPathStepOriented
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue

fun <V : Any> MatrixGraph<V>.allShortestOrientedPathsModifiedDijkstra(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (GraphPathStepOriented, Direction) -> Long,
): Sequence<GraphPathStepOriented> {
    val minCosts = GraphPathOrientedMinCostsMatrix(this@allShortestOrientedPathsModifiedDijkstra.dims)
    var shortestPathCost = MatrixGraph.INFINITE_COST

    val queue = ObjectHeapPriorityQueue<GraphPathStepOriented>(compareBy { it.pathCost }).apply {
        enqueue(GraphPathStepOriented(start, startDir, 0))
    }

    return generateSequence {
        while (queue.size() > 0) {
            val currentStep = queue.dequeue()

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
                return@generateSequence currentStep
            }

            val neighbours = connectionsFrom(currentStep.pos).let { connections ->
                connections.mapNotNullTo(ArrayList(connections.size)) {
                    if (it == currentStep.prev?.pos) {
                        null // no 180 flips
                    } else {
                        currentStep.pos.relativeDirectionTo(it)!! to it
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

        return@generateSequence null
    }
}
