package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day09Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(9, "example1"))
    }

    @Test
    fun input() {
        solve(resource2024(9, "input"))
    }

    @Test
    fun perf_test_1() {
        solve(resource2024(9, "perf_test_1"))
    }

    @Test
    fun perf_test_2() {
        solve(resource2024(9, "perf_test_2"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day09()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
