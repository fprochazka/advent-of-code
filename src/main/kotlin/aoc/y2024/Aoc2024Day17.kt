package aoc.y2024

import aoc.utils.Resource
import aoc.utils.math.deMod
import aoc.utils.math.deModCandidates
import aoc.utils.math.pow2
import aoc.utils.strings.toInts
import aoc.utils.strings.toLongs
import aoc.y2024.Day17.Debugger.ComboArgType.*
import aoc.y2024.Day17.InstructionOpcode.*

fun Resource.day17(): Day17 = Day17(
    Day17.parseDebugger(nonBlankLines())
)

@Suppress("NOTHING_TO_INLINE")
data class Day17(val debugger: Debugger) {

    // run the given program, collecting any output produced by out instructions.
    // (Always join the values produced by out instructions with commas.)
    val result1 by lazy { debugger.copy().run().joinToString(",") }

    val result2 by lazy { findRegisterABasedOnProgramKnowledge() }

    fun findRegisterABasedOnProgramKnowledge(): Long {
        // 185639041000 ... (ops=128) [4, 3, 3, 0, 1, 3, 4, 1, 1, 2, 2, 3, 3, 1, 3, 2] (16)
        //                  expected: [2, 4, 1, 5, 7, 5, 1, 6, 0, 3, 4, 3, 5, 5, 3, 0] (16)

        // Input:                      Bst(4) Bxl(5) Cdv(5) Bxl(6) Adv(3) Bxc(3) Out(5) Jnz(0)
        // Example:                                                Adv(3)        Out(4) Jnz(0)

        // A / 2^B ... A >> B

        // B <- (A mod 8) xor 5      ... B in (0 .. 7)
        // C <- A >> B               ... C in (0 .. (A shr (0 .. 7)) )
        // B <- B xor 6
        // A <- A >> 3
        // B <- B xor C
        // OUT <- B mod 8            ... OUT in (0 .. 7)
        // IF A != 0 THEN JMP 0

        //    0 xor  5 =   5      ....     b00000000 xor b00000101 = b00000101
        //    1 xor  5 =   4      ....     b00000001 xor b00000101 = b00000100
        //    2 xor  5 =   7      ....     b00000010 xor b00000101 = b00000111
        //    3 xor  5 =   6      ....     b00000011 xor b00000101 = b00000110
        //    4 xor  5 =   1      ....     b00000100 xor b00000101 = b00000001
        //    5 xor  5 =   0      ....     b00000101 xor b00000101 = b00000000
        //    6 xor  5 =   3      ....     b00000110 xor b00000101 = b00000011
        //    7 xor  5 =   2      ....     b00000111 xor b00000101 = b00000010

        //    0 xor  6 =   6      ....     b00000000 xor b00000110 = b00000110
        //    1 xor  6 =   7      ....     b00000001 xor b00000110 = b00000111
        //    2 xor  6 =   4      ....     b00000010 xor b00000110 = b00000100
        //    3 xor  6 =   5      ....     b00000011 xor b00000110 = b00000101
        //    4 xor  6 =   2      ....     b00000100 xor b00000110 = b00000010
        //    5 xor  6 =   3      ....     b00000101 xor b00000110 = b00000011
        //    6 xor  6 =   0      ....     b00000110 xor b00000110 = b00000000
        //    7 xor  6 =   1      ....     b00000111 xor b00000110 = b00000001

        val program = debugger.program

        fun run(a: Long): List<Int> = debugger.copy(regA = a).run()

        fun solve(a: Long, len: Int): Long? {
            if (len > program.size) {
                return a
            }

            val expectedForLen = program.takeLast(len)

            for (i in 0L until 8) {
                val a2 = (a shl 3) or i
                val runOutput = run(a2)
                if (listsAreEqual(runOutput, expectedForLen)) {
                    val result = solve(a2, len + 1)
                    if (result != null) {
                        return result
                    }
                }
            }

            return null
        }

        return solve(0, 1) ?: error("Failed to find register A")
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
                    op_adv -> regA = regA / pow2(comboArg(rawArg()))
                    op_bdv -> regB = regA / pow2(comboArg(rawArg()))
                    op_cdv -> regC = regA / pow2(comboArg(rawArg()))
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
                val denominator = pow2(comboArg(rawArg))
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
                val denominator = pow2(comboArg(rawArg))
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
                val denominator = pow2(comboArg(rawArg))
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
         * The denominator is found by raising 2 to the power of the instruction's combo operand.
         * (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
         * The result of the division operation is truncated to an integer and then written to the A register.
         */
        op_adv,

        /**
         * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand,
         * then stores the result in register B.
         */
        op_bxl,

        /**
         * The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits),
         * then writes that value to the B register.
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

        val naturalNumbers = 0L until Int.MAX_VALUE
        val reasonableExponentsOfTwo = 0L until 63

        // WHEN: result = (val mod 8)
        // THEN: val = (8 * val + result) where val in (0..inf)
        fun deMod8(result: Long, i: Long = 0): Long = deMod(result, 8, i)
        fun deMod8(result: Int, i: Long = 0): Long = deMod8(result.toLong(), i)
        fun deMod8Candidates(result: Long): Sequence<Long> = deModCandidates(result, 8)
        fun deMod8Candidates(result: Int): Sequence<Long> = deMod8Candidates(result.toLong())

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
