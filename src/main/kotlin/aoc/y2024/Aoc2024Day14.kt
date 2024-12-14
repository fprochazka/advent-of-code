package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.d2.Dimensions
import aoc.utils.d2.Distance
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import aoc.utils.strings.matchEntire
import aoc.y2024.Day14.Room.Quadrant
import aoc.y2024.Day14.Room.Robot
import java.awt.Color
import java.nio.file.Files

fun main() {
    Resource.named("aoc2024/day14/input.txt").day14().easterEgg()
}

fun Resource.day14(): Day14 = Day14(
    Day14.parseRoom(nonBlankLines())
)

data class Day14(val room: Room) {

    val result1 by lazy { safetyFactor(100) }

    fun safetyFactor(afterSeconds: Int): Long {
        val quadrants = room.dims.quadrants()

        for (robot in room.tick(afterSeconds).robots) {
            quadrants.forEach { it.addRobotIfInRange(robot.pos) }
        }

        return quadrants.map { it.robots }.reduce { r, v -> r * v }
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

        data class Quadrant(
            val x: IntRange,
            val y: IntRange,
            var robots: Long = 0,
        ) {

            fun addRobotIfInRange(position: Position) {
                if (position.x in x && position.y in y) {
                    robots++
                }
            }

        }

        data class Robot(
            val pos: Position,
            val velocityPerSecond: Distance, // can be negative!
        ) {

            fun travel(room: Room, seconds: Int): Robot =
                copy(pos = (pos + (velocityPerSecond * seconds)) % room.dims)

        }

    }

    // 0..4  6..10
    //
    // ..... 2..1.   0..2
    // ..... .....
    // 1.... .....
    //
    // ..... .....   4..6
    // ...12 .....
    // .1... 1....
    fun Dimensions.quadrants(): List<Quadrant> = buildList {
        val midX = (w / 2).toInt()
        val midY = (h / 2).toInt()
        val maxX = maxX.toInt()
        val maxY = maxY.toInt()

        add(Quadrant(0 until midX, 0 until midY)) // top, left
        add(Quadrant((midX + 1)..maxX, 0 until midY)) // top, right
        add(Quadrant(0 until midX, (midY + 1)..maxY)) // bottom, left
        add(Quadrant((midX + 1)..maxX, (midY + 1)..maxY)) // bottom, right
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
