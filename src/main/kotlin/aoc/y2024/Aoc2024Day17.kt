package aoc.y2024

import aoc.utils.Resource
import aoc.utils.strings.toInts
import aoc.utils.strings.toLongs
import aoc.y2024.Day17.Debugger.*
import aoc.y2024.Day17.Debugger.ComboArgType.*
import aoc.y2024.Day17.InstructionOpcode.*
import kotlin.math.pow

fun Resource.day17(): Day17 = Day17(
    Day17.parseDebugger(nonBlankLines())
)

@Suppress("NOTHING_TO_INLINE")
data class Day17(val debugger: Debugger) {

    // run the given program, collecting any output produced by out instructions.
    // (Always join the values produced by out instructions with commas.)
    val result1 by lazy { debugger.copy().run().joinToString(",") }

    val result2 by lazy { fixCorruptedRegisterA() }

    fun fixCorruptedRegisterA(): Long {
        // 185639041000 ... (ops=128) [4, 3, 3, 0, 1, 3, 4, 1, 1, 2, 2, 3, 3, 1, 3, 2] (16)
        //                  expected: [2, 4, 1, 5, 7, 5, 1, 6, 0, 3, 4, 3, 5, 5, 3, 0] (16)
        //                  inst:     [bst 4, bxl 5, cdv 5, bxl 6, adv 3, bxc 3, out 5, jnz 0] (16)
        //	                           Bst(4) Bxl(5) Cdv(5) Bxl(6) Adv(3) Bxc    Out(5) Jnz(0)

        val rawInstructions = debugger.analyzeInstructions()
        // to what index can we jump from known jumps?
        val jumpsByTarget = rawInstructions.withIndex().filter { it.value is Jnz }.associate { (it.value as Jnz).rawArg to it.index }

        data class Branch(
            var halted: Boolean = true,
            var regA: Long = -1,
            var regB: Long = -1,
            var regC: Long = -1,
            val expectedOut: ArrayDeque<Int> = ArrayDeque(debugger.program),
            var reversePointer: Int = rawInstructions.size - 1,
        )

        fun validateSolution(solution: Branch): Branch? {
            val fixedOutput = debugger.copy(regA = solution.regA).run()
            if (listsAreEqual(fixedOutput, debugger.program)) {
                return solution
            }

            println("regA=${solution.regA} is not usable, because $fixedOutput != ${debugger.program}")
            return null
        }

        val naturalNumbers = 0L until Int.MAX_VALUE
        val reasonableExponentsOfTwo = 0L until 63

        /**
         * DFS search of possible solutions
         */
        fun solve(branch: Branch): Branch? {
            fun solveFor(values: Sequence<Long>, forkBranch: (Long) -> Branch): Branch? {
                for (value in values) {
                    solve(forkBranch(value))?.let {
                        return it
                    }
                }
                return null
            }

            fun solveFor(values: Iterable<Long>, forkBranch: (Long) -> Branch): Branch? =
                solveFor(values.asSequence(), forkBranch)

            fun solveForDeMod8(result: Long, forkBranch: (Long) -> Branch): Branch? =
                solveFor(naturalNumbers.asSequence().map { deMod8(result, moduloExp = it) }, forkBranch)

            fun solveForDeMod8(result: Int, forkBranch: (Long) -> Branch): Branch? =
                solveForDeMod8(result.toLong(), forkBranch)

            while (branch.reversePointer >= 0) {
                val instruction = rawInstructions[branch.reversePointer]
                when (instruction) {
                    // op_jnz -> if (regA != 0L) { pointer = literalArg.toInt() }
                    is Jnz -> {
                        if (branch.halted) {
                            branch.regA = 0
                        } else {
                            // noop, just walk left normally
                        }
                    }

                    // op_out -> output += (comboArg mod 8)
                    is Out -> {
                        val expectedOut = branch.expectedOut.removeLast()
                        val solution = when (ComboArgType.from(instruction.rawArg)) {
                            VAL_LITERAL -> {
                                // there is no unknown or forking, this branch is viable
                                // but we cannot deduce anything from it, because we were mod-ing a constant
                                null
                            }

                            // result = (comboArg mod 8) ... I know the left side, but the right side could have been any (0..inf)

                            // comboArg is regA where the register had value from (0..inf)
                            VAL_REG_A -> solveForDeMod8(expectedOut) {
                                branch.copy(regA = it, reversePointer = branch.reversePointer - 1)
                            }

                            // comboArg is regB where the register had value from (0..inf)
                            VAL_REG_B -> solveForDeMod8(expectedOut) {
                                branch.copy(regB = it, reversePointer = branch.reversePointer - 1)
                            }

                            // comboArg is regC where the register had value from (0..inf)
                            VAL_REG_C -> solveForDeMod8(expectedOut) {
                                branch.copy(regC = it, reversePointer = branch.reversePointer - 1)
                            }
                        }

                        if (solution != null) return solution
                    }

                    // op_adv -> regA = regA / exp(2, comboArg)
                    is Adv -> {
                        // restoring previous value of A
                        branch.regA = when (ComboArgType.from(instruction.rawArg)) {
                            VAL_LITERAL -> branch.regA * exp(2, instruction.rawArg)

                            VAL_REG_A -> if (branch.regA == -1L) {
                                return null // uninitialized
                            } else {
                                // newerRegA = prevRegA / exp(2, prevRegA)
                                TODO() // we have to search all naturalNumbers
                            }

                            VAL_REG_B -> if (branch.regB == -1L) {
                                return null // uninitialized
                            } else {
                                branch.regA * exp(2, branch.regB)
                            }

                            VAL_REG_C -> if (branch.regC == -1L) {
                                return null // uninitialized
                            } else {
                                branch.regA * exp(2, branch.regC)
                            }
                        }
                    }

                    // op_bdv -> regB = regA / exp(2, comboArg)
                    is Bdv -> {
                        // restoring previous value of B
                        val solution = when (ComboArgType.from(instruction.rawArg)) {
                            // B could have been any (0..inf)
                            VAL_LITERAL -> TODO()
                            VAL_REG_A -> TODO()
                            VAL_REG_C -> TODO()

                            // newerRegB = regA / exp(2, prevRegB)
                            VAL_REG_B -> if (branch.regB <= 0L || branch.regA == -1L) {
                                null // unsolvable
                            } else {
                                // I know the newerRegB and regA, I need to find prevRegB
                                // exp(2, prevRegB) = regA / newerRegB
                                solveFor(reasonableExponentsOfTwo.asSequence().filter { exp(2, it) == (branch.regA / branch.regB) }) {
                                    branch.copy(regB = it, reversePointer = branch.reversePointer - 1)
                                }
                            }
                        }

                        if (solution != null) return solution
                    }

                    // op_cdv -> regC = regA / exp(2, comboArg)
                    is Cdv -> {
                        // restoring previous value of C
                        val solution = when (ComboArgType.from(instruction.rawArg)) {
                            // C could have been any (0..inf)
                            VAL_LITERAL -> TODO()
                            VAL_REG_A -> TODO()
                            VAL_REG_B -> TODO()

                            // newerRegC = regA / exp(2, prevRegC)
                            VAL_REG_C -> if (branch.regC <= 0L || branch.regA == -1L) {
                                return null // unsolvable
                            } else {
                                // I know the newerRegC and regA, I need to find prevRegC
                                // exp(2, prevRegC) = regA / newerRegC
                                solveFor(reasonableExponentsOfTwo.asSequence().filter { exp(2, it) == (branch.regA / branch.regC) }) {
                                    branch.copy(regC = it, reversePointer = branch.reversePointer - 1)
                                }
                            }
                        }

                        if (solution != null) return solution
                    }

                    // op_bst -> regB = (comboArg mod 8)
                    is Bst -> {
                        if (branch.regB == -1L) {
                            return null // uninitialized
                        }

                        val solution = when (ComboArgType.from(instruction.rawArg)) {
                            // the left side could have been anything before the assignment
                            VAL_LITERAL -> solveFor(naturalNumbers) {
                                branch.copy(regB = it, reversePointer = branch.reversePointer - 1)
                            }

                            // comboArg is regA where the register had value from (0..inf)
                            VAL_REG_A -> solveForDeMod8(branch.regB) {
                                branch.copy(regA = it, reversePointer = branch.reversePointer - 1)
                            }

                            // result = (result mod 8) ... => result has to be (0..7)
                            VAL_REG_B -> solveFor(0L..7) {
                                branch.copy(regB = it, reversePointer = branch.reversePointer - 1)
                            }

                            // comboArg is regC where the register had value from (0..inf)
                            VAL_REG_C -> solveForDeMod8(branch.regB) {
                                branch.copy(regC = it, reversePointer = branch.reversePointer - 1)
                            }
                        }

                        if (solution != null) return solution
                    }

                    // op_bxl -> regB = regB xor literalArg
                    is Bxl -> {
                        if (branch.regB == -1L) {
                            return null // uninitialized
                        }
                        // XOR is naively reversible
                        branch.regB = branch.regB xor (instruction.rawArg.toLong())
                    }

                    // op_bxc -> regB = regB xor regC
                    is Bxc -> {
                        if (branch.regB == -1L || branch.regC == -1L) {
                            return null // uninitialized
                        }
                        // XOR is naively reversible
                        branch.regB = branch.regB xor branch.regC
                    }

                }

                branch.reversePointer -= 1
                branch.halted = false

                if (branch.reversePointer == 0 && branch.expectedOut.isEmpty()) {
                    validateSolution(branch)?.let { return it }
                }

                if (branch.reversePointer in jumpsByTarget) {
                    // TODO: this should be branched
                    branch.reversePointer = jumpsByTarget[branch.reversePointer]!!
                    continue
                }
            }

            return null;
        }

        return solve(Branch())?.regA ?: error("Failed to fix register A")
    }

