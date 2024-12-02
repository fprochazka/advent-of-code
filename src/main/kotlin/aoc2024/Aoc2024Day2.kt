package aoc2024

import kotlin.math.absoluteValue

fun main() {
    val inputStream = object {}.javaClass.getResourceAsStream("/aoc2024/day02/input.txt")!!
    val lines = inputStream.bufferedReader().use { it.readText() }
        .split("\n")
        .filter { it.isNotBlank() }

    val reports = lines.map { line -> line.split("\\s+".toRegex()) }
        .map { line -> Report(line.map { it.toInt() }) }

    var safeReports = 0
    reports.forEach { report ->
        println("${report}: safe=${report.isSafe}, increasing=${report.allIncreasing}, decreasing=${report.allDecreasing}, diffs=${report.allDiffs}")
        if (report.isSafe) safeReports++
    }

    println("total reports: ${reports.size}")

    println("safe reports: $safeReports")

    println("safe with dampener reports: ${reports.count { it.isSafeWithDampener()}}")
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
          && allDiffs.map { it.absoluteValue }.all { it >= 1 && it <= 3 }
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
