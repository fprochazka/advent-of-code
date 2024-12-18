package aoc.utils.d2.graph.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Position

data class GraphPathOrientedStep(
    val pos: Position,
    val inDir: Direction,
    val pathCost: Long,
    val prev: GraphPathOrientedStep? = null
) : Comparable<GraphPathOrientedStep> {

    fun next(pos: Position, inDir: Direction, stepCost: Long): GraphPathOrientedStep =
        GraphPathOrientedStep(pos, inDir, this.pathCost + stepCost, this)

    fun toList(): List<GraphPathOrientedStep> =
        generateSequence(this) { it.prev }.toList().reversed()

    override fun compareTo(other: GraphPathOrientedStep): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

}
