package aoc.utils.d2.graph.component

import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position

class GraphComponent<V : Any>(
    private val graph: MatrixGraph<V>,
    val positions: Set<Position>
) {

    val nodes: List<MatrixGraph<V>.Node>
        get() = positions.map { graph.nodes[it]!! }

    val size: Int
        get() = positions.size

}
