package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import aoc.utils.d2.Dimensions
import org.junit.jupiter.api.Test

class Aoc2024Day18Test : AocTest() {

    @Test
    fun example1task1() {
        solve(resource2024(18, "example1"), Dimensions(7, 7), simulateInitiallyCorrupted = 12)
    }

    @Test
    fun input() {
        solve(resource2024(18, "input"), Dimensions(71, 71), simulateInitiallyCorrupted = 1024)
    }

    private fun solve(input: Resource, dims: Dimensions, simulateInitiallyCorrupted: Int) {
        println("input: ${input}")

        val problem = input.day18(dims, simulateInitiallyCorrupted)

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
