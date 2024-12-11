package aoc.y2024

import utils.Resource
import utils.d2.*

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

    val result1 by lazy { patrolPrediction.first.map { it.position }.toSet().size }
    val result2 by lazy { findObstaclePlacementToCreateLoops().size }

    fun Matrix<Char>.predictPatrol(patrolStart: OrientedPosition): Pair<List<OrientedPosition>, PatrolEnd> {
        val patrolPath = LinkedHashSet<OrientedPosition>()
        var patrolEnd = PatrolEnd.OUT_OF_LAB

        var current = patrolStart
        while (true) {
            val visited = current
            try {
                val next = current.step()

                when {
                    visited in patrolPath -> {
                        patrolEnd = PatrolEnd.LOOP_DETECTED
                        break
                    }

                    next.position !in this -> {
                        patrolEnd = PatrolEnd.OUT_OF_LAB
                        break
                    }

                    isObstacle(this[next.position]) -> {
                        current = current.turnRight()
                    }

                    else -> {
                        current = next
                    }
                }

            } finally {
                patrolPath.add(visited)
            }
        }

        return patrolPath.toList() to patrolEnd
    }

    fun findObstaclePlacementToCreateLoops(): Set<Position> {
        val result = mutableSetOf<Position>()

        // we can't place an obstacle on were the guard is standing
        val originalPatrol = patrolPrediction.first.let { it.subList(1, it.size) }

        for ((nextPosition, _) in originalPatrol) {
            val originalCellData = floorPlan[nextPosition]!!
            if (isObstacle(originalCellData)) {
                continue // no point in replacing existing obstacles
            }

            try {
                floorPlan[nextPosition] = EXTRA_OBSTACLE

                val (_, simulatedPatrolEnd) = floorPlan.predictPatrol(startingPoint)
                if (simulatedPatrolEnd == PatrolEnd.LOOP_DETECTED) {
                    result += nextPosition
                }

            } finally {
                floorPlan[nextPosition] = originalCellData
            }
        }

        return result
    }

    fun OrientedPosition.turnRight(): OrientedPosition = OrientedPosition(position, direction.turnRight())

    fun Direction.turnRight(): Direction = when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }

    enum class PatrolEnd {
        OUT_OF_LAB,
        LOOP_DETECTED,
    }

    companion object {

        const val OBSTACLE = '#'
        const val EXTRA_OBSTACLE = 'O'
        const val GUARD_UP = '^'

        fun toMatrix(matrix: Map<Position, Char>): Pair<Matrix<Char>, OrientedPosition> {
            val floorPlan = Matrix.from(matrix)
            val startingPos = OrientedPosition(floorPlan.allPositionsOfValue(GUARD_UP).first(), Direction.UP)
            floorPlan[startingPos.position] = '.'
            return floorPlan to startingPos
        }

        fun isObstacle(char: Char?): Boolean = char == OBSTACLE || char == EXTRA_OBSTACLE

    }

}
