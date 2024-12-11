package aoc.y2024

import utils.Resource
import utils.combinatorics.variationsWithoutRepetition
import utils.d2.Matrix
import utils.d2.Position

fun main() {
    solve(Resource.named("aoc2024/day08/example1.txt"))
    solve(Resource.named("aoc2024/day08/example2.txt"))
    solve(Resource.named("aoc2024/day08/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day08()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day08(): Day08 = Day08(
    Matrix.from(charMatrix())
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
        val result = mutableSetOf<Position>()

        for ((_, antennas) in antennasByFrequency) {
            for ((posA, posB) in antennas.variationsWithoutRepetition(2)) {
                antinodes(posA, posB)
                    .takeWhile { it in antennasMap }
                    .forEach(result::add)
            }
        }

        return result
    }

}
