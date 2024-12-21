package aoc.utils.d2.graph.path

import aoc.utils.d2.Direction
import aoc.utils.d2.Position

data class GraphPathStep(
    val pos: Position,
    val pathCost: Long,
    val prev: GraphPathStep? = null
) : Comparable<GraphPathStep> {

    fun next(pos: Position, stepCost: Long = 1): GraphPathStep =
        GraphPathStep(pos, this.pathCost + stepCost, this)

    fun toList(): List<GraphPathStep> =
        generateSequence(this) { it.prev }.toList().reversed()

    fun toPositions(): List<Position> =
        toList().map { it.pos }

    fun toDirections(): List<Direction> =
        toPositions().zipWithNext().map { (a, b) -> a.relativeDirectionTo(b) ?: error("Cannot determine direction from $a to $b") }

    override fun compareTo(other: GraphPathStep): Int =
        this.pathCost.compareTo(other.pathCost)

    override fun toString(): String = "($pos, cost=$pathCost, prev=${prev?.pos})"

}
