package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.headTail
import aoc.utils.containers.toLongArray
import aoc.utils.strings.toLongs
import kotlin.math.pow

fun Resource.day07(): Day07 = Day07.parse(nonBlankLines())

class Day07(val equations: List<Equation>) {

    val solvableEquationsWithBasicOperators: List<Pair<Equation, List<Operator>>> by lazy {
        buildList(equations.size) {
            val allowedOperators = listOf(Operator.PLUS, Operator.TIMES)

            for (equation in equations) {
                equation.findFirstSolution(allowedOperators)?.let { add(equation to it) }
            }
        }
    }

    // the total calibration result, which is the sum of the test values from just the equations that could possibly be true.
    val result1: Long by lazy {
        solvableEquationsWithBasicOperators.sumOf { (equation, _) -> equation.result }
    }

    val equationsSolvableWithAlsoConcatenation: List<Pair<Equation, List<Operator>>> by lazy {
        buildList(equations.size) {
            val solvedEquations = solvableEquationsWithBasicOperators.map { it.first }.toSet()
            val equationsUnsolvableByFirstMethod = equations.filter { it !in solvedEquations }

            val allowedOperators = listOf(Operator.PLUS, Operator.TIMES, Operator.CONCATENATE)

            for (equation in equationsUnsolvableByFirstMethod) {
                equation.findFirstSolution(allowedOperators)?.let { add(equation to it) }
            }
        }
    }

    val result2: Long by lazy {
        result1 + equationsSolvableWithAlsoConcatenation.sumOf { (equation, _) -> equation.result }
    }

    class Equation(val result: Long, val components: LongArray) {

        fun findFirstSolution(allowedOperators: List<Operator>) =
            dfsOperatorVariants(
                allowedOperators,
                components.first(),
                { componentIndex, beforeOperatorValue, op ->
                    op.eval(beforeOperatorValue, components[componentIndex]).let { newValue ->
                        when {
                            newValue > result -> State.ELIMINATE_BRANCH

                            components.size == (componentIndex + 1) -> when {
                                newValue == result -> State.DONE
                                else -> State.ELIMINATE_BRANCH
                            }

                            else -> State.Continue(newValue)
                        }
                    }
                }
            )

        fun dfsOperatorVariants(
            allowedOperators: List<Operator>,
            value: Long,
            compute: (Int, Long, Operator) -> State,
            operators: ArrayDeque<Operator> = ArrayDeque<Operator>()
        ): List<Operator>? {
            for (op in allowedOperators) {
                operators.addLast(op)

                val state = compute.invoke(operators.size, value, op)
                when (state) {
                    is State.EliminateBranch -> {}
                    is State.Done -> return operators.toList()
                    is State.Continue -> dfsOperatorVariants(allowedOperators, state.newValue, compute, operators)?.let { return it }
                }

                operators.removeLast()
            }

            return null
        }

        sealed interface State {

            class EliminateBranch : State
            class Done : State
            data class Continue(val newValue: Long) : State

            companion object {
                val ELIMINATE_BRANCH = EliminateBranch()
                val DONE = Done()
            }

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
            CONCATENATE -> a * 10.0.pow(b.digitCount()).toLong() + b
        }

    }

    companion object {

        fun Long.digitCount(): Int = if (this == 0L) 1 else Math.log10(this.toDouble()).toInt() + 1

        fun parse(lines: List<String>): Day07 {
            val equations = lines.map {
                it.toLongs().headTail().let { (result, components) ->
                    Equation(result, components.toLongArray())
                }
            }

            return Day07(equations)
        }

    }

}
