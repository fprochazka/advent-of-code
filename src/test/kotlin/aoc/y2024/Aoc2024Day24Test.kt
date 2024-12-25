package aoc.y2024

import aoc.AocTest
import org.junit.jupiter.api.Test

class Aoc2024Day24Test : AocTest() {

    @Test
    fun example1() {
        resource2024(24, "example1").let { input ->
            println("input: ${input}")

            val problem = input.day24()

            input.assertResult("task2") { problem.result2 }
        }
    }

    @Test
    fun example2() {
        resource2024(24, "example2").let { input ->
            println("input: ${input}")

            val problem = input.day24()

            input.assertResult("task1") { problem.result1 }
        }
    }

    @Test
    fun input() {
        resource2024(24, "input").let { input ->
            println("input: ${input}")

            val problem = input.day24()

            input.assertResult("task1") { problem.result1 }
            input.assertResult("task2") { problem.result2 }
        }
    }

}
