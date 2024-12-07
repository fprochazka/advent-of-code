package aoc2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day99/example1.txt"))
    solve(Resource.named("aoc2024/day99/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day99()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day99(): Day99 = Day99(
    content()
)

data class Day99(val data: String) {

    val result1: String by lazy { TODO() }
    val result2: String by lazy { TODO() }

}
