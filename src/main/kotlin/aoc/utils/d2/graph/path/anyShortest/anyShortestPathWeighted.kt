package aoc.utils.d2.graph.path.anyShortest

import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.path.PathStep
import java.util.*

fun <V : Any> MatrixGraph<V>.anyShortestPathWeighted(
    start: Position,
    startDir: Direction,
    end: Position,
    edgeCost: (PathStep, Direction) -> Long = { cursor, inDir -> connectionWeight(cursor.pos, cursor.pos + inDir)!! },
): PathStep? {
    val queue = PriorityQueue<PathStep>(compareBy { it.pathCost }).apply {
        add(PathStep(start, startDir, 0))
    }

    val visited = mutableSetOf<Position>()

    while (queue.isNotEmpty()) {
        val currentStep = queue.poll()!!
        visited.add(currentStep.pos)

        if (currentStep.pos == end) {
            return currentStep
        }

        val neighbours = connectionsOf(currentStep.pos)
            .filter { it != currentStep.prev?.pos } // no 180 flips
            .filter { it !in visited }
            .map { currentStep.pos.relativeDirectionTo(it)!! to it }

        for ((neighbourDir, neighbourPos) in neighbours) {
            val nextStep = PathStep(
                neighbourPos,
                neighbourDir,
                stepCost = edgeCost(currentStep, neighbourDir),
                prev = currentStep
            )
            queue.add(nextStep)
        }
    }

    return null
}
