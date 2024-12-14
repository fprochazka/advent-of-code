package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day08Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(8, "example1"))
    }

    @Test
    fun example2() {
        solve(resource2024(8, "example2"))
    }

    @Test
    fun input() {
        solve(resource2024(8, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day08()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
