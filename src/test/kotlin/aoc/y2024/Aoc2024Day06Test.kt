package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day06Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(6, "example1"))
    }

    @Test
    fun input() {
        solve(resource2024(6, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day06()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
