package aoc.utils.d2.graph.component

import aoc.utils.containers.popAny
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.PositionSet

fun <V : Any> MatrixGraph<V>.groupToConnectedComponents(): Sequence<GraphComponent<V>> = sequence {
    val remainingPositions = nodes.positions.toMutableSet()
    while (remainingPositions.isNotEmpty()) {
        val connectedComponent = PositionSet(dims)

        val neighboursQueue = ArrayDeque<Position>()
        neighboursQueue.add(remainingPositions.popAny())

        while (neighboursQueue.isNotEmpty()) {
            val position = neighboursQueue.removeFirst()

            remainingPositions.remove(position) // remove from candidates to find separate areas

            if (!connectedComponent.add(position)) {
                continue // already visited
            }

            nodes[position]!!.connections
                .filter { it !in connectedComponent }
                .forEach { neighboursQueue.addLast(it) }
        }

        yield(GraphComponent(this@groupToConnectedComponents, connectedComponent))
    }
}
