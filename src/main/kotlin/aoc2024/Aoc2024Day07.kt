package aoc2024

import utils.Combinatorics
import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day07/example1.txt"))
    solve(Resource.named("aoc2024/day07/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day07()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day07(): Day07 = Day07(
    nonBlankLines()
        .map {
            it.split(":", limit = 2)
                .let { equationParts -> equationParts[0].trim().toLong() to (equationParts[1].trim().split(" ").map { it.trim().toLong() }).toList() }
        }
        .map { (result, components) -> Day07.Equation(result, components) }
        .toList()
)

data class Day07(val equations: List<Equation>) {

    val solvableEquationsWithBasicOperators by lazy {
        buildList {
            val allowedOperators = listOf(Operator.PLUS, Operator.TIMES)

            for (equation in equations) {
                findOperatorsThatSolveEquation(equation, allowedOperators)?.let { add(equation to it) }
            }
        }
    }

    // the total calibration result, which is the sum of the test values from just the equations that could possibly be true.
    val result1 by lazy {
        solvableEquationsWithBasicOperators.sumOf { (equation, _) -> equation.result }
    }

    val equationsSolvableWithAlsoConcatenation by lazy {
        buildList {
            val solvedEquations = solvableEquationsWithBasicOperators.map { it.first }.toSet()
            val equationsUnsolvableByFirstMethod = equations.filter { it !in solvedEquations }

            val allowedOperators = listOf(Operator.PLUS, Operator.TIMES, Operator.CONCATENATE)

            for (equation in equationsUnsolvableByFirstMethod) {
                findOperatorsThatSolveEquation(equation, allowedOperators)?.let { add(equation to it) }
            }
        }
    }

    val result2 by lazy {
        result1 + equationsSolvableWithAlsoConcatenation.sumOf { (equation, _) -> equation.result }
    }

    data class Equation(val result: Long, val components: List<Long>) {

        fun solvableWithOperators(operators: List<Operator>): Boolean =
            result == evaluateWith(operators)

        fun evaluateWith(operators: List<Operator>): Long {
            var operatorsIndex = 0
            return components.reduce { acc, lng -> operators[operatorsIndex].eval(acc, lng).also { operatorsIndex += 1 } }
        }

    }

    fun findOperatorsThatSolveEquation(equation: Equation, operators: List<Operator>): List<Operator>? =
        Combinatorics.permutationsWithRepetition(operators, equation.components.size - 1)
            .firstOrNull { operators -> equation.solvableWithOperators(operators) }

    enum class Operator {
        PLUS,
        TIMES,
        CONCATENATE,
        ;

        fun eval(a: Long, b: Long): Long = when (this) {
            PLUS -> a + b
            TIMES -> a * b
            CONCATENATE -> "$a$b".toLong()
        }

    }

}

