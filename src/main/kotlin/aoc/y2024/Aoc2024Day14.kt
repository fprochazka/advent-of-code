package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.d2.Dimensions
import aoc.utils.d2.Distance
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.strings.matchEntire
import aoc.y2024.Day14.Room.Robot
import java.awt.Color
import java.nio.file.Files
import kotlin.math.sign

fun main() {
    Resource.named("aoc2024/day14/input.txt").day14().easterEgg()
}

fun Resource.day14(): Day14 = Day14(
    Day14.parseRoom(nonBlankLines())
)

data class Day14(val room: Room) {

    val result1 by lazy { safetyFactor(100) }

    fun safetyFactor(afterSeconds: Int): Int {
        // 0..4  6..10
        //
        // ..... 2..1.   0..2
        // ..... .....
        // 1.... .....
        //
        // ..... .....   4..6
        // ...12 .....
        // .1... 1....

        // when (11 / 2) => 5, then 5 - 6 => sign -1
        // when (11 / 2) => 5, then 5 - 4 => sign +1
        // when (11 / 2) => 5, then 5 - 5 => sign  0 ... middle of the matrix

        fun Dimensions.quadrant(pos: Position): Pair<Int, Int> = ((w / 2) - pos.x).sign to ((h / 2) - pos.y).sign
        fun Robot.quadrant(): Pair<Int, Int>? = room.dims.quadrant(pos).takeIf { (a, b) -> a != 0 && b != 0 }

        val robotsInQuadrants = room.tick(afterSeconds).robots
            .mapNotNull { it.quadrant() }
            .groupingBy { it }
            .eachCount()

        return robotsInQuadrants.values.reduce { r, v -> r * v }
    }

    fun easterEgg() {
        val dir = Files.createTempDirectory("aoc_trees_")

        println()
        println("Trees dir: ${dir.toAbsolutePath()}")

        var state = room
        for (seconds in 0..Int.MAX_VALUE) {
            state.toMatrix().draw(dir.resolve("${seconds.toString().padStart(10, '0')}.png")) {
                when (it) {
                    false -> Color.WHITE
                    true -> Color.BLACK
                }
            }

            if (seconds % 100 == 0) println("Clock: ${seconds}s")
            if (seconds == 10_000) break

            state = state.tick(1)
        }
    }

    data class Room(
        val dims: Dimensions,
        val robots: List<Robot>,
    ) {

        fun tick(seconds: Int): Room =
            copy(robots = robots.map { robot -> robot.travel(this, seconds) })

        fun toMatrix(): Matrix<Boolean> =
            Matrix.of<Boolean>(dims) { false }.apply {
                robots.forEach { robot -> this[robot.pos] = true }
            }

        data class Robot(
            val pos: Position,
            val velocityPerSecond: Distance, // can be negative!
        ) {

            fun travel(room: Room, seconds: Int): Robot =
                copy(pos = (pos + (velocityPerSecond * seconds)) % room.dims)

        }

    }

    companion object {

        fun parseRoom(lines: List<String>): Room =
            lines.headTail().let { (head, tail) ->
                Room(
                    parseRoom(head),
                    tail.map { parseRobot(it) }.toList()
                )
            }

        fun parseRoom(input: String): Dimensions =
            input.trim().matchEntire(roomPattern) { Dimensions(it.groupValues[1], it.groupValues[2]) }

        // p=77,66 v=-73,-2
        fun parseRobot(input: String): Robot =
            input.trim().matchEntire(robotPattern) {
                Robot(
                    Position(it.groupValues[1], it.groupValues[2]),
                    Distance(it.groupValues[3], it.groupValues[4]),
                )
            }

        private val roomPattern = "w=(\\d+),h=(\\d+)".toRegex()
        private val robotPattern = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

    }

}
