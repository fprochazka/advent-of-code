package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position

fun Resource.day10(): Day10 = Day10(
    Day10.TerrainMap(Matrix.ofInts(matrix2d()))
)

data class Day10(val data: TerrainMap) {

    val trails by lazy {
        data.findHikingTrails()
    }

    val result1 by lazy {
        trails.uniqueStartAndEnd.size
    }

    val result2 by lazy {
        trails.uniqueTrails.size
    }

    data class TerrainMap(val map: Matrix<Int>) {

        fun trailStartingPoints(): Set<Position> =
            map.allPositionsOfValue(TRAIL_START_ELEVATION)

        fun findHikingTrails(): Trails {
            val directions = listOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

            val uniqueStartAndEnd = mutableSetOf<Pair<Position, Position>>()
            val uniqueTrails = mutableSetOf<List<Position>>()

            fun Matrix<Int>.validSteps(previous: Position): List<Position> =
                directions
                    .map { previous + it }
                    .filter { it in map.dims }
                    .filter { (map[it]!! - map[previous]!!) == 1 } // ascending numbers

            fun Matrix<Int>.dfs(steps: List<Position>) {
                for (step in validSteps(steps.last())) {
                    if (map[step] == TRAIL_FINISH_ELEVATION) {
                        uniqueStartAndEnd.add(steps.first() to step)
                        uniqueTrails.add(steps + step)

                    } else {
                        map.dfs(steps + step)
                    }
                }
            }

            trailStartingPoints().forEach {
                map.dfs(listOf(it))
            }

            return Trails(uniqueStartAndEnd, uniqueTrails);
        }

        companion object {

            const val TRAIL_START_ELEVATION = 0
            const val TRAIL_FINISH_ELEVATION = 9

        }

    }

    data class Trails(
        val uniqueStartAndEnd: Set<Pair<Position, Position>>,
        val uniqueTrails: Set<List<Position>>,
    )

}
