package aoc.y2024

import aoc.utils.Resource
import aoc.utils.strings.matchEntire
import arrow.core.sort

fun Resource.day24(): Day24 = Day24.parse(content())

data class Day24(
    val initialStates: Map<String, Boolean>,
    val inputGates: Map<String, CommutativeBinaryGate<String, String>>,
) {

    val result1 by lazy { evaluateGatesIntoANumberFromZ() }
    val result2 by lazy { listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectlySolveManually(inputGates.toMutableMap()) }

    fun listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectlySolveManually(
        outputToGate: MutableMap<String, CommutativeBinaryGate<String, String>>
    ): String {
        val switchGates = mutableListOf<Pair<String, String>>()

        switchGates.add("z15" to "qnw")

        // carry = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
        // sum   = xBit xor yBit xor carry

        for ((a, b) in switchGates) {
            val aGate = outputToGate[a]!!
            val bGate = outputToGate[b]!!

            outputToGate[a] = bGate
            outputToGate[b] = aGate
        }

        return listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(
            outputToGate,
            switchGates
        )
    }

    fun listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(
        outputToGate: MutableMap<String, CommutativeBinaryGate<String, String>>,
        switchGates: MutableList<Pair<String, String>>
    ): String {
        // the system you're simulating is trying to add two binary numbers.
        // it is treating the bits on wires starting with x as one binary number,
        // treating the bits on wires starting with y as a second binary number,
        // and then attempting to add those two numbers together.
        // The output of this operation is produced as a binary number on the wires starting with z.

        val state = initialStates.evaluateLiveSystem(inputGates)
        var x = gatesToNumberByPrefix("x", state)
        var y = gatesToNumberByPrefix("y", state)
        var actualZ = gatesToNumberByPrefix("z", state)

        val expectedZ = x + y
        val diffZ = actualZ xor expectedZ

        val gateToOutput = outputToGate.entries.associate { it.value to it.key }

        fun inlineGates(name: String, state: MutableMap<String, String>, inliningRound: MutableCollection<String>): String {
            if (name !in initialStates) inliningRound.add(name)

            return state.getOrPut(name) {
                when (val gate = outputToGate[name] ?: error("Inlining error - unknown gate: '$name'")) {
                    is AndGate -> "(${inlineGates(gate.a, state, inliningRound)} AND ${inlineGates(gate.b, state, inliningRound)})"
                    is OrGate -> "(${inlineGates(gate.a, state, inliningRound)}  OR ${inlineGates(gate.b, state, inliningRound)})"
                    is XorGate -> "(${inlineGates(gate.a, state, inliningRound)} XOR ${inlineGates(gate.b, state, inliningRound)})"
                }
            }
        }

        val inlinedState = initialStates.mapValues { it.key }.toMutableMap()

//        for (name in state.keys.filter { it.startsWith("z") }.sorted().reversed()) {
//            inlineGates(name, inlinedState)
//        }

        fun reRunInliningRound(resultGateName: String, inliningRound: MutableCollection<String>) {
            inliningRound.forEach { name -> inlinedState.remove(name) }
            inliningRound.clear()
            inlineGates(resultGateName, inlinedState, inliningRound)
        }

//        val switchedGates = mutableListOf<String>()
//        fun getGateWithin(gate: CommutativeBinaryGate<*, *>, inliningRound: MutableCollection<String>, resultGateName: String): String {
//            val gateName = gateToOutput[gate]
//            if (gateName != null && gateName in inliningRound) return gateName
//
//
//
//            reRunInliningRound(resultGateName, inliningRound)
//
//            return gateName!!
//        }

        fun findGateName(gate: CommutativeBinaryGate<*, *>): String {
            if (gate.a is String && gate.b is String) {
                return gateToOutput[gate] ?: error("Gate not found: $gate")
            }

            val a = if (gate.a is String) (gate.a as String) else findGateName(gate.a as CommutativeBinaryGate<*, *>)
            val b = if (gate.b is String) (gate.b as String) else findGateName(gate.b as CommutativeBinaryGate<*, *>)

            return copy(gate, a, b).let { flattened ->
                gateToOutput[flattened] ?: error("Gate not found: $flattened")
            }
        }

        fun patternMatchOrFixAndReturnUsedCarryName(additionGate: CommutativeBinaryGate<*, *>, resultGateName: String) {
            // resultWithoutCarryGate: CommutativeBinaryGate<String, String>
            // val fullAdditionGate = if (carryGate == null) resultWithoutCarryGate else XorGate.of(resultWithoutCarryGate, carryGate)

            val inliningRound = mutableListOf<String>()
            inlineGates(resultGateName, inlinedState, inliningRound)

            println()
            inliningRound.sortedWith(compareBy<String> { inlinedState[it]?.length ?: 0 }.thenComparingInt { it.length })
                .forEach { name -> println("  $name <- ${outputToGate[name]}      ~ ${inlinedState[name]}") }
            println("  inliningOrder: ${inliningRound}")

            val foundGateOutput = findGateName(additionGate)
            require(foundGateOutput == resultGateName) {
                "expected $resultGateName but got $foundGateOutput"
            }
        }

        var carryFromPrevName: String? = null

        var sum = 0L
        var carry = 0L
        for (i in 0..<64) {
            val xGateName = "x" + (i.toString().padStart(2, '0'))
            val yGateName = "y" + (i.toString().padStart(2, '0'))

            val resultGateName = "z" + (i.toString().padStart(2, '0'))
            if (resultGateName !in outputToGate) break

            val xBit = (x shr i) and 1
            val yBit = (y shr i) and 1
            val actualZBit = (actualZ shr i) and 1
            val expectedZBit = (expectedZ shr i) and 1

            println()
            println("bit $i: x=$xBit, y=$yBit, expectedZ=$expectedZBit, actualZ=$actualZBit")

            val sumBit = xBit xor yBit xor carry
            println("   $resultGateName <- $xGateName XOR $yGateName XOR carry")
            println("     $sumBit <-   $xBit XOR   $yBit XOR $carry")

            val carryPrev = carry
            carry = (carryPrev and (xBit xor yBit)) or (xBit and yBit)
            sum = sum or (sumBit shl i)

            println("     carryAfter <- (carryPrev and ($xGateName xor $yGateName)) or ($xGateName and $yGateName)")
            println("              $carry <- ($carryPrev         and (  $xBit xor   $yBit)) or (  $xBit and   $yBit)")

            val xPrevGateName = "x" + ((i - 1).toString().padStart(2, '0'))
            val yPrevGateName = "y" + ((i - 1).toString().padStart(2, '0'))

            val resultWithoutCarryGate = XorGate.of(xGateName, yGateName)
            if (i == 0) {
                // carry = 0
                // sum   = xBit xor yBit xor carry
                patternMatchOrFixAndReturnUsedCarryName(resultWithoutCarryGate, resultGateName)

            } else if (i == 1) {
                // carry = (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = AndGate.of(xPrevGateName, yPrevGateName)
                patternMatchOrFixAndReturnUsedCarryName(XorGate.of(resultWithoutCarryGate, carryGate), resultGateName)
                carryFromPrevName = findGateName(carryGate)

            } else {
                // carry = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = OrGate.of(
                    AndGate.of(carryFromPrevName!!, XorGate.of(xPrevGateName, yPrevGateName)),
                    AndGate.of(xPrevGateName, yPrevGateName)
                )
                patternMatchOrFixAndReturnUsedCarryName(XorGate.of(resultWithoutCarryGate, carryGate), resultGateName)
                carryFromPrevName = findGateName(carryGate)
            }

            if (expectedZBit != actualZBit) {
                println()
                println("  x + y = z")
                println("  $x + $y = $actualZ")
                println()
                println("  b${x.toString(2).padStart(64, '0')} (x)")
                println("+ b${y.toString(2).padStart(64, '0')} (y)")
                println("= b${actualZ.toString(2).padStart(64, '0')} (actual z)")
                println("  b${expectedZ.toString(2).padStart(64, '0')} (expected z)")
                println("  b${diffZ.toString(2).padStart(64, '0')} (diff z)")
                println("   " + ("^".padStart(64 - i)))

                error("expectedZBit != actualZBit")
            }

//            val inliningRound = mutableListOf<String>()
//            inlineGates(resultGateName, inlinedState, inliningRound)
//
//            println()
//            inliningRound.sortedBy { inlinedState[it]?.length ?: 0 }.forEach { name -> println("  $name -> ${outputToGate[name]}      ~ ${inlinedState[name]}") }
//            println("  inliningOrder: ${inliningRound}")

//            val resultWithoutCarryName = getGateWithin(XorGate.of(xGateName, yGateName), inliningRound, resultGateName)
//            val carryFromPrevPrimeCandidate = inliningRound.lastOrNull { it != resultWithoutCarryName && !it.startsWith("z") }
//
//            if (i == 0) {
//                // TODO: its correct in the input, so let's ignore for now
//                require(resultWithoutCarryName == "z00")
//
//                println()
//                println("  ${resultGateName} -> xBit XOR yBit -> $xGateName XOR $yGateName")
//
//            } else if (i == 1) {
//                // TODO: its correct in the input, so let's ignore for now
//                require(carryFromPrevName == null)
//
//                println()
//                println("  ${resultGateName} -> xBit XOR yBit XOR carry -> $xGateName XOR $yGateName XOR carry -> $resultWithoutCarryName? XOR " + carryFromPrevPrimeCandidate + "?")
//
//                val carryFromPrevGateName = getGateWithin(AndGate.of(xPrevGateName, yPrevGateName), inliningRound, resultGateName)
//
//                carryFromPrevName = carryFromPrevGateName
//
//            } else {
//                require(carryFromPrevName != null)
//
//                println()
//                println("  ${resultGateName} -> xBit XOR yBit XOR carry -> $xGateName XOR $yGateName XOR carry -> $resultWithoutCarryName? XOR " + carryFromPrevPrimeCandidate + "?")
//
//                // currentCarry = (carryFromPrevName and (xBit xor yBit)) or (xBit and yBit)
//                val carryFromPrevAndExistingGateName = getGateWithin(AndGate.of(xPrevGateName, yPrevGateName), inliningRound, resultGateName)
//                val carryFromPrevXorExistingGateName = getGateWithin(XorGate.of(xPrevGateName, yPrevGateName), inliningRound, resultGateName)
//                val carryFromPrevLeftExistingGateName = getGateWithin(AndGate.of(carryFromPrevName, carryFromPrevXorExistingGateName), inliningRound, resultGateName)
//                val carryFromPrevGateName = getGateWithin(OrGate.of(carryFromPrevLeftExistingGateName, carryFromPrevAndExistingGateName), inliningRound, resultGateName)
//
//                carryFromPrevName = carryFromPrevGateName
//            }
        }

        println("final sum: $sum, expectedZ: $expectedZ")
        println("sum ok? " + (sum == expectedZ))

        TODO()

        return switchGates.flatMap { listOf(it.first, it.second) }.distinct().sorted().joinToString(",")
    }

    // Gates wait until both inputs are received before producing output; wires can carry 0, 1 or no value at all.
    // There are no loops; once a gate has determined its output, the output will not change until the whole system is reset.
    // Each wire is connected to at most one gate output, but can be connected to many gate inputs.
    fun evaluateGatesIntoANumberFromZ(): Long {
        val state = initialStates.evaluateLiveSystem(inputGates)

        var result = gatesToNumberByPrefix("z", state)
        println("result: b${result.toString(2).padStart(64, '0')}")

        return result
    }

    // The system is trying to produce a number by combining the bits on all wires starting with z.
    // z00 is the least significant bit, then z01, then z02, and so on.
    fun gatesToNumberByPrefix(prefix: String, state: Map<String, Boolean>): Long {
        var result = 0L
        for ((index, zGate) in state.keys.filter { it.startsWith(prefix) }.sorted().withIndex()) {
            if (state[zGate] == false) continue
            result = result or (1L shl index)
        }
        return result
    }

    fun Map<String, Boolean>.evaluateLiveSystem(gates: Map<String, CommutativeBinaryGate<String, String>>): MutableMap<String, Boolean> {
        val state = initialStates.toMutableMap()

        fun evaluate(name: String): Boolean = state.getOrPut(name) {
            when (val gate = gates[name]!!) {
                is AndGate -> evaluate(gate.a) and evaluate(gate.b)
                is OrGate -> evaluate(gate.a) or evaluate(gate.b)
                is XorGate -> evaluate(gate.a) xor evaluate(gate.b)
            }
        }

        gates.forEach { (name, _) -> evaluate(name) }

        return state
    }

    sealed interface Gate
    sealed interface CommutativeBinaryGate<A, B> : Gate where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any {
        val a: A
        val b: B
    }

    // AND gates output 1 if both inputs are 1; if either input is 0, these gates output 0.
    data class AndGate<A, B>(override val a: A, override val b: B) : Comparable<AndGate<A, B>>, CommutativeBinaryGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any {

        override fun compareTo(other: AndGate<A, B>): Int = compareValuesBy(this, other, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AndGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + "AND".hashCode()

        override fun toString(): String = "($a AND $b)"

        companion object {
            fun <A, B> of(a: A, b: B): AndGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any = AndGate(a, b)
        }

    }

    // OR gates output 1 if one or both inputs is 1; if both inputs are 0, these gates output 0.
    data class OrGate<A, B>(override val a: A, override val b: B) : Comparable<OrGate<A, B>>, CommutativeBinaryGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any {

        override fun compareTo(other: OrGate<A, B>): Int = compareValuesBy(this, other, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OrGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + "OR".hashCode()

        override fun toString(): String = "($a  OR $b)"

        companion object {
            fun <A, B> of(a: A, b: B): OrGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any = OrGate(a, b)
        }

    }

    // XOR gates output 1 if the inputs are different; if the inputs are the same, these gates output 0.
    data class XorGate<A, B>(override val a: A, override val b: B) : Comparable<XorGate<A, B>>, CommutativeBinaryGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any {

        override fun compareTo(other: XorGate<A, B>): Int = compareValuesBy(this, other, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is XorGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + "XOR".hashCode()

        override fun toString(): String = "($a XOR $b)"

        companion object {
            fun <A, B> of(a: A, b: B): XorGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any = XorGate(a, b)
        }

    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <A, B> copy(of: CommutativeBinaryGate<*, *>, newA: A, newB: B): CommutativeBinaryGate<A, B> where A : Comparable<A>, A : Any, B : Comparable<B>, B : Any {
            if (newA is String && newB is String) {
                val (a, b) = sort(newA, newB)

                return when (of) {
                    is AndGate<*, *> -> AndGate.of(a as A, b as B)
                    is OrGate<*, *> -> OrGate.of(a as A, b as B)
                    is XorGate<*, *> -> XorGate.of(a as A, b as B)
                }
            } else {
                return when (of) {
                    is AndGate<*, *> -> AndGate.of(newA, newB)
                    is OrGate<*, *> -> OrGate.of(newA, newB)
                    is XorGate<*, *> -> XorGate.of(newA, newB)
                }
            }
        }

        fun parse(input: String): Day24 =
            input.trim().split("\n\n", limit = 2)
                .let { (initialStates, gates) ->
                    Day24(
                        parseInitialStates(initialStates),
                        parseGates(gates)
                    )
                }

        fun parseInitialStates(input: String): Map<String, Boolean> =
            input.trim().lines()
                .map { it.trim() }
                .associate { it.matchEntire(statePattern) { it.groupValues[1] to (it.groupValues[2] == "1") } }

        // The gates all operate on values that are either true (1) or false (0).
        fun parseGates(input: String): Map<String, CommutativeBinaryGate<String, String>> =
            input.trim().lines()
                .map { it.trim() }
                .associate { it.matchEntire(gatePattern) { it.groupValues[4] to parseGate(it.groupValues[2], it.groupValues[1], it.groupValues[3]) } }

        fun parseGate(type: String, left: String, right: String): CommutativeBinaryGate<String, String> =
            sort(left, right).let { (first, second) ->
                return when (type) {
                    "AND" -> AndGate.of(first, second)
                    "OR" -> OrGate.of(first, second)
                    "XOR" -> XorGate.of(first, second)
                    else -> throw IllegalArgumentException("Unknown gate type: $type")
                }
            }

        val statePattern = "([^:]{3}): (\\d)".toRegex()
        val gatePattern = "([^\\s]+) (AND|OR|XOR) ([^\\s]+) -> ([^\\s]+)".toRegex()

    }

}
