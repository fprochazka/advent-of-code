package aoc.y2024

import aoc.utils.Resource

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
                { componentIndex, beforeOperatorValue, op ->
                    op.eval(beforeOperatorValue, components[componentIndex]).let { newValue ->
                        when {
                            newValue > result -> State.ELIMINATE_BRANCH to newValue

                            components.size == (componentIndex + 1) -> when {
                                newValue == result -> State.DONE to newValue
                                else -> State.ELIMINATE_BRANCH to newValue
                            }

                            else -> State.CONTINUE to newValue
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
            for (op in allowedOperators) {
                operators.addLast(op)

                val (state, newValue) = compute.invoke(operators.size, value, op)
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

