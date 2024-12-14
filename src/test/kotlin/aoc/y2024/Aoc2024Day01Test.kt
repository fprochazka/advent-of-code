package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day01Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(1, "example"))
    }

    @Test
    fun input() {
        solve(resource2024(1, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day01()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
