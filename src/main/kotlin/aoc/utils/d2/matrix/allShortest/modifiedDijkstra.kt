package aoc.utils.d2.matrix.allShortest

import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph.Companion.INFINITE_COST
import aoc.utils.d2.Position
import java.util.PriorityQueue

fun <V : Any> Matrix<V>.allShortestPathsDfs(
    start: Position,
    end: Position,
    edge: (Position, Position) -> Boolean,
): Sequence<List<Position>> = sequence {
    data class GraphPathStep(
        val pos: Position,
        val pathCost: Long,
        val prev: GraphPathStep? = null
    ) : Comparable<GraphPathStep> {

        fun next(pos: Position, stepCost: Long = 1): GraphPathStep =
            GraphPathStep(pos, this.pathCost + stepCost, this)

        fun toList(): List<GraphPathStep> =
            generateSequence(this) { it.prev }.toList().reversed()

        fun toPositions(): List<Position> =
            toList().map { it.pos }

        override fun compareTo(other: GraphPathStep): Int =
            this.pathCost.compareTo(other.pathCost)

        override fun toString(): String = "($pos, cost=$pathCost, prev=${prev?.pos})"

    }

    fun connectionsFrom(pos: Position): List<Position> = buildList(4) {
        for (connection in pos.neighboursCardinalIn(dims)) {
            if (!edge(pos, connection)) continue
            add(connection)
        }
    }

    val minCosts = Matrix.empty<Long>(dims)
    var shortestPathCost = INFINITE_COST

    val queue = PriorityQueue<GraphPathStep>(compareBy { it.pathCost }).apply {
        add(GraphPathStep(start, 0))
    }

    while (queue.isNotEmpty()) {
        val currentStep = queue.poll()

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
            yield(currentStep.toPositions())
            continue
        }

        val neighbours = connectionsFrom(currentStep.pos)
            .filter { it != currentStep.prev?.pos } // no 180 flips
        for (neighbourPos in neighbours) {
            queue.add(currentStep.next(neighbourPos))
        }
    }
}
