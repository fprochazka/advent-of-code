package aoc.y2024

import aoc.utils.AocDebug
import aoc.utils.Resource
import aoc.utils.strings.matchEntire
import aoc.y2024.Day24.GateInput.WireRef
import arrow.core.sort
import java.util.*
import kotlin.math.abs

fun Resource.day24(): Day24 = Day24.parse(content())

data class Day24(
    val initialStates: Map<String, Boolean>,
    val inputGates: Map<String, CommutativeBinaryGate<WireRef, WireRef>>,
) {

    val result1 by lazy { evaluateGatesIntoANumberFromZ() }
    val result2 by lazy { listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(inputGates.toMutableMap()) }

    fun listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(outputToGate: Map<String, CommutativeBinaryGate<WireRef, WireRef>>): String {
        // the system you're simulating is trying to add two binary numbers.
        // it is treating the bits on wires starting with x as one binary number,
        // treating the bits on wires starting with y as a second binary number,
        // and then attempting to add those two numbers together.
        // The output of this operation is produced as a binary number on the wires starting with z.

        val gates = Gates(outputToGate)

        val (x, y) = initialStates.evaluateLiveSystem(outputToGate)
            .let { state -> gatesToNumberByPrefix("x", state) to gatesToNumberByPrefix("y", state) }

        val knownCarryGates = mutableSetOf<String>()

        fun inlineGates(name: String, state: MutableMap<String, GateInput>): MutableCollection<String> {
            val inliningRound = mutableListOf<String>()

            fun inline(name: String): GateInput {
                if (name.startsWith("x") || name.startsWith("y")) return GateInput.of(name)
                if (name in knownCarryGates) return GateInput.of(name)
                if (name !in initialStates) inliningRound.add(name)

                return state.getOrPut(name) {
                    gates.gateByOutput(name)
                        ?.let { gate -> copy(gate, inline(gate.a.name), inline(gate.b.name)) }
                        ?: error("Inlining error - unknown gate: '$name'")
                }
            }

            inline(name)

            return inliningRound
        }

        val inlinedState = mutableMapOf<String, GateInput>()

        fun reRunInliningRound(resultGateName: String, inliningRound: MutableCollection<String>) {
            inliningRound.forEach { name -> inlinedState.remove(name) }
            inliningRound.clear()
            inliningRound.addAll(inlineGates(resultGateName, inlinedState))
        }

        fun <A : GateInput, B : GateInput> patternMatchOrFix(expectedGate: CommutativeBinaryGate<A, B>, resultGateName: String) {
            val inliningRound = inlineGates(resultGateName, inlinedState)

            if (AocDebug.enabled) {
                println()
                inliningRound.sortedWith(compareBy<String> { inlinedState[it]?.toString()?.length ?: 0 }.thenComparingInt { it.length })
                    .forEach { name -> println("  $name <- ${outputToGate[name]}    ~ ${inlinedState[name]}") }
                println("  inliningOrder: ${inliningRound}")
            }

            var fixedWires = false

            fun <C : GateInput, D : GateInput> matchOrFix(expectedGate: CommutativeBinaryGate<C, D>, resultGateName: String): Boolean {
                val expectedGateName = gates.outputNameBy(expectedGate)
                if (expectedGateName == resultGateName) return true // OK

                if (expectedGateName != null) {
                    // simple case fast-path
                    gates.switchWires(resultGateName, expectedGateName)
                    if (gates.outputNameBy(expectedGate) == resultGateName) {
                        reRunInliningRound(resultGateName, inliningRound)
                        fixedWires = true
                        return true // OK
                    }
                    gates.returnWires(resultGateName, expectedGateName)
                }

                val expectedGateA = expectedGate.a.asGateRef()
                val expectedGateB = expectedGate.b.asGateRef()

                val expectedGateAOps = expectedGateA?.getInlinedOperationsCount() ?: 0
                val expectedGateBOps = expectedGateB?.getInlinedOperationsCount() ?: 0

                val resultGate = gates.gateByOutput(resultGateName) ?: error("Cannot find gate for '$resultGateName'")
                val resultGateAOps = inlinedState[resultGate.a.name]?.getInlinedOperationsCount() ?: 0
                val resultGateBOps = inlinedState[resultGate.b.name]?.getInlinedOperationsCount() ?: 0

                if (expectedGate.type != resultGate.type) return false

                if (expectedGateA != null) {
                    if (expectedGateAOps.isCloserToNThanM(resultGateAOps, resultGateBOps)) {
                        var matches = matchOrFix(expectedGateA, resultGate.a.name)
                        if (!matches) return false
                    } else {
                        var matches = matchOrFix(expectedGateA, resultGate.b.name)
                        if (!matches) return false
                    }
                }

                if (expectedGateB != null) {
                    if (expectedGateBOps.isCloserToNThanM(resultGateAOps, resultGateBOps)) {
                        var matches = matchOrFix(expectedGateB, resultGate.a.name)
                        if (!matches) return false
                    } else {
                        var matches = matchOrFix(expectedGateB, resultGate.b.name)
                        if (!matches) return false
                    }
                }

                return true
            }

            val matches = matchOrFix(expectedGate, resultGateName)
            if (!matches) {
                throw IllegalStateException("Unable to fix $resultGateName = $expectedGate")
            }

            if (AocDebug.enabled && fixedWires) {
                println()
                println("  fixed wires:")
                inliningRound.sortedWith(compareBy<String> { inlinedState[it]?.toString()?.length ?: 0 }.thenComparingInt { it.length })
                    .forEach { name -> println("  $name <- ${outputToGate[name]}    ~ ${inlinedState[name]}") }
            }
        }

        var carryFromPrevName: String? = null

        var sum = 0L
        var carry = 0L
        for (i in 0..<64) {
            val xGateName = "x" + (i.toString().padStart(2, '0'))
            val yGateName = "y" + (i.toString().padStart(2, '0'))

            val resultGateName = "z" + (i.toString().padStart(2, '0'))
            val resultNextGateName = "z" + ((i + 1).toString().padStart(2, '0'))
            if (resultGateName !in outputToGate) break
            val isLastCarryBit = resultNextGateName !in outputToGate

            val carryPrev = carry

            val xBit = (x shr i) and 1
            val yBit = (y shr i) and 1
            val sumBit = xBit xor yBit xor carry
            carry = (carryPrev and (xBit xor yBit)) or (xBit and yBit)
            sum = sum or (sumBit shl i)

            if (AocDebug.enabled) {
                println()
                println("bit $i: $xGateName=$xBit, $yGateName=$yBit, carry=$carryPrev")
                println("   $resultGateName <- ($xGateName XOR $yGateName) XOR carry")
                println("     $sumBit <- (  $xBit XOR   $yBit) XOR     $carryPrev")
                println("     $sumBit <- ${xBit xor yBit} xor $carryPrev")

                println("     carryAfter <- (carryPrev AND ($xGateName XOR $yGateName)) OR ($xGateName AND $yGateName)")
                println("              $carry <- ($carryPrev         and (  $xBit xor   $yBit)) or (  $xBit and   $yBit)")
                println("              $carry <- ($carryPrev and ${xBit xor yBit}) or ${xBit and yBit}")
                println("              $carry <- ${carryPrev and (xBit xor yBit)} or ${xBit and yBit}")
            }

            val xPrevGateName = "x" + ((i - 1).toString().padStart(2, '0'))
            val yPrevGateName = "y" + ((i - 1).toString().padStart(2, '0'))

            if (i == 0) {
                // carry = 0
                // sum   = xBit xor yBit xor carry
                patternMatchOrFix(XorGate.of(xGateName, yGateName), resultGateName)

            } else if (i == 1) {
                // carry = (0 and (0 xor 0)) or (xPrevBit and yPrevBit) = (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = AndGate.of(xPrevGateName, yPrevGateName)
                patternMatchOrFix(XorGate.of(XorGate.of(xGateName, yGateName), carryGate), resultGateName)
                carryFromPrevName = gates.outputNameBy(carryGate)

            } else if (isLastCarryBit) {
                // no X and Y, just carry
                // sum   = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
                val carryGate = OrGate.of(
                    AndGate.of(carryFromPrevName!!, XorGate.of(xPrevGateName, yPrevGateName)),
                    AndGate.of(xPrevGateName, yPrevGateName)
                )
                patternMatchOrFix(carryGate, resultGateName)

            } else {
                // carry = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = OrGate.of(
                    AndGate.of(carryFromPrevName!!, XorGate.of(xPrevGateName, yPrevGateName)),
                    AndGate.of(xPrevGateName, yPrevGateName)
                )
                patternMatchOrFix(XorGate.of(XorGate.of(xGateName, yGateName), carryGate), resultGateName)
                carryFromPrevName = gates.outputNameBy(carryGate)
            }

            carryFromPrevName?.let {
                knownCarryGates.add(it)
                if (AocDebug.enabled) println("  carry gate: $it")
            }
        }

        val wireChanges = gates.changedWires.keys.sorted()
        require(gates.changedWires.size == 8) {
            "Expected only 4 pairs of wire changes, but found: $wireChanges"
        }

        return wireChanges.joinToString(",")
    }

    // Gates wait until both inputs are received before producing output; wires can carry 0, 1 or no value at all.
    // There are no loops; once a gate has determined its output, the output will not change until the whole system is reset.
    // Each wire is connected to at most one gate output, but can be connected to many gate inputs.
    fun evaluateGatesIntoANumberFromZ(): Long {
        val state = initialStates.evaluateLiveSystem(inputGates)

        var result = gatesToNumberByPrefix("z", state)

        if (AocDebug.enabled) {
            println("result: b${result.toString(2).padStart(64, '0')}")
        }

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

    class Gates(inputGates: Map<String, CommutativeBinaryGate<WireRef, WireRef>>) {

        val outputToGate: Map<String, CommutativeBinaryGate<WireRef, WireRef>> = inputGates
        val gateToOutput: Map<CommutativeBinaryGate<WireRef, WireRef>, String> = outputToGate.entries.associate { it.value to it.key }

        val componentToGate = HashMap<String, MutableList<CommutativeBinaryGate<WireRef, WireRef>>>().apply {
            outputToGate.forEach { (_, gate) ->
                this.getOrPut(gate.a.name) { mutableListOf() }.add(gate)
                this.getOrPut(gate.b.name) { mutableListOf() }.add(gate)
            }
        }

        val changedWires = mutableMapOf<String, String>()

        fun switchWires(a: String, b: String) {
            changedWires.put(a, b)
            changedWires.put(b, a)
        }

        fun returnWires(a: String, b: String) {
            changedWires.remove(a)
            changedWires.remove(b)
        }

        fun outputByGate(gate: CommutativeBinaryGate<WireRef, WireRef>): String? = gateToOutput[gate]?.let { changedWires[it] ?: it }
        fun gateByOutput(output: String): CommutativeBinaryGate<WireRef, WireRef>? = outputToGate[changedWires[output] ?: output]

        fun <A : GateInput, B : GateInput> outputNameBy(gate: CommutativeBinaryGate<A, B>): String? {
            gate.asWireInputsOrNull()?.let { return outputByGate(it) }

            val a = gate.a.visit({ it }, { outputNameBy(it)?.let { GateInput.of(it) } }) ?: return null
            val b = gate.b.visit({ it }, { outputNameBy(it)?.let { GateInput.of(it) } }) ?: return null

            return copy(gate, a, b).let { flattened -> outputByGate(flattened) }
        }

    }

    fun Map<String, Boolean>.evaluateLiveSystem(gates: Map<String, CommutativeBinaryGate<WireRef, WireRef>>): MutableMap<String, Boolean> {
        val state = initialStates.toMutableMap()

        fun evaluate(name: String): Boolean = state.getOrPut(name) {
            when (val gate = gates[name]!!) {
                is AndGate -> evaluate(gate.a.name) and evaluate(gate.b.name)
                is OrGate -> evaluate(gate.a.name) or evaluate(gate.b.name)
                is XorGate -> evaluate(gate.a.name) xor evaluate(gate.b.name)
            }.also { result ->
                if (AocDebug.enabled) {
                    println(" $name <- ${result.toInt()} <- ${state[gates[name]!!.a.name]!!.toInt()} ${gates[name]!!.type} ${state[gates[name]!!.b.name]!!.toInt()} <- ${gates[name]}")
                }
            }
        }

        gates.forEach { (name, _) -> evaluate(name) }

        return state
    }

    fun <V : Any> MutableMap<String, V>.switchGates(switch: Pair<String, String>): List<String> =
        this.switchGates(switch.first, switch.second)

    fun <V : Any> MutableMap<String, V>.switchGates(a: String, b: String): List<String> {
        val aGate = this[a]!!
        val bGate = this[b]!!

        this[a] = bGate
        this[b] = aGate

        return listOf(a, b)
    }

    sealed interface GateInput : Comparable<GateInput> {

        data class WireRef(val name: String) : GateInput {

            override fun compareTo(other: GateInput): Int {
                return when (other) {
                    is CommutativeBinaryGate<*, *> -> -1
                    is WireRef -> name.compareTo(other.name)
                }
            }

            override fun toString(): String = name

        }

        companion object {
            fun of(ref: Any): GateInput = when (ref) {
                is String -> of(ref)
                is GateInput -> ref
                else -> error("Cannot create GateInput from type ${ref.javaClass.name}")
            }

            fun <T : GateInput> of(ref: T): T = ref

            fun <A : GateInput, B : GateInput> of(ref: CommutativeBinaryGate<A, B>) = ref

            private val wireRefCache = HashMap<String, WireRef>(1 shl 8)

            fun of(ref: String): WireRef =
                wireRefCache.getOrPut(ref) { WireRef(ref) }

        }

    }

    sealed interface CommutativeBinaryGate<A : GateInput, B : GateInput> : GateInput {
        val a: A
        val b: B
        val type: Type

        enum class Type {
            AND,
            OR,
            XOR,
        }

    }

    // AND gates output 1 if both inputs are 1; if either input is 0, these gates output 0.
    data class AndGate<A : GateInput, B : GateInput>(override val a: A, override val b: B) : CommutativeBinaryGate<A, B> {

        override val type = CommutativeBinaryGate.Type.AND

        override fun compareTo(other: GateInput): Int = when (other) {
            is CommutativeBinaryGate<*, *> -> compareValuesBy(this, other.asUnknown(), { it.type }, { it.a }, { it.b })
            is WireRef -> 1
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AndGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()
        override fun toString(): String = "($a $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = AndGate(a, b)
        }

    }

    // OR gates output 1 if one or both inputs is 1; if both inputs are 0, these gates output 0.
    data class OrGate<A : GateInput, B : GateInput>(override val a: A, override val b: B) : CommutativeBinaryGate<A, B> {

        override val type = CommutativeBinaryGate.Type.OR

        override fun compareTo(other: GateInput): Int = when (other) {
            is CommutativeBinaryGate<*, *> -> compareValuesBy(this, other.asUnknown(), { it.type }, { it.a }, { it.b })
            is WireRef -> 1
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OrGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()

        override fun toString(): String = "($a  $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = OrGate(a, b)
        }

    }

    // XOR gates output 1 if the inputs are different; if the inputs are the same, these gates output 0.
    data class XorGate<A : GateInput, B : GateInput>(override val a: A, override val b: B) : CommutativeBinaryGate<A, B> {

        override val type = CommutativeBinaryGate.Type.XOR

        override fun compareTo(other: GateInput): Int = when (other) {
            is CommutativeBinaryGate<*, *> -> compareValuesBy(this, other.asUnknown(), { it.type }, { it.a }, { it.b })
            is WireRef -> 1
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is XorGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()

        override fun toString(): String = "($a $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = XorGate(a, b)
        }

    }

    companion object {

        val gateInlinedOperationsCount = IdentityHashMap<CommutativeBinaryGate<GateInput, GateInput>, Int>()

        fun GateInput.getInlinedOperationsCount(): Int = when (this) {
            is CommutativeBinaryGate<*, *> -> gateInlinedOperationsCount.getOrPut(this.asUnknown()) { 1 + this.a.getInlinedOperationsCount() + this.b.getInlinedOperationsCount() }
            is WireRef -> 0
        }

        fun Int.isCloserToNThanM(n: Int, m: Int): Boolean {
            return abs(this - n) < abs(this - m)
        }

        @Suppress("UNCHECKED_CAST")
        fun GateInput.asGateRef(): CommutativeBinaryGate<GateInput, GateInput>? = this as? CommutativeBinaryGate<GateInput, GateInput>

        fun GateInput.asWireRef(): WireRef? = this as? WireRef

        @Suppress("UNCHECKED_CAST")
        fun <R> GateInput.visit(
            onWire: (WireRef) -> R,
            onGate: (CommutativeBinaryGate<GateInput, GateInput>) -> R,
        ): R = when (this) {
            is WireRef -> onWire(this)
            is CommutativeBinaryGate<*, *> -> onGate(this as CommutativeBinaryGate<GateInput, GateInput>)
        }

        @Suppress("UNCHECKED_CAST")
        fun <A : GateInput, B : GateInput> CommutativeBinaryGate<A, B>.asWireInputsOrNull(): CommutativeBinaryGate<WireRef, WireRef>? =
            (if (a !is WireRef || b !is WireRef) null else this) as CommutativeBinaryGate<WireRef, WireRef>?

        @Suppress("UNCHECKED_CAST")
        fun <A : GateInput, B : GateInput> CommutativeBinaryGate<A, B>.asUnknown(): CommutativeBinaryGate<GateInput, GateInput> =
            this as CommutativeBinaryGate<GateInput, GateInput>

        fun Boolean.toInt() = if (this) 1 else 0

        @Suppress("UNCHECKED_CAST")
        fun <InA : GateInput, InB : GateInput, OutA : GateInput, OutB : GateInput> copy(of: CommutativeBinaryGate<InA, InB>, newA: OutA, newB: OutB): CommutativeBinaryGate<OutA, OutB> {
            var a = newA
            var b = newB

            if (a is WireRef && b is WireRef) { // both are the same type
                val (first, second) = sort(a, b)
                a = first as OutA
                b = second as OutB
            }

            return when (of) {
                is AndGate<*, *> -> AndGate.of(a, b)
                is OrGate<*, *> -> OrGate.of(a, b)
                is XorGate<*, *> -> XorGate.of(a, b)
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
        fun parseGates(input: String): Map<String, CommutativeBinaryGate<WireRef, WireRef>> =
            input.trim().lines()
                .map { it.trim() }
                .associate { it.matchEntire(gatePattern) { it.groupValues[4] to parseGate(it.groupValues[2], it.groupValues[1], it.groupValues[3]) } }

        fun parseGate(type: String, left: String, right: String): CommutativeBinaryGate<WireRef, WireRef> =
            sort(GateInput.of(left), GateInput.of(right)).let { (first, second) ->
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
