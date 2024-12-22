package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Aoc2024Day22Test : AocTest() {

    @Test
    fun example1() {
        solve(resource2024(22, "example1"))
    }

    @Test
    fun example2() {
        solve(resource2024(22, "example2"))
    }

    @Test
    fun example3() {
        solve(resource2024(22, "example3"))
    }

    @Test
    fun input() {
        solve(resource2024(22, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day22()

        input.assertResult("task1") { problem.result1 }
        input.assertResult("task2") { problem.result2 }
    }

}
