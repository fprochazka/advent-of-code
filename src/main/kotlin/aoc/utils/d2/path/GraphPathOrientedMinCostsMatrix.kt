package aoc.utils.d2.path

import aoc.utils.d2.AreaDimensions
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.MatrixGraph.Companion.INFINITE_COST

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
