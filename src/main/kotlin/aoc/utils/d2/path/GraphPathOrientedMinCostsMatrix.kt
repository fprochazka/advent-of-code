package aoc.utils.d2.path

import aoc.utils.d2.AreaDimensions
import aoc.utils.d2.Direction
import aoc.utils.d2.graph.MatrixGraph.Companion.INFINITE_COST
import aoc.utils.d2.matrix.Matrix

class GraphPathOrientedMinCostsMatrix(dims: AreaDimensions) {

    private val minCosts = Matrix.empty<MutableMap<Direction, Long>>(dims)

    fun update(step: GraphPathStepOriented) {
        minCosts[step.pos] = (minCosts[step.pos] ?: mutableMapOf()).also {
            it.merge(step.inDir, step.pathCost, ::minOf)
        }
    }

    operator fun get(step: GraphPathStepOriented): Long =
        minCosts[step.pos]?.get(step.inDir) ?: INFINITE_COST

}
