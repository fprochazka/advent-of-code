package aoc.utils.d2

data class OrientedPosition(var position: Position, var direction: Direction) {

    fun turnRight90(): OrientedPosition =
        OrientedPosition(position, direction.turnRight90())

    fun turnLeft90(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft90())

    fun turnRight45(): OrientedPosition =
        OrientedPosition(position, direction.turnRight45())

    fun turnLeft45(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft45())

    fun step(count: Int = 1): OrientedPosition =
        OrientedPosition(
            position.stepInDirection(direction, count),
            direction
        )

    override fun toString(): String = "$position -> $direction"

}
