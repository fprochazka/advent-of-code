package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Distance
import aoc.utils.math.gcd
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import com.google.ortools.sat.LinearExpr
import java.util.*

fun main() {
    solve(Resource.named("aoc2024/day13/example1.txt"))
    solve(Resource.named("aoc2024/day13/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day13()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day13(): Day13 = Day13(
    nonBlankLines()
        .chunked(3)
        .map { Day13.parseSlotMachine(it) }
)

data class Day13(val arcades: List<SlotMachine>) {

    init {
        com.google.ortools.Loader.loadNativeLibraries()
    }

    val result1 by lazy {
        arcades
//            .sumOf<SlotMachine> { howManyMinTokensAreNeededToReachPrizeNaive(it) }
            .sumOf<SlotMachine> { howManyMinTokensAreNeededToReachPrizeFast(it) }
    }

    val result2 by lazy {
        arcades
            .map { it.copy(prize = PositionL(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L)) }
            .sumOf<SlotMachine> { howManyMinTokensAreNeededToReachPrizeFast(it) }
    }

    fun howManyMinTokensAreNeededToReachPrizeFast(machine: SlotMachine): Long {
        fun SlotMachine.isPrizeReachable(): Boolean {
            val gcdX = gcd(buttonA.xDiff, buttonB.xDiff)
            val gcdY = gcd(buttonA.yDiff, buttonB.yDiff)

            // AI says that if the target coordinates are NOT divisible by their respective GCDs, then the prize is not reachable at all
            return (prize.x % gcdX == 0L) && (prize.y % gcdY == 0L)
        }

        if (!machine.isPrizeReachable()) return 0L

        val (buttonA, buttonB) = listOf(machine.buttonA, machine.buttonB)

        val model = CpModel()

        val a = model.newIntVar(0, machine.maxButtonA, "a")
        val b = model.newIntVar(0, machine.maxButtonB, "b")

        val prizeX = model.newConstant(machine.prize.x)
        val prizeY = model.newConstant(machine.prize.y)

        model.addEquality(prizeX, LinearExpr.sum(arrayOf(LinearExpr.term(a, buttonA.xDiff.toLong()), LinearExpr.term(b, buttonB.xDiff.toLong()))))
        model.addEquality(prizeY, LinearExpr.sum(arrayOf(LinearExpr.term(a, buttonA.yDiff.toLong()), LinearExpr.term(b, buttonB.yDiff.toLong()))))

        model.minimize(LinearExpr.sum(arrayOf(LinearExpr.term(a, 3), LinearExpr.term(b, 1))))

        // Solve the model
        val solver = CpSolver()
        val status = solver.solve(model)

        // Check if a solution was found
        return when (status) {
            CpSolverStatus.OPTIMAL -> solver.objectiveValue().toLong() // // Return the minimum cost as the result
            else -> 0L // If unreachable, return 0
        }
    }

    fun howManyMinTokensAreNeededToReachPrizeNaive(machine: SlotMachine): Int {
        val generatedButtonPresses = mutableSetOf<Pair<Int, Int>>()

        val queue = PriorityQueue<GraphNode>(compareBy { it.cost })
        queue.add(GraphNode(PositionL(0, 0), 0, 0))

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (current.position == machine.prize) {
                return current.cost // Yay
            }

            val nextSteps = listOf(current.presA(machine), current.presB(machine))

            val reasonableNextSteps = nextSteps
                .filterNot { it.missedThePrize(machine) }
                .filter { it.buttonA <= machine.maxButtonA && it.buttonB <= machine.maxButtonB }
                .filter { (it.buttonA to it.buttonB) !in generatedButtonPresses }

            queue.addAll(reasonableNextSteps)

            reasonableNextSteps.forEach { generatedButtonPresses.add(it.buttonA to it.buttonB) }
        }

        return 0; // target is unreachable, 0 tokens
    }

    data class GraphNode(
        val position: PositionL,
        val buttonA: Int,
        val buttonB: Int,
    ) {

        // pressing A costs 3
        // pressing B costs 1
        val cost: Int = (buttonA * 3) + buttonB

        fun presA(machine: SlotMachine): GraphNode = copy(
            position = position + machine.buttonA,
            buttonA = buttonA + 1,
        )

        fun presB(machine: SlotMachine): GraphNode = copy(
            position = position + machine.buttonB,
            buttonB = buttonB + 1,
        )

        fun missedThePrize(machine: SlotMachine): Boolean =
            this.position.x > machine.prize.x || this.position.y > machine.prize.y

    }

    /**
     * Conditions:
     * prize.x = a * vectorA.x + b * vectorB.x
     * prize.y = a * vectorA.y + b * vectorB.y
     * cost = a * costA + b * costB
     * where (m >= 0 && n >= 0) and (costA = 3, costB = 1)
     *
     * Task:
     * search for "m" and "n" and return minimal cost
     */
    data class SlotMachine(
        val buttonA: Distance,
        val buttonB: Distance,
        val prize: PositionL,
    ) {

        // pressing the buttons more would overshoot the prize
        val maxButtonA = minOf(prize.x / buttonA.xDiff, prize.y / buttonA.yDiff)
        val maxButtonB = minOf(prize.x / buttonB.xDiff, prize.y / buttonB.yDiff)

    }

    data class PositionL(val x: Long, val y: Long) {

        operator fun plus(other: Distance): PositionL =
            PositionL(this.x + other.xDiff, this.y + other.yDiff)

        override fun toString(): String = "(x=$x, y=$y)"

    }

    companion object {

        fun parseSlotMachine(input: List<String>): SlotMachine =
            SlotMachine(
                parseButton(input[0]),
                parseButton(input[1]),
                parsePrize(input[2]),
            )

        fun parseButton(input: String): Distance =
            buttonPattern.matchEntire(input.trim())
                ?.let { Distance(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
                ?: error("Bad input: $input")

        fun parsePrize(input: String): PositionL =
            prizePattern.matchEntire(input.trim())
                ?.let { PositionL(it.groupValues[1].toLong(), it.groupValues[2].toLong()) }
                ?: error("Bad input: $input")

        val buttonPattern = "Button\\s*[AB]:\\s*X\\+(\\d+),\\s*Y\\+(\\d+)".toRegex()
        val prizePattern = "Prize:\\s*X=(\\d+),\\s*Y=(\\d+)".toRegex()

    }

}
