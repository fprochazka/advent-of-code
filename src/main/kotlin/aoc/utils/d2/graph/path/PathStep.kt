package aoc.utils.d2.graph.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Position

data class PathStep(
    val pos: Position,
    val inDir: Direction,
    val stepCost: Long,
    val pathCost: Long,
    val prev: PathStep? = null
) : Comparable<PathStep> {

    constructor(pos: Position, inDir: Direction, stepCost: Long) : this(pos, inDir, stepCost, stepCost, prev = null)

    constructor(pos: Position, inDir: Direction, stepCost: Long, prev: PathStep) : this(
        pos,
        inDir,
        pathCost = prev.pathCost + stepCost,
        stepCost = stepCost,
        prev = prev
    )

    fun toList(): List<PathStep> =
        generateSequence(this) { it.prev }.toList().reversed()

    override fun compareTo(other: PathStep): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

}
