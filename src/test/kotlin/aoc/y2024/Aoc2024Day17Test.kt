package aoc.y2024

import aoc.AocTest
import aoc.utils.Resource
import org.junit.jupiter.api.Test

class Aoc2024Day17Test : AocTest() {

    @Test
    fun example1() {
        resource2024(17, "example1").let { input ->
            println("input: $input")
            input.assertResult("task1") { input.day17().result1 }
        }
    }

    @Test
    fun example2() {
        resource2024(17, "example2").let { input ->
            println("input: $input")
            input.assertResult("task1") { input.day17().result2 }
        }
    }

    @Test
    fun input() {
//        solve(resource2024(17, "input"))
    }

    private fun solve(input: Resource) {
        println("input: $input")

        val problem = input.day17()

        input.assertResult("task1") { problem.result1 }
//        input.assertResult("task2") { problem.result2 }
    }

}
