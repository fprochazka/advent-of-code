package aoc2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day05/example1.txt"))
    solve(Resource.named("aoc2024/day05/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day06()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day06(): Day06 = Day06(
    content()
)

data class Day06(val data: String) {

    val result1: String by lazy { TODO() }
    val result2: String by lazy { TODO() }

}
