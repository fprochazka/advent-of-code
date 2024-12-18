package aoc.utils.d2.graph.path.allShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.MatrixGraph.Companion.INFINITE_COST
import aoc.utils.d2.Position
import aoc.utils.d2.graph.path.GraphPathStep
import java.util.*

fun <V : Any> MatrixGraph<V>.allShortestPathsModifiedDijkstra(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (GraphPathStep, Direction) -> Long = { cursor, inDir -> connectionWeight(cursor.pos, cursor.pos + inDir)!! },
): Sequence<GraphPathStep> = sequence {
    class GraphMinCostsMatrix {

        private val minCosts = Matrix.empty<MutableMap<Direction, GraphPathStep>>(nodes.dims)

        fun update(step: GraphPathStep) {
            minCosts[step.pos] = (minCosts[step.pos] ?: mutableMapOf()).also {
                it.merge(step.inDir, step, { prev, next -> if (next.pathCost < prev.pathCost) next else prev })
            }
        }

        fun getPath(step: GraphPathStep): GraphPathStep? =
            minCosts[step.pos]?.get(step.inDir)

        operator fun get(step: GraphPathStep): Long =
            getPath(step)?.pathCost ?: INFINITE_COST

    }

    val minCosts = GraphMinCostsMatrix()
    var shortestPathCost = INFINITE_COST

    val queue = PriorityQueue<GraphPathStep>(compareBy { it.pathCost }).apply {
        add(GraphPathStep(start, startDir, 0))
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

        val neighbours = connectionsOf(currentStep.pos)
            .filter { it != currentStep.prev?.pos } // no 180 flips
            .map { currentStep.pos.relativeDirectionTo(it)!! to it }

        for ((neighbourDir, neighbourPos) in neighbours) {
            val nextStep = GraphPathStep(
                neighbourPos,
                neighbourDir,
                stepCost = edgeCost(currentStep, neighbourDir),
                prev = currentStep
            )
            queue.add(nextStep)
        }
    }
}
