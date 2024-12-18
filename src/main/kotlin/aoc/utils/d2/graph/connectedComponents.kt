package aoc.utils.d2.graph

import aoc.utils.containers.popAny
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position

fun <V : Any> MatrixGraph<V>.groupToConnectedComponents(): List<GraphComponent<V>> {
    val result = mutableListOf<GraphComponent<V>>()

    val remainingPositions = nodes.positions.toMutableSet()
    while (remainingPositions.isNotEmpty()) {
        val connectedComponent = mutableSetOf<Position>()

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

        result.add(GraphComponent(this, connectedComponent))
    }

    return result
}

class GraphComponent<V : Any>(
    private val graph: MatrixGraph<V>,
    val positions: Set<Position>
) {

    val nodes: List<MatrixGraph<V>.Node>
        get() = positions.map { graph.nodes[it]!! }

    val size: Int
        get() = positions.size

}
