package aoc2024

import utils.Combinatorics
import utils.Resource
import utils.d2.Matrix
import utils.d2.Position
import utils.d2.distanceTo
import utils.d2.plus

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
    Matrix(charMatrix())
)

data class Day08(
    val antennasMap: Matrix<Char>
) {

    val antennasByFrequency: Map<Char, Set<Position>> by lazy {
        antennasMap.cells.entries
            .groupBy { entry -> entry.value }
            .mapValues { entry -> entry.value.map { it.key }.toSet() }
    }

    val result1 by lazy {
        allAntennaAntinodes(::basicRadiusAntinodes).size
    }

    val result2 by lazy {
        allAntennaAntinodes(::gridAntinodes).size
    }

    fun basicRadiusAntinodes(posA: Position, posB: Position): Sequence<Position> {
        val distance = posA.distanceTo(posB)
        return generateSequence(posA) { it + distance }.drop(1).take(1)
    }

    fun gridAntinodes(posA: Position, posB: Position): Sequence<Position> {
        val distance = posA.distanceTo(posB)
        return generateSequence(posA) { it + distance }
    }

    fun allAntennaAntinodes(antinodes: (Position, Position) -> Sequence<Position>): Set<Position> {
        val result = mutableSetOf<Pair<Position, Char>>()

        for (frequency in antennasByFrequency.keys) {
            if (frequency == '.') continue
            for ((posA, posB) in allAntennaPairs(frequency)) {
                antinodes(posA, posB)
                    .takeWhile { it in antennasMap }
                    .forEach { result.add(it to frequency) }

                antinodes(posB, posA)
                    .takeWhile { it in antennasMap }
                    .forEach { result.add(it to frequency) }
            }
        }

        return result.map { it.first }.toSet()
    }

    fun allAntennaPairs(frequency: Char): Sequence<Pair<Position, Position>> =
        Combinatorics.combinationsWithoutRepetition(antennasByFrequency[frequency]!!.toList(), 2)
            .map { it[0] to it[1] }

}
