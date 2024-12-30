package aoc.utils.d2.graph.component

import aoc.utils.containers.popAny
import aoc.utils.d2.Position
import aoc.utils.d2.PositionBitSet
import aoc.utils.d2.graph.MatrixGraph

fun <V : Any> MatrixGraph<V>.groupToConnectedComponents(): Sequence<GraphComponent<V>> {
    val remainingPositions = nodes.positions.toMutableSet()
    return generateSequence {
        while (remainingPositions.isNotEmpty()) {
            val connectedComponent = PositionBitSet(dims)

            val neighboursQueue = ArrayDeque<Position>()
            neighboursQueue.add(remainingPositions.popAny())

            while (neighboursQueue.isNotEmpty()) {
                val position = neighboursQueue.removeFirst()

                remainingPositions.remove(position) // remove from candidates to find separate areas

                if (!connectedComponent.add(position)) {
                    continue // already visited
                }

                nodes[position]!!.connections.forEach {
                    if (it !in connectedComponent) neighboursQueue.addLast(it)
                }
            }

            return@generateSequence GraphComponent(this@groupToConnectedComponents, connectedComponent)
        }

        return@generateSequence null
    }
}
