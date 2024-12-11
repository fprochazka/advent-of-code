package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Direction
import aoc.utils.d2.Matrix
import aoc.utils.d2.OrientedPosition
import aoc.utils.d2.Position
import kotlinx.coroutines.*

fun main() {
    solve(Resource.named("aoc2024/day06/example1.txt"))
    solve(Resource.named("aoc2024/day06/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day06()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day06(): Day06 = Day06.toMatrix(charMatrix()).let { (floorPlan, startingPoint) -> Day06(floorPlan, startingPoint) }

data class Day06(
    val floorPlan: Matrix<Char>,
    val startingPoint: OrientedPosition,
) {

    val patrolPrediction by lazy { floorPlan.predictPatrol(startingPoint) }

    val result1 by lazy { patrolPrediction.map { it.position }.toSet().size }
    val result2 by lazy { findObstaclePlacementToCreateLoops().size }

    fun Matrix<Char>.predictPatrol(patrolStart: OrientedPosition): List<OrientedPosition> {
        val patrolPath = mutableSetOf<OrientedPosition>()

        var current = patrolStart
        while (true) {
            val visited = current
            try {
                val next = current.step()

                current = when {
                    next.position !in this -> break
                    isObstacle(this[next.position]) -> current.turnRight()
                    else -> next
                }

            } finally {
                patrolPath.add(visited)
            }
        }

        return patrolPath.toList()
    }

    fun Matrix<Char>.isPatrolLooping(patrolStart: OrientedPosition): Boolean {
        val patrolPath = HashSet<Pair<Position, Direction>>()

        var (currentPosition, direction) = patrolStart
        while (true) {
            val nextPosition = currentPosition + direction
            when {
                nextPosition !in this -> {
                    return false
                }

                isObstacle(this[nextPosition]) -> {
                    if (!patrolPath.add(currentPosition to direction)) {
                        return true
                    }
                    direction = direction.turnRight()
                }

                else -> {
                    currentPosition = nextPosition
                }
            }
        }

        return false
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun findObstaclePlacementToCreateLoops(): Set<Position> = runBlocking {
        // we can't place an obstacle on were the guard is standing
        val originalPatrol = patrolPrediction.let { it.subList(1, it.size) }

        val parallelism = 8
        val workloads = originalPatrol.chunked(originalPatrol.size / parallelism)

        return@runBlocking withContext(Dispatchers.Default.limitedParallelism(parallelism)) {
            val chunkResults = workloads.map { workChunk ->
                async {
                    val taskSpecificFloorPlan = floorPlan.copy()

                    return@async workChunk
                        .map { (nextPosition, _) ->
                            val originalCellData = taskSpecificFloorPlan[nextPosition]!!
                            if (isObstacle(originalCellData)) {
                                return@map null // no point in replacing existing obstacles
                            }

                            try {
                                taskSpecificFloorPlan[nextPosition] = OBSTACLE

                                return@map if (taskSpecificFloorPlan.isPatrolLooping(startingPoint)) nextPosition else null

                            } finally {
                                taskSpecificFloorPlan[nextPosition] = originalCellData
                            }
                        }
                        .filterNotNull()
                        .toSet()
                }
            }

            // Await all results and add to the thread-safe set
            chunkResults
                .map { it.await() }
                .flatten()
                .toSet()
        }
    }

    fun OrientedPosition.turnRight(): OrientedPosition = OrientedPosition(position, direction.turnRight())

    fun Direction.turnRight(): Direction = when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }

    companion object {

        const val OBSTACLE = '#'
        const val GUARD_UP = '^'

        fun toMatrix(matrix: Map<Position, Char>): Pair<Matrix<Char>, OrientedPosition> {
            val floorPlan = Matrix.from(matrix)

            val startingPos = OrientedPosition(floorPlan.allPositionsOfValue(GUARD_UP).single(), Direction.UP)
            floorPlan[startingPos.position] = '.'

            return floorPlan to startingPos
        }

        fun isObstacle(char: Char?): Boolean = char == OBSTACLE

    }

}
