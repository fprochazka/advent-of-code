package aoc.utils.d2.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Position

data class GraphPathStepOriented(
    val pos: Position,
    val inDir: Direction,
    val pathCost: Long,
    val prev: GraphPathStepOriented? = null
) : Comparable<GraphPathStepOriented> {

    fun next(pos: Position, inDir: Direction, stepCost: Long): GraphPathStepOriented =
        GraphPathStepOriented(pos, inDir, this.pathCost + stepCost, this)

    fun toReverseSteps() = generateSequence(this) { it.prev }

    fun toList(): List<GraphPathStepOriented> {
        val result = ArrayList<GraphPathStepOriented>()
        for (step in toReverseSteps()) {
            result.add(step)
        }
        result.reverse()
        return result
    }

    override fun compareTo(other: GraphPathStepOriented): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

}
