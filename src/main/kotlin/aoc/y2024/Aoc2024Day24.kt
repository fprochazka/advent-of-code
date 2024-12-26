package aoc.y2024

import aoc.utils.AocDebug
import aoc.utils.Resource
import aoc.utils.strings.matchEntire
import aoc.y2024.Day24.GateInput.GateRef
import aoc.y2024.Day24.GateInput.WireRef
import arrow.core.sort
import kotlin.Any
import kotlin.Comparable

fun Resource.day24(): Day24 = Day24.parse(content())

data class Day24(
    val initialStates: Map<String, Boolean>,
    val inputGates: Map<String, CommutativeBinaryGate<WireRef, WireRef>>,
) {

    val result1 by lazy { evaluateGatesIntoANumberFromZ() }

    // val result2 by lazy { listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectlySolveManually(inputGates.toMutableMap()) }
    val result2 by lazy { listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(inputGates.toMutableMap()) }

    fun listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectlySolveManually(
        outputToGate: MutableMap<String, CommutativeBinaryGate<WireRef, WireRef>>
    ): String {
        val switchGates = mutableListOf<String>()

        // x14 XOR y14 -> gdr
        // x15 XOR y15 -> rjm
        // x14 AND y14 -> gnc
        // gdr AND ftc -> dqg
        // dqg  OR gnc -> ctg
        // ctg XOR rjm -> qnw
        // dnn  OR mrm -> z15

        // z15 = (x15 xor y15) xor ((ftc and (x14 xor y14)) or (x14 and y14))
        // z15 = rjm xor ((ftc and gdr) or gnc)
        // z15 = rjm xor (dqg or gnc)
        // z15 = rjm xor ctg
        // z15 = qnw
        switchGates.addAll(outputToGate.switchGates("z15" to "qnw"))

        // y19 AND x19 -> vct
        // x19 XOR y19 -> qpk
        // x20 XOR y20 -> msn
        // nbt  OR vrq -> wwc
        // wwc XOR qpk -> z19
        // wwc AND qpk -> mjm
        // mjm  OR vct -> wrb
        // wrb XOR msn -> cqr
        // x20 AND y20 -> z20

        // z20 = (x20 XOR y20) XOR ((wwc AND (x19 XOR y19)) or (x19 AND y19))
        // z20 = msn XOR ((wwc AND qpk) or vct)
        // z20 = msn XOR (mjm or vct)
        // z20 = msn XOR wrb
        // z20 = cqr
        switchGates.addAll(outputToGate.switchGates("z20" to "cqr"))

        // y27 AND x27 -> nfj
        // x27 XOR y27 -> ncd
        // x26 XOR y26 -> hdm
        // y26 AND x26 -> nsh
        // hdm AND pfw -> mmj
        // mmj  OR nsh -> trt
        // trt XOR nfj -> z27

        // z27 = (x27 XOR y27) XOR ((pfw and (x26 xor y26)) or (x26 and y26))
        // z27 = ncd XOR ((pfw and hdm) or nsh)
        // z27 = ncd XOR (mmj or nsh)
        // z27 = ncd XOR trt
        // there is no (ncd XOR trt) gate, but (nfj XOR trt) exists
        // z27 = nfj XOR ((pfw and hdm) or nsh)
        // z27 = nfj XOR (mmj or nsh)
        // z27 = nfj XOR trt
        switchGates.addAll(outputToGate.switchGates("nfj" to "ncd"))

        // y37 XOR x37 -> dnt
        // x36 XOR y36 -> sjw
        // x36 AND y36 -> crj
        // twd AND sjw -> dvm
        // crj  OR dvm -> fcm
        // fcm XOR dnt -> vkg
        // dnt AND fcm -> z37

        // z37 = (x37 xor y37) xor ((twd and (x36 xor y36)) or (x36 and y36))
        // z37 = dnt xor ((twd and sjw) or crj)
        // z37 = dnt xor ((twd and sjw) or crj)
        // z37 = dnt xor (dvm or crj)
        // z37 = dnt xor fcm
        // z37 = vkg
        switchGates.addAll(outputToGate.switchGates("z37" to "vkg"))

        // sum = (xBit xor yBit) xor ((prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit))

        return listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(
            outputToGate,
            switchGates
        )
    }

    fun listGatesThatNeedToBeSwappedSoThatSystemPerformsAdditionCorrectly(
        outputToGate: MutableMap<String, CommutativeBinaryGate<WireRef, WireRef>>,
        switchGates: MutableList<String> = mutableListOf(),
    ): String {
        // the system you're simulating is trying to add two binary numbers.
        // it is treating the bits on wires starting with x as one binary number,
        // treating the bits on wires starting with y as a second binary number,
        // and then attempting to add those two numbers together.
        // The output of this operation is produced as a binary number on the wires starting with z.

        val state = initialStates.evaluateLiveSystem(outputToGate)
        var x = gatesToNumberByPrefix("x", state)
        var y = gatesToNumberByPrefix("y", state)
        var actualZ = gatesToNumberByPrefix("z", state)

        val gateToOutput: Map<CommutativeBinaryGate<WireRef, WireRef>, String> = outputToGate.entries.associate { it.value to it.key }

        fun inlineGates(name: String, state: MutableMap<String, String>, inliningRound: MutableCollection<String>): String {
            if (name !in initialStates) inliningRound.add(name)

            return state.getOrPut(name) {
                when (val gate = outputToGate[name] ?: error("Inlining error - unknown gate: '$name'")) {
                    is AndGate -> "(${inlineGates(gate.a.name, state, inliningRound)} AND ${inlineGates(gate.b.name, state, inliningRound)})"
                    is OrGate -> "(${inlineGates(gate.a.name, state, inliningRound)}  OR ${inlineGates(gate.b.name, state, inliningRound)})"
                    is XorGate -> "(${inlineGates(gate.a.name, state, inliningRound)} XOR ${inlineGates(gate.b.name, state, inliningRound)})"
                }
            }
        }

        val inlinedState = initialStates.mapValues { it.key }.toMutableMap()

        fun reRunInliningRound(resultGateName: String, inliningRound: MutableCollection<String>) {
            inliningRound.forEach { name -> inlinedState.remove(name) }
            inliningRound.clear()
            inlineGates(resultGateName, inlinedState, inliningRound)
        }

        fun <A : GateInput, B : GateInput> patternMatchOrFixAndReturnUsedCarryName(expectedGate: CommutativeBinaryGate<A, B>, resultGateName: String) {
            // resultWithoutCarryGate: CommutativeBinaryGate<String, String>
            // val fullAdditionGate = if (carryGate == null) resultWithoutCarryGate else XorGate.of(resultWithoutCarryGate, carryGate)

            val inliningRound = mutableListOf<String>()
            inlineGates(resultGateName, inlinedState, inliningRound)

//            val actualGateName = gateToOutput.outputNameBy(expectedGate)
//            if (actualGateName == resultGateName) {
//                return
//            }

            val (expectedFullyInlined, expectedAllInlinedAsOutputs) = gateToOutput.inlineFully(expectedGate)
            if (AocDebug.enabled) println("  $resultGateName = $expectedFullyInlined")

            if (expectedFullyInlined == resultGateName) return

            if (AocDebug.enabled) {
                println()
                inliningRound.sortedWith(compareBy<String> { inlinedState[it]?.length ?: 0 }.thenComparingInt { it.length })
                    .forEach { name -> println("  $name <- ${outputToGate[name]}      ~ ${state[name]!!.toInt()}    ~ ${inlinedState[name]}") }
                println("  inliningOrder: ${inliningRound}")
            }

            TODO()

//            fun matchLevel(gate: CommutativeBinaryGate<*, *>) {
//                if (gate.a is String && gate.b is String) return
//            }
//
//            matchLevel(expectedGate)
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

            val actualZBit = (actualZ shr i) and 1
            val expectedZBit = sumBit

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

                if (actualZBit != expectedZBit) {
                    println()
                    println("   !!! actualZ = $actualZBit, expectedZ = $expectedZBit")
                }
            }

            val xPrevGateName = "x" + ((i - 1).toString().padStart(2, '0'))
            val yPrevGateName = "y" + ((i - 1).toString().padStart(2, '0'))

            if (i == 0) {
                // carry = 0
                // sum   = xBit xor yBit xor carry
                patternMatchOrFixAndReturnUsedCarryName(XorGate.of(xGateName, yGateName), resultGateName)

            } else if (i == 1) {
                // carry = (0 and (0 xor 0)) or (xPrevBit and yPrevBit) = (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = AndGate.of(xPrevGateName, yPrevGateName)
                patternMatchOrFixAndReturnUsedCarryName(XorGate.of(XorGate.of(xGateName, yGateName), carryGate), resultGateName)
                carryFromPrevName = gateToOutput.outputNameBy(carryGate)

            } else if (isLastCarryBit) {
                // no X and Y, just carry
                // sum   = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
                val carryGate = OrGate.of(
                    AndGate.of(carryFromPrevName!!, XorGate.of(xPrevGateName, yPrevGateName)),
                    AndGate.of(xPrevGateName, yPrevGateName)
                )
                patternMatchOrFixAndReturnUsedCarryName(carryGate, resultGateName)

            } else {
                // carry = (prevCarry and (xPrevBit xor yPrevBit)) or (xPrevBit and yPrevBit)
                // sum   = xBit xor yBit xor carry
                val carryGate = OrGate.of(
                    AndGate.of(carryFromPrevName!!, XorGate.of(xPrevGateName, yPrevGateName)),
                    AndGate.of(xPrevGateName, yPrevGateName)
                )
                patternMatchOrFixAndReturnUsedCarryName(XorGate.of(XorGate.of(xGateName, yGateName), carryGate), resultGateName)
                carryFromPrevName = gateToOutput.outputNameBy(carryGate)
            }

            if (expectedZBit != actualZBit) {
                val expectedZ = x + y
                val diffZ = actualZ xor expectedZ

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
        }

        return switchGates.sorted().joinToString(",")
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

    @Suppress("UNCHECKED_CAST")
    fun <A : GateInput, B : GateInput> Map<CommutativeBinaryGate<WireRef, WireRef>, String>.inlineDeepestLevel(
        gate: CommutativeBinaryGate<A, B>,
        inlinedAsOutputs: MutableMap<CommutativeBinaryGate<WireRef, WireRef>, String> = mutableMapOf()
    ): Pair<CommutativeBinaryGate<GateInput, GateInput>, MutableMap<CommutativeBinaryGate<WireRef, WireRef>, String>> {

        fun inlineGateRef(ref: GateRef<GateInput, GateInput>): GateInput =
            ref.gate.asWireInputsOrNull()?.let { wireGate -> this[wireGate]?.let { outputName -> GateInput.of(outputName).also { inlinedAsOutputs.put(wireGate, outputName) } } }
                ?: inlineDeepestLevel(ref.gate, inlinedAsOutputs).let { (inlined, _) -> GateInput.of(inlined) }

        val a: GateInput = gate.a.visit({ it }, { inlineGateRef(it) })
        val b: GateInput = gate.b.visit({ it }, { inlineGateRef(it) })

        return copy(gate, a, b) to inlinedAsOutputs
    }

    fun <A : GateInput, B : GateInput> Map<CommutativeBinaryGate<WireRef, WireRef>, String>.inlineFully(gate: CommutativeBinaryGate<A, B>): Pair<String?, MutableMap<CommutativeBinaryGate<WireRef, WireRef>, String>> {
        var allInlinedAsOutputs = mutableMapOf<CommutativeBinaryGate<WireRef, WireRef>, String>()

        var gateInlined = gate as CommutativeBinaryGate<GateInput, GateInput>
        var fullyInlined: String? = null
        if (AocDebug.enabled) println("\n  inlining: $gateInlined")
        while (true) {
            val (inlined, inlinedAsOutputs) = inlineDeepestLevel(gateInlined)
            gateInlined = inlined
            allInlinedAsOutputs.putAll(inlinedAsOutputs)
            if (AocDebug.enabled) {
                inlinedAsOutputs.forEach { (gate, name) -> println("    $name <- $gate") }
                println("  inlining: $gateInlined")
            }
            fullyInlined = gateInlined.asWireInputsOrNull()?.let { this[it] }
            if (inlinedAsOutputs.isEmpty() || fullyInlined != null) break
        }

        return fullyInlined to allInlinedAsOutputs
    }

    fun <A : GateInput, B : GateInput> Map<CommutativeBinaryGate<WireRef, WireRef>, String>.outputNameBy(gate: CommutativeBinaryGate<A, B>): String? {
        gate.asWireInputsOrNull()?.let { return this[it] }

        val a = gate.a.visit({ it }, { this.outputNameBy(it.gate)?.let { WireRef(it) } }) ?: return null
        val b = gate.b.visit({ it }, { this.outputNameBy(it.gate)?.let { WireRef(it) } }) ?: return null

        return copy(gate, a, b).let { flattened -> this[flattened] }
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
                    is GateRef<*, *> -> -1
                    is WireRef -> name.compareTo(other.name)
                }
            }

            override fun toString(): String = name

        }

        data class GateRef<A : GateInput, B : GateInput>(val gate: CommutativeBinaryGate<A, B>) : GateInput {

            override fun compareTo(other: GateInput): Int {
                return when (other) {
                    is GateRef<*, *> -> gate.compareTo(other.gate as CommutativeBinaryGate<GateInput, GateInput>)
                    is WireRef -> 1
                }
            }

            override fun toString(): String = gate.toString()

        }

        companion object {
            fun of(ref: Any): GateInput = when (ref) {
                is GateInput -> ref
                is String -> WireRef(ref)
                is CommutativeBinaryGate<*, *> -> GateRef(ref)
                else -> error("Cannot create GateInput from type ${ref.javaClass.name}")
            }

            fun of(ref: String): WireRef = WireRef(ref)

            fun <T : GateInput> of(ref: T): T = ref

            fun <A : GateInput, B : GateInput> of(ref: CommutativeBinaryGate<A, B>): GateRef<A, B> = GateRef(ref)
        }

    }

    @Suppress("UNCHECKED_CAST")
    fun <R> GateInput.visit(
        onWire: (WireRef) -> R,
        onGate: (GateRef<GateInput, GateInput>) -> R,
    ): R = when (this) {
        is WireRef -> onWire(this)
        is GateRef<*, *> -> onGate(this as GateRef<GateInput, GateInput>)
    }

    sealed interface CommutativeBinaryGate<A : GateInput, B : GateInput> : Comparable<CommutativeBinaryGate<GateInput, GateInput>> {
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

        override fun compareTo(other: CommutativeBinaryGate<GateInput, GateInput>): Int = compareValuesBy(this, other, { it.type }, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AndGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()

        override fun toString(): String = "($a $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <B : GateRef<GateInput, GateInput>> of(a: String, b: B) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateRef<GateInput, GateInput>> of(a: A, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput, C : GateInput, D : GateInput> of(a: CommutativeBinaryGate<A, B>, b: CommutativeBinaryGate<C, D>) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = AndGate(a, b)
        }

    }

    // OR gates output 1 if one or both inputs is 1; if both inputs are 0, these gates output 0.
    data class OrGate<A : GateInput, B : GateInput>(override val a: A, override val b: B) : CommutativeBinaryGate<A, B> {

        override val type = CommutativeBinaryGate.Type.OR

        override fun compareTo(other: CommutativeBinaryGate<GateInput, GateInput>): Int = compareValuesBy(this, other, { it.type }, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OrGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()

        override fun toString(): String = "($a  $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <B : GateRef<GateInput, GateInput>> of(a: String, b: B) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateRef<GateInput, GateInput>> of(a: A, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput, C : GateInput, D : GateInput> of(a: CommutativeBinaryGate<A, B>, b: CommutativeBinaryGate<C, D>) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = OrGate(a, b)
        }

    }

    // XOR gates output 1 if the inputs are different; if the inputs are the same, these gates output 0.
    data class XorGate<A : GateInput, B : GateInput>(override val a: A, override val b: B) : CommutativeBinaryGate<A, B> {

        override val type = CommutativeBinaryGate.Type.XOR

        override fun compareTo(other: CommutativeBinaryGate<GateInput, GateInput>): Int = compareValuesBy(this, other, { it.type }, { it.a }, { it.b })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is XorGate<A, B>) return false
            return a == other.a && b == other.b
        }

        override fun hashCode(): Int = ((31 * a.hashCode()) + b.hashCode()) * 31 + type.hashCode()

        override fun toString(): String = "($a $type $b)"

        companion object {
            fun of(a: String, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <B : GateRef<GateInput, GateInput>> of(a: String, b: B) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateRef<GateInput, GateInput>> of(a: A, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: String, b: CommutativeBinaryGate<A, B>) = of(GateInput.of(a), GateInput.of(b))
            fun <A : GateInput, B : GateInput> of(a: CommutativeBinaryGate<A, B>, b: String) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput, C : GateInput, D : GateInput> of(a: CommutativeBinaryGate<A, B>, b: CommutativeBinaryGate<C, D>) = of(GateInput.of(a), GateInput.of(b))

            fun <A : GateInput, B : GateInput> of(a: A, b: B) = XorGate(a, b)
        }

    }

    @Suppress("UNCHECKED_CAST")
    fun CommutativeBinaryGate<*, *>.asWireInputsOrNull(): CommutativeBinaryGate<WireRef, WireRef>? {
        return (if (a !is WireRef || b !is WireRef) null else this) as CommutativeBinaryGate<WireRef, WireRef>?
    }

    companion object {

        fun Boolean.toInt() = if (this) 1 else 0

        @Suppress("UNCHECKED_CAST")
        fun <T> asGateArg(any: Any): T where T : Comparable<T>, T : Any {
            return any as T
        }

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
