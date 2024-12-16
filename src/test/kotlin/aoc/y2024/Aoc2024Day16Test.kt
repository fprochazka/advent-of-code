package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Aoc2024Day16Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(16, "example1"))
    }

    @Test
    fun example2() {
        solve(resource2024(16, "example2"))
    }

    @Test
    fun input() {
        solve(resource2024(16, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day16()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
