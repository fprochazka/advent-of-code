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

    fun toList(): List<GraphPathStepOriented> =
        generateSequence(this) { it.prev }.toList().reversed()

    override fun compareTo(other: GraphPathStepOriented): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

}
