package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Dimensions
import aoc.utils.d2.Distance
import aoc.utils.d2.Matrix
import aoc.utils.d2.Position
import java.awt.Color
import java.nio.file.Files
import javax.imageio.ImageIO

fun main() {
    Resource.named("aoc2024/day14/example1.txt").let { input ->
        input.day14().let { problem ->
            println("input: $input")
            input.assertResult("task1") { problem.result1 }
        }
    }

    Resource.named("aoc2024/day14/input.txt").let { input ->
        input.day14().let { problem ->
            println("input: $input")
            input.assertResult("task1") { problem.result1 }
        }
    }

    Resource.named("aoc2024/day14/input.txt").day14().easterEgg()
}

fun Resource.day14(): Day14 = Day14(
    Day14.parseRoom(nonBlankLines())
)

data class Day14(val room: Room) {

    val result1 by lazy { safetyFactor(100) }
    val result2 by lazy { easterEgg() }

    fun safetyFactor(afterSeconds: Int): Long {
        val quadrants = room.quadrants()

        for (robot in room.robots.patrol(afterSeconds)) {
            quadrants.forEach { it.addRobotIfInRange(robot.pos) }
        }

        return quadrants.map { it.robots }.reduce { res, cur -> res * cur }
    }

    fun easterEgg(): Long {
        println()

        val dir = Files.createTempDirectory("aoc_trees_")
        println("Trees dir: ${dir.toAbsolutePath()}")

        var robots = room.robots
        for (seconds in 0..Int.MAX_VALUE) {
            val matrix = Matrix.of<Char>(room.dims) { ' ' }
            robots.forEach { robot -> matrix[robot.pos] = 'R' }

            var tree = "Seconds: $seconds\n"
            tree += matrix.toString() + "\n\n\n"

            val image = matrix.draw {
                when (it) {
                    ' ' -> Color.WHITE
                    'R' -> Color.BLACK
                    else -> error("Unexpected char: $it")
                }
            }

            dir.resolve("aoc_trees_${seconds.toString().padStart(10, '0')}.png")
                .let { file -> ImageIO.write(image, "png", file.toFile()) }

            if (seconds % 100 == 0) {
                println("Clock: ${seconds}s")
            }

            if (seconds == 10_000) {
                break
            }

            robots = robots.patrol(1).toList()
        }

        return 0L;
    }

    fun List<Robot>.patrol(seconds: Int): Sequence<Robot> = sequence {
        for (robot in this@patrol) {
            val traveledDistance = robot.velocityPerSecond * seconds
            val positionOutsideOfRoomMatrix = robot.pos + traveledDistance
            val positionInRoom = positionOutsideOfRoomMatrix.rem(room.dims)

            yield(Robot(positionInRoom, robot.velocityPerSecond))
        }
    }

    data class Room(
        val dims: Dimensions,
        val robots: List<Robot>,
    ) {

        val midX = (dims.w / 2).toInt()
        val midY = (dims.h / 2).toInt()
        val maxX = dims.maxX.toInt()
        val maxY = dims.maxY.toInt()

        // 0..4  6..10
        //
        // ..... 2..1.   0..2
        // ..... .....
        // 1.... .....
        //
        // ..... .....   4..6
        // ...12 .....
        // .1... 1....
        fun quadrants() = listOf(
            Quadrant(0 until midX, 0 until midY), // top, left
            Quadrant((midX + 1)..maxX, 0 until midY), // top, right
            Quadrant(0 until midX, (midY + 1)..maxY), // bottom, left
            Quadrant((midX + 1)..maxX, (midY + 1)..maxY), // bottom, right
        )

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
    )

    companion object {

        fun parseRoom(lines: List<String>): Room {
            val linesI = lines.iterator()

            val roomDims = parseRoom(linesI.next())

            val robots = linesI.asSequence()
                .map { parseRobot(it) }
                .toList()

            return Room(roomDims, robots)
        }

        fun parseRoom(input: String): Dimensions =
            "w=(\\d+),h=(\\d+)".toRegex()
                .matchEntire(input)
                ?.let { Dimensions(it.groupValues[1], it.groupValues[2]) }
                ?: error("Invalid room: $input")

        fun parseRobot(input: String): Robot =
            // p=77,66 v=-73,-2
            "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()
                .matchEntire(input)
                ?.let {
                    Robot(
                        Position(it.groupValues[1], it.groupValues[2]),
                        Distance(it.groupValues[3], it.groupValues[4]),
                    )
                }
                ?: error("Invalid room: $input")

    }

}
