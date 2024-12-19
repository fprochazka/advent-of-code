package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.*
import kotlinx.coroutines.*

fun Resource.day06(): Day06 =
    Day06.parse(matrix2d()).let { (floorPlan, startingPoint) -> Day06(floorPlan, startingPoint) }

data class Day06(
    val floorPlan: Matrix<Char>,
    val startingPoint: OrientedPosition,
) {

    val patrolPrediction by lazy { floorPlan.predictPatrol(startingPoint) }

    val result1 by lazy { patrolPrediction.map { it.position }.toSet().size }
    val result2 by lazy { findObstaclePlacementToCreateLoops().size }

    fun Matrix<Char>.predictPatrol(patrolStart: OrientedPosition): List<OrientedPosition> {
        val patrolPath = mutableListOf<OrientedPosition>()

        var current = patrolStart
        while (true) {
            val visited = current
            try {
                val next = current.step()

                current = when {
                    next.position !in this.dims -> break
                    isObstacle(this[next.position]) -> current.turnRight90()
                    else -> next
                }

            } finally {
                patrolPath.add(visited)
            }
        }

        return patrolPath.toList()
    }

    fun Matrix<Char>.isPatrolLooping(patrolStart: OrientedPosition): Boolean {
        val patrolPath = HashMap<Position, DirectionBitSet>(32)

        var (currentPosition, direction) = patrolStart
        while (true) {
            val nextPosition = currentPosition + direction
            when {
                nextPosition !in this.dims -> {
                    return false
                }

                isObstacle(this[nextPosition]) -> {
                    val visitedDirections = patrolPath.getOrPut(currentPosition) { DirectionBitSet() }
                    if (!visitedDirections.add(direction)) {
                        return true
                    }
                    direction = direction.turnRight90()
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

    companion object {

        const val OBSTACLE = '#'
        const val GUARD_UP = '^'

        fun parse(matrix: Resource.CharMatrix2d): Pair<Matrix<Char>, OrientedPosition> {
            val floorPlan = Matrix.ofChars(matrix)

            val startingPos = OrientedPosition(floorPlan.allPositionsOfValue(GUARD_UP).single(), Direction.UP)
            floorPlan[startingPos.position] = '.'

            return floorPlan to startingPos
        }

        fun isObstacle(char: Char?): Boolean = char == OBSTACLE

    }

}
