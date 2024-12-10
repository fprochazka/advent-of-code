package aoc.y2024

import aoc.y2024.Day02.Report
import utils.Resource
import kotlin.math.absoluteValue

fun main() {
    solve(Resource.named("aoc2024/day02/example.txt"))
    solve(Resource.named("aoc2024/day02/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day02()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day02(): Day02 = Day02(
    nonBlankLines()
        .map { line -> line.split("\\s+".toRegex()) }
        .map { line -> Report(line.map { it.toInt() }) }
)

data class Day02(val reports: List<Report>) {

    val result1 by lazy {
        reports.count { it.isSafe }
    }

    val result2 by lazy {
        reports.count { it.isSafeWithDampener() }
    }

    data class Report(val levels: List<Int>) {

        val allDiffs = levels.zipWithNext { a, b -> (b - a) }
        val allIncreasing = allDiffs.all { it > 0 }
        val allDecreasing = allDiffs.all { it < 0 }

        /**
         * The levels are either all increasing or all decreasing.
         * Any two adjacent levels differ by at least one and at most three.
         */
        val isSafe by lazy {
            (allIncreasing || allDecreasing)
              && allDiffs.map { it.absoluteValue }.all { it in 1..3 }
        }

        fun copyWithout(index: Int): Report = Report(levels.filterIndexed { i, _ -> i != index })

        /**
         * The same rules apply as before, except if removing a single level from an unsafe report would make it safe, the report instead counts as safe.
         */
        fun isSafeWithDampener(): Boolean {
            if (isSafe) return true

            levels.indices.forEach { index ->
                val copy = copyWithout(index)
                if (copy.isSafe) return true
            }

            return false
        }

    }

}