    // This seems to be a 3-bit computer:
    // its program is a list of 3-bit numbers (0 through 7), like 0,1,2,3.
    // registers named A, B, and C, but these registers aren't limited to 3 bits and can instead hold any integer
    // eight instructions, each identified by a 3-bit number (called the instruction's opcode)
    // Each instruction also reads the 3-bit number after it as an input; this is called its operand.
    // instruction pointer identifies the position in the program from which the next opcode will be read; it starts at 0, pointing at the first 3-bit number in the program
    data class Debugger(
        var regA: Long,
        var regB: Long,
        var regC: Long,
        val program: List<Int>,
    ) {

        val debug: Boolean = System.getProperty("aoc.debugInstructions", "false").toBoolean()

        val output = mutableListOf<Int>()
        var executedInstructions = mutableListOf<Instruction>()

        var pointer = 0
        var operations = 0L

        fun analyzeInstructions(): List<Instruction> {
            val result = mutableListOf<Instruction>()

            for (instruction in instructions()) {
                result += instruction
                pointer += 2
            }

            pointer = 0

            return result
        }

        fun instructions(): Sequence<Instruction> = sequence {
            while (pointer < program.size) {
                val opcode = InstructionOpcode.entries[program[pointer]]
                operations++

                val instruction = when (opcode) {
                    op_adv -> Adv()
                    op_bxl -> Bxl()
                    op_bst -> Bst()
                    op_jnz -> Jnz()
                    op_bxc -> Bxc()
                    op_out -> Out()
                    op_bdv -> Bdv()
                    op_cdv -> Cdv()
                }

                yield(instruction)
            }
        }

        fun run(): List<Int> {
            for (instruction in instructions()) {
                if (debug) println("\nState{A=$regA, B=$regB, C=$regC, pointer: $pointer, ops: $operations, outSize: ${output.size}} \n\t" + (instruction.debug().replace(" =>", "\n\t=>")))

                executedInstructions += instruction

                when (instruction) {
                    is RegOpInstruction -> instruction.eval()
                    is JumpInstruction -> if (instruction.evalJump()) continue
                    is OutInstruction -> instruction.evalOut()
                }

                // Except for jump instructions,
                // the instruction pointer increases by 2 after each instruction is processed (to move past the instruction's opcode and its operand)
                // halts, when it tries to read opcode past the end of the program
                pointer += 2
            }

            if (debug) println("\nState{A=$regA, B=$regB, C=$regC, pointer: $pointer, ops: $operations, output: ${output}}")

            return output
        }

        fun runSimple(): List<Int> {
            while (pointer < program.size) {
                val opcode = InstructionOpcode.entries[program[pointer]]
                operations++
                when (opcode) {
                    op_adv -> regA = regA / exp(2, comboArg(rawArg()))
                    op_bdv -> regB = regA / exp(2, comboArg(rawArg()))
                    op_cdv -> regC = regA / exp(2, comboArg(rawArg()))
                    op_bxl -> regB = regB xor literalArg(rawArg())
                    op_bxc -> regB = regB xor regC
                    op_bst -> regB = mod8(comboArg(rawArg()))
                    op_out -> output += mod8(comboArg(rawArg())).toInt()
                    op_jnz -> if (regA != 0L) {
                        pointer = literalArg(rawArg()).toInt()
                        continue // do not inc the pointer by +2
                    }
                }

                // Except for jump instructions,
                // the instruction pointer increases by 2 after each instruction is processed (to move past the instruction's opcode and its operand)
                // halts, when it tries to read opcode past the end of the program
                pointer += 2
            }

            return output
        }

        inline fun rawArg(): Int = program[pointer + 1].toInt()

        // The value of a literal operand is the operand itself. For example, the value of the literal operand 7 is the number 7.
        inline fun literalArg(rawArg: Int): Long = rawArg.toLong()

        // The value of a combo operand can be found as follows:
        //    Combo operands 0 through 3 represent literal values 0 through 3.
        //    Combo operand 4 represents the value of register A.
        //    Combo operand 5 represents the value of register B.
        //    Combo operand 6 represents the value of register C.
        //    Combo operand 7 is reserved and will not appear in valid programs.
        fun comboArg(rawArg: Int): Long = when (ComboArgType.from(rawArg)) {
            VAL_LITERAL -> literalArg(rawArg)
            VAL_REG_A -> regA
            VAL_REG_B -> regB
            VAL_REG_C -> regC
        }

        enum class ComboArgType {
            VAL_LITERAL,
            VAL_REG_A,
            VAL_REG_B,
            VAL_REG_C,
            ;

            companion object {

                fun from(value: Long) = from(value.toInt())

                fun from(value: Int): ComboArgType = when (value) {
                    0, 1, 2, 3 -> VAL_LITERAL
                    4 -> VAL_REG_A
                    5 -> VAL_REG_B
                    6 -> VAL_REG_C
                    7 -> error("reserved combo operand 7")
                    else -> error("Invalid combo arg: $value")
                }

            }

        }

        sealed interface Instruction {
            var pos: Int
            fun debug(): String
        }

        sealed interface RegOpInstruction : Instruction {
            fun eval()
        }

        sealed interface OutInstruction : Instruction {
            fun evalOut()
        }

        sealed interface JumpInstruction : Instruction {
            fun evalJump(): Boolean
        }

        interface SingleArg {
            val rawArg: Int

            // The value of a combo operand can be found as follows:
            //    Combo operands 0 through 3 represent literal values 0 through 3.
            //    Combo operand 4 represents the value of register A.
            //    Combo operand 5 represents the value of register B.
            //    Combo operand 6 represents the value of register C.
            //    Combo operand 7 is reserved and will not appear in valid programs.
            fun comboArgDesc(): String = when (ComboArgType.from(rawArg)) {
                VAL_LITERAL -> rawArg.toString()
                VAL_REG_A -> "regA"
                VAL_REG_B -> "regB"
                VAL_REG_C -> "regC"
            }

        }

        inner class Adv() : RegOpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val numerator = regA
                val denominator = exp(2, comboArg(rawArg))
                val result = numerator / denominator
                return result
            }

