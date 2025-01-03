package aoc.utils.d2.matrix.allShortest

import aoc.utils.d2.Position
import aoc.utils.d2.graph.MatrixGraph.Companion.INFINITE_COST
import aoc.utils.d2.matrix.Matrix
import aoc.utils.d2.path.GraphConnection
import aoc.utils.d2.path.GraphPathStep
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue

fun <V : Any> Matrix<V>.allShortestPathsModifiedDijkstra(
    start: Position,
    end: Position,
    edge: (Position, Position) -> GraphConnection,
): Sequence<GraphPathStep> {
    fun connectionsFrom(step: GraphPathStep): List<Pair<Position, Long>> = buildList(4) {
        for (connection in step.pos.neighboursCardinalIn(dims)) {
            if (connection == step.prev?.pos) continue  // no 180 flips
            when (val edgeResult = edge(step.pos, connection)) {
                is GraphConnection.None -> continue
                is GraphConnection.Edge -> add(connection to edgeResult.cost)
            }
        }
    }

    val minCosts = Matrix.empty<Long>(dims)
    var shortestPathCost = INFINITE_COST

    val queue = ObjectHeapPriorityQueue<GraphPathStep>(compareBy { it.pathCost }).apply {
        enqueue(GraphPathStep(start, 0))
    }

    return generateSequence {
        while (queue.size() > 0) {
            val currentStep = queue.dequeue()!!

            if (currentStep.pathCost > shortestPathCost) {
                // the queue is sorted, therefore once it starts returning "too long" results we know we can throw away the rest
                break
            }

            if (currentStep.pathCost > (minCosts[currentStep.pos] ?: INFINITE_COST)) {
                // we've seen this path for cheaper
                continue
            }

            minCosts[currentStep.pos] = currentStep.pathCost

            if (currentStep.pos == end) {
                shortestPathCost = minOf(shortestPathCost, currentStep.pathCost)
                return@generateSequence currentStep
            }

            for ((neighbourPos, stepCost) in connectionsFrom(currentStep)) {
                queue.enqueue(currentStep.next(neighbourPos, stepCost))
            }
        }

        return@generateSequence null
    }
}
