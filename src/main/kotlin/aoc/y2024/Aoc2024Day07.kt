package aoc.y2024

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
                equation.findFirstSolution(allowedOperators)?.let { add(equation to it) }
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
                equation.findFirstSolution(allowedOperators)?.let { add(equation to it) }
            }
        }
    }

    val result2 by lazy {
        result1 + equationsSolvableWithAlsoConcatenation.sumOf { (equation, _) -> equation.result }
    }

    data class Equation(val result: Long, val components: List<Long>) {

        fun findFirstSolution(allowedOperators: List<Operator>) =
            dfsOperatorVariants(
                allowedOperators,
                components.first(),
                { componentIndex, leftValue, operator ->
                    operator.eval(leftValue, components[componentIndex]).let { operationResult ->
                        when {
                            operationResult > result -> State.ELIMINATE_BRANCH to operationResult

                            components.size == (componentIndex + 1) -> when {
                                operationResult == result -> State.DONE to operationResult
                                else -> State.ELIMINATE_BRANCH to operationResult
                            }

                            else -> State.CONTINUE to operationResult
                        }
                    }
                }
            )

        fun dfsOperatorVariants(
            allowedOperators: List<Operator>,
            value: Long,
            compute: (Int, Long, Operator) -> Pair<State, Long>,
            operators: ArrayDeque<Operator> = ArrayDeque<Operator>()
        ): List<Operator>? {
            for (operator in allowedOperators) {
                operators.addLast(operator)

                val (state, newValue) = compute.invoke(operators.size, value, operator)
                when (state) {
                    State.ELIMINATE_BRANCH -> {}
                    State.DONE -> return operators.toList()
                    State.CONTINUE -> dfsOperatorVariants(allowedOperators, newValue, compute, operators)?.let { return it }
                }

                operators.removeLast()
            }

            return null
        }

        enum class State {
            ELIMINATE_BRANCH,
            DONE,
            CONTINUE,
        }

    }

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