            override fun eval() {
                regA = result()
            }

            override fun debug(): String = "adv(combo=${rawArg}) -> regA => expanded{regA / (2^combo(${rawArg()}))} => expanded{regA / (2^${comboArgDesc()})} => expanded{${regA} / (2^${comboArg(rawArg)})} => ${result()} -> regA"

            override fun toString(): String = "Adv($rawArg)"

        }

        inner class Bxl() : RegOpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val result = regB xor literalArg(rawArg)
                return result
            }

            override fun eval() {
                regB = result()
            }

            override fun debug(): String = "bxl(lit=${rawArg}) -> regB => expanded{regB xor lit} => expanded{${regB} xor ${literalArg(rawArg)}} => ${result()} -> regB"

            override fun toString(): String = "Bxl($rawArg)"

        }

        inner class Bst() : RegOpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val result = mod8(comboArg(rawArg))
                return result
            }

            override fun eval() {
                regB = result()
            }

            override fun debug(): String = "bst(combo=${rawArg}) -> regB => expanded{combo(${rawArg()}) % 8} => expanded{${comboArgDesc()} % 8} => expanded{${comboArg(rawArg)} % 8} => ${result()} -> regB"

            override fun toString(): String = "Bst($rawArg)"

        }

        inner class Jnz() : JumpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            override fun evalJump(): Boolean {
                if (regA != 0L) {
                    pointer = literalArg(rawArg).toInt()
                    return true
                }

                return false
            }

            override fun debug(): String = "jnz(lit=${rawArg}) => expanded{jumpTo(lit) if (regA != 0)} => expanded{jumpTo(${rawArg()}) if (${regA} != 0)} => ${if (regA != 0L) "jump to pointer ${literalArg(rawArg)}" else "noop"}"

            override fun toString(): String = "Jnz($rawArg)"

        }

        inner class Bxc() : RegOpInstruction {

            override var pos = pointer

            inline fun result(): Long {
                val result = regB xor regC
                return result
            }

            override fun eval() {
                regB = result()
            }

            override fun debug(): String = "bxc() -> regB => expanded{regB xor regC} => expanded{${regB} xor ${regC}} => ${result()} -> regB"

            override fun toString(): String = "Bxc()"

        }

        inner class Out() : OutInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val result = mod8(comboArg(rawArg))
                return result
            }

            override fun evalOut() {
                output += result().toInt()
            }

            override fun debug(): String = "out(combo=${rawArg}) -> out => expanded{${comboArgDesc()} % 8} => ${result()} -> out"

            override fun toString(): String = "Out($rawArg)"

        }

        inner class Bdv() : RegOpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val numerator = regA
                val denominator = exp(2, comboArg(rawArg))
                val result = numerator / denominator
                return result
            }

            override fun eval() {
                regB = result()
            }

            override fun debug(): String = "bdv(combo=${rawArg}) -> regB => expanded{regA / (2^combo(${rawArg()}))} => expanded{regA / (2^${comboArgDesc()})} => expanded{${regA} / (2^${comboArg(rawArg)})} => ${result()} -> regB"

            override fun toString(): String = "Bdv($rawArg)"

        }

        inner class Cdv() : RegOpInstruction, SingleArg {

            override var pos = pointer
            override val rawArg = rawArg()

            inline fun result(): Long {
                val numerator = regA
                val denominator = exp(2, comboArg(rawArg))
                val result = numerator / denominator
                return result
            }

            override fun eval() {
                regC = result()
            }

            override fun debug(): String = "cdv(combo=${rawArg}) -> regC => expanded{regA / (2^combo(${rawArg()}))} => expanded{regA / (2^${comboArgDesc()})} => expanded{${regA} / (2^${comboArg(rawArg)})} => ${result()} -> regC"

            override fun toString(): String = "Cdv($rawArg)"

        }

    }

    /**
     * `ordinal` == `opcode`
     */
    enum class InstructionOpcode() {
        /**
         * The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
         * The denominator is found by raising 2 to the power of the instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
         * The result of the division operation is truncated to an integer and then written to the A register.
         */
        op_adv,

        /**
         * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
         */
        op_bxl,

        /**
         * The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
         */
        op_bst,

        /**
         * The jnz instruction (opcode 3) does nothing if the A register is 0.
         * However,
         * if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand;
         * if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
         */
        op_jnz,

        /**
         * The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B
         * (For legacy reasons, this instruction reads an operand but ignores it.)
         */
        op_bxc,

        /**
         * The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value.
         * (If a program outputs multiple values, they are separated by commas.)
         */
        op_out,

        /**
         * The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register.
         * (The numerator is still read from the A register.)
         */
        op_bdv,

        /**
         * The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register.
         * (The numerator is still read from the A register.)
         */
        op_cdv;
    }

    companion object {

        fun deMod8(result: Long, moduloExp: Long = 0): Long = (8 * moduloExp + result).toLong()
        fun deMod8(result: Int, moduloExp: Long = 0): Long = deMod8(result.toLong(), moduloExp)

        fun exp(a: Long, b: Long): Long = a.toDouble().pow(b.toDouble()).toLong()
        fun exp(a: Long, b: Int): Long = exp(a, b.toLong())

        fun mod8(a: Long): Long = a.mod(8).toLong()

        fun listsAreEqual(actual: List<Int>, expected: List<Int>): Boolean {
            if (actual.size != expected.size) return false

            for ((index, expectedValue) in expected.withIndex()) {
                if (actual[index] != expectedValue) return false
            }

            return true
        }

        fun parseDebugger(lines: List<String>): Debugger {
            val a: Long = lines[0].split(":", limit = 2)[1].toLongs(1).single()
            val b: Long = lines[1].split(":", limit = 2)[1].toLongs(1).single()
            val c: Long = lines[2].split(":", limit = 2)[1].toLongs(1).single()
            val program = lines[3].split(":", limit = 2)[1].toInts()

            return Debugger(a, b, c, program)
        }

    }

}
