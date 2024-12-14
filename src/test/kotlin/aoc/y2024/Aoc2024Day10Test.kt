package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day10Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(10, "example1"))
    }

    @Test
    fun input() {
        solve(resource2024(10, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day10()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
