package aoc.y2024

import aoc.AocTest
import org.junit.jupiter.api.Test

class Aoc2024Day14Test : AocTest() {

    @Test
    fun example1() {
        resource2024(14, "example1").let { input ->
            input.day14().let { problem ->
                println("input: $input")
                input.assertResult("task1") { problem.result1 }
            }
        }
    }

    @Test
    fun input() {
        resource2024(14, "input").let { input ->
            input.day14().let { problem ->
                println("input: $input")
                input.assertResult("task1") { problem.result1 }
            }
        }
    }

}
