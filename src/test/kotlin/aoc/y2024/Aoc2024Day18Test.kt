package aoc.y2024

import aoc.AocTest
import aoc.utils.d2.Dimensions
import org.junit.jupiter.api.Test

class Aoc2024Day18Test : AocTest() {

    @Test
    fun example1task1() {
        resource2024(18, "example1").let { input ->
            println("input: ${input}")

            val problem = input.day18(Dimensions(7, 7), simulateInitiallyCorrupted = 12)

            input.assertResult("task1") { problem.result1 }
            input.assertResult("task2") { problem.result2 }
        }
    }

    @Test
    fun input() {
        resource2024(18, "input").let { input ->
            println("input: ${input}")

            val problem = input.day18()

            input.assertResult("task1") { problem.result1 }
            input.assertResult("task2") { problem.result2 }
        }
    }

}
