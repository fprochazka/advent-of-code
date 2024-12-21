package aoc.utils.d2.matrix.anyShortest

import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.path.GraphConnection
import aoc.utils.d2.path.GraphPathStep

fun <V : Any> Matrix<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
    edge: (Position, Position) -> GraphConnection,
): GraphPathStep? {
    val queue = ArrayDeque<GraphPathStep>().apply { add(GraphPathStep(start, 0)) }
    val visited = HashSet<Position>().apply { add(start) }

    fun connectionsFrom(step: GraphPathStep): List<Position> = buildList(4) {
        for (connection in step.pos.neighboursCardinalIn(dims)) {
            if (connection in visited) continue
            when (val edgeResult = edge(step.pos, connection)) {
                is GraphConnection.None -> continue
                is GraphConnection.Edge -> {
                    require(edgeResult.cost == 1L) { "Cost != 1 is not supported, use different algo" }
                    add(connection)
                }
            }
        }
    }

    while (queue.isNotEmpty()) {
        val currentStep = queue.removeFirst()
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        for (neighbourPos in connectionsFrom(currentStep)) {
            visited.add(neighbourPos)
            queue.add(currentStep.next(neighbourPos))
        }
    }

    return null
}
