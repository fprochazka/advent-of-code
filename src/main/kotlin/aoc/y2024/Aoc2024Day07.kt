package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.chunksCount
import aoc.utils.containers.headTail
import aoc.utils.containers.toLongArray
import aoc.utils.math.digitCount
import aoc.utils.math.scale10
import aoc.utils.strings.toLongs
import java.util.concurrent.Callable
import java.util.concurrent.Executors

fun Resource.day07(): Day07 = Day07.parse(nonBlankLines())

class Day07(val inputEquations: List<Equation>) {

    val solvableEquationsWithBasicOperators by lazy(LazyThreadSafetyMode.PUBLICATION) { solvableEquationsWithBasicOperators() }


    // the total calibration result, which is the sum of the test values from just the equations that could possibly be true.
    val result1: Long by lazy {
        solvableEquationsWithBasicOperators.sumOf { it.result }
    }

    val equationsSolvableWithAlsoConcatenation by lazy(LazyThreadSafetyMode.PUBLICATION) { equationsSolvableWithAlsoConcatenation() }

    val result2: Long by lazy {
        result1 + equationsSolvableWithAlsoConcatenation.sumOf { it.result }
    }

    fun solvableEquationsWithBasicOperators(): List<Equation> =
        findSolutions(listOf(Operator.PLUS, Operator.TIMES), inputEquations)

    fun equationsSolvableWithAlsoConcatenation(): List<Equation> {
        val solvedEquations = solvableEquationsWithBasicOperators.toSet()
        val equationsUnsolvableByFirstMethod = inputEquations.filter { it !in solvedEquations }

        return findSolutions(listOf(Operator.PLUS, Operator.TIMES, Operator.CONCATENATE), equationsUnsolvableByFirstMethod)
    }

    private fun findSolutions(allowedOperators: List<Operator>, equations: List<Equation>): List<Equation> {
        val parallelism = 8
        val executor = Executors.newFixedThreadPool(parallelism, Thread.ofVirtual().factory())

        return equations.chunksCount(parallelism)
            .map { workChunk ->
                executor.submit(Callable {
                    val chunkResult = ArrayList<Equation>()
                    for (equation in workChunk) {
                        equation.findFirstSolution(allowedOperators)?.let {
                            chunkResult.add(equation)
                        }
                    }
                    return@Callable chunkResult
                })
            }
            .flatMapTo(ArrayList<Equation>(equations.size)) { it.get() }
            .also { executor.shutdown() }
    }

    data class Equation(val result: Long, val components: LongArray) {

        fun findFirstSolution(allowedOperators: List<Operator>): List<Operator>? =
            dfsOperatorVariants(allowedOperators, components.first())

        fun dfsOperatorVariants(
            allowedOperators: List<Operator>,
            value: Long,
            operators: ArrayDeque<Operator> = ArrayDeque<Operator>()
        ): List<Operator>? {
            for (op in allowedOperators) {
                operators.addLast(op)

                val componentIndex = operators.size
                val newValue = op.eval(value, components[componentIndex])
                when {
                    newValue > result -> {}

                    components.size == (componentIndex + 1) -> when {
                        newValue == result -> return operators.toList()
                        else -> {}
                    }

                    else -> dfsOperatorVariants(allowedOperators, newValue, operators)?.let { return it }
                }

                operators.removeLast()
            }

            return null
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Equation) return false
            return result == other.result && components.contentEquals(other.components)
        }

        override fun hashCode(): Int =
            31 * (result.hashCode()) + components.contentHashCode()

    }

    enum class Operator {
        PLUS,
        TIMES,
        CONCATENATE,
        ;

        fun eval(a: Long, b: Long): Long = when (this) {
            PLUS -> a + b
            TIMES -> a * b
            CONCATENATE -> a.scale10(b.digitCount()) + b
        }

    }

    companion object {

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
