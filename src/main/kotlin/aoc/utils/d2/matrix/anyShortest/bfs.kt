package aoc.utils.d2.matrix.anyShortest

import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.PositionSet
import aoc.utils.d2.path.GraphPathStep

fun <V : Any> Matrix<V>.anyShortestPathBfs(
    start: Position,
    end: Position,
    edge: (Position, Position) -> Boolean,
): GraphPathStep? {
    val queue = ArrayDeque<GraphPathStep>().apply { add(GraphPathStep(start, 0)) }
    val visited = PositionSet(dims).apply { add(start) }

    fun connectionsFrom(step: GraphPathStep): List<Position> = buildList(4) {
        for (connection in step.pos.neighboursCardinalIn(dims)) {
            if (connection in visited) continue
            if (!edge(step.pos, connection)) continue
            add(connection)
        }
    }

    while (queue.isNotEmpty()) {
        val currentStep = queue.removeFirst()
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        val neighbours = connectionsFrom(currentStep)
        for (neighbour in neighbours) {
            visited.add(neighbour)
            queue.add(currentStep.next(neighbour))
        }
    }

    return null
}
