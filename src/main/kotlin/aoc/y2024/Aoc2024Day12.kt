package aoc.y2024

import aoc.utils.Resource
import aoc.utils.Resource.CharMatrix2d
import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.component.groupToConnectedComponents

fun Resource.day12(): Day12 = Day12.parse(matrix2d())

data class Day12(val fieldGraph: MatrixGraph<Char>) {

    val areasByCrop by lazy { fieldGraph.groupToConnectedComponents().toList() }

    val fencedAreas by lazy {
        buildList {
            for (area in areasByCrop) {
                val fenceBetweenPositions = area.nodes
                    .flatMap { node ->
                        node.vacantSidesIncludingOutOfMatrix()
                            .map { vacantSide -> node.position to vacantSide.direction }
                    }
                    .toSet()

                add(area to fenceBetweenPositions)
            }
        }
    }

    val result1 by lazy {
        fencedAreas.sumOf { (area, fenceBetweenPositions) ->
            (area.size * fenceBetweenPositions.size).toLong()
        }
    }

    val result2 by lazy {
        fencedAreas.sumOf { (area, fenceBetweenPositions) ->
            (area.size * uniqueFenceSides(fenceBetweenPositions)).toLong()
        }
    }

    fun uniqueFenceSides(fenceBetweenPositions: Set<Pair<Position, Direction>>): Int {
        var fences = fenceBetweenPositions.toMutableSet()

        var uniqueFenceSides = 0
        while (fences.isNotEmpty()) {
            for (fence in fences) {
                uniqueFenceSides++
                fences.remove(fence)

                val (nodePosition, fenceSide) = fence
                for (directionAlongTheFence in listOf(fenceSide.turnRight90(), fenceSide.turnLeft90())) {
                    var siblingNode = nodePosition + directionAlongTheFence
                    while (fences.remove(siblingNode to fenceSide)) {
                        siblingNode = siblingNode + directionAlongTheFence
                    }
                }

                break; // fresh iterator
            }
        }

        return uniqueFenceSides
    }

    companion object {

        fun parse(matrix: CharMatrix2d): Day12 =
            Day12(parseGraph(matrix))

        fun parseGraph(matrix: CharMatrix2d): MatrixGraph<Char> =
            MatrixGraph.of(matrix, Direction.entriesCardinal, { it }) { a, b -> a.value == b.value }

    }

}
