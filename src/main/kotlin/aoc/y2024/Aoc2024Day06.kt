package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.chunksCount
import aoc.utils.d2.Direction
import aoc.utils.d2.DirectionBitSet
import aoc.utils.d2.OrientedPosition
import aoc.utils.d2.Position
import aoc.utils.d2.matrix.Matrix
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Callable
import java.util.concurrent.Executors

fun Resource.day06(): Day06 = Day06.parse(matrix2d())

@OptIn(ExperimentalCoroutinesApi::class)
class Day06(
    val floorPlan: Matrix<Char>,
    val startingPoint: OrientedPosition,
) {

    val patrolPrediction by lazy { floorPlan.predictPatrol(startingPoint) }

    val result1 by lazy { patrolPrediction.mapTo(HashSet()) { it.position }.size }
    val result2 by lazy { findObstaclePlacementsCountToCreateLoops() }

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
        val patrolPath = PositionMap<DirectionBitSet>(dims)

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

    fun Matrix<Char>.isObstaclePlacementCauseLoop(obstaclePosition: Position): Boolean {
        val originalCellData = this[obstaclePosition]!!
        if (isObstacle(originalCellData)) {
            return false // no point in replacing existing obstacles
        }

        try {
            this[obstaclePosition] = OBSTACLE

            return if (this.isPatrolLooping(startingPoint)) true else false

        } finally {
            this[obstaclePosition] = originalCellData
        }
    }

    fun findObstaclePlacementsCountToCreateLoops(): Int {
        // we can't place an obstacle on were the guard is standing
        val originalPatrol = patrolPrediction.let { it.subList(1, it.size) }

        val parallelism = 8
        val workloads = originalPatrol.chunksCount(parallelism)

        val executor = Executors.newFixedThreadPool(parallelism, Thread.ofVirtual().factory())

        return workloads
            .map { workChunk ->
                executor.submit(Callable {
                    val workerSpecificFloorPlan = floorPlan.copy()

                    return@Callable workChunk
                        .mapNotNull { (nextPosition, _) ->
                            if (workerSpecificFloorPlan.isObstaclePlacementCauseLoop(nextPosition)) nextPosition else null
                        }
                })
            }
            .flatMapTo(HashSet()) { it.get() }
            .also { executor.shutdown() }
            .size
    }

    companion object {

        const val OBSTACLE = '#'
        const val GUARD_UP = '^'

        fun parse(matrix: Resource.CharMatrix2d): Day06 {
            val floorPlan = Matrix.ofChars(matrix)

            val startingPos = OrientedPosition(floorPlan.allPositionsOfValue(GUARD_UP).single(), Direction.UP)
            floorPlan[startingPos.position] = '.'

            return Day06(floorPlan, startingPos)
        }

        fun isObstacle(char: Char?): Boolean = char == OBSTACLE

    }

}
