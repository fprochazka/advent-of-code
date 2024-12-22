package aoc.y2024

import aoc.utils.Resource
import aoc.utils.combinatorics.variationsWithoutRepetition
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.d2.PositionBitSet

fun Resource.day08(): Day08 = Day08(
    Matrix.ofChars(matrix2d())
)

data class Day08(
    val antennasMap: Matrix<Char>
) {

    val antennasByFrequency by lazy {
        antennasMap.allPositionsByValues { it != '.' }
    }

    val result1 by lazy {
        allAntennaAntinodes(::basicRadiusAntinodes).size
    }

    val result2 by lazy {
        allAntennaAntinodes(::gridAntinodes).size
    }

    fun basicRadiusAntinodes(posA: Position, posB: Position): Sequence<Position> =
        sequenceOf(posA + posA.distanceTo(posB))

    fun gridAntinodes(posA: Position, posB: Position): Sequence<Position> =
        posA.distanceTo(posB)
            .let { distance -> generateSequence(posA) { it + distance } }

    fun allAntennaAntinodes(antinodes: (Position, Position) -> Sequence<Position>): Set<Position> {
        val result = PositionBitSet(antennasMap.dims)

        for ((_, antennas) in antennasByFrequency) {
            for ((posA, posB) in antennas.variationsWithoutRepetition(2)) {
                antinodes(posA, posB)
                    .takeWhile { it in antennasMap.dims }
                    .forEach(result::add)
            }
        }

        return result
    }

}
