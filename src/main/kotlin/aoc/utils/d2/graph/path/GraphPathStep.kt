package aoc.utils.d2.graph.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Position

data class GraphPathStep(
    val pos: Position,
    val inDir: Direction,
    var stepCost: Long,
    var pathCost: Long,
    val prev: GraphPathStep? = null
) : Comparable<GraphPathStep> {

    constructor(pos: Position, inDir: Direction, stepCost: Long) : this(pos, inDir, stepCost, stepCost, prev = null)

    constructor(pos: Position, inDir: Direction, stepCost: Long, prev: GraphPathStep) : this(
        pos,
        inDir,
        pathCost = prev.pathCost + stepCost,
        stepCost = stepCost,
        prev = prev
    )

    fun toList(): List<GraphPathStep> =
        generateSequence(this) { it.prev }.toList().reversed()

    override fun compareTo(other: GraphPathStep): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

}
