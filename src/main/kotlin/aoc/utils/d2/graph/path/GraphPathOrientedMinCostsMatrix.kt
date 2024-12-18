package aoc.utils.d2.graph.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.MatrixGraph.Companion.INFINITE_COST

class GraphPathOrientedMinCostsMatrix(graph: MatrixGraph<*>) {

    private val minCosts = Matrix.empty<MutableMap<Direction, Long>>(graph.dims)

    fun update(step: GraphPathOrientedStep) {
        minCosts[step.pos] = (minCosts[step.pos] ?: mutableMapOf()).also {
            it.merge(step.inDir, step.pathCost, ::minOf)
        }
    }

    operator fun get(step: GraphPathOrientedStep): Long =
        minCosts[step.pos]?.get(step.inDir) ?: INFINITE_COST

}
