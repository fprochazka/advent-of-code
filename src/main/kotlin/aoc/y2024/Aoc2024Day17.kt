package aoc.y2024

import aoc.utils.Resource
import aoc.utils.strings.toInts
import aoc.utils.strings.toLongs
import aoc.y2024.Day17.InstructionOpcode.*
import kotlin.math.pow

fun Resource.day17(): Day17 = Day17(
    Day17.parseDebugger(nonBlankLines())
)

data class Day17(val debugger: Debugger) {

    val result1 by lazy { debugger.copy().run().joinToString(",") }
    val result2 by lazy { findValueOfRegisterAThatMakesTheProgramOutputItself() }

    fun findValueOfRegisterAThatMakesTheProgramOutputItself(): Long {
        for (i in 1L..10_000_000L) {
            if (i % 10000L == 0L) println("Trying $i")

            val copy = debugger.copy(regA = i)
            val output = copy.run { output ->
                output.size > debugger.program.size
            }
            if (output.equals(debugger.program)) {
                return i
            }
        }

        throw IllegalStateException("NO RESULT")
    }

    // This seems to be a 3-bit computer: its program is a list of 3-bit numbers (0 through 7), like 0,1,2,3.
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

        // the program 0,1,2,3 would
        //     run the instruction whose opcode is 0 and pass it the operand 1,
        //     run the instruction having opcode 2 and pass it the operand 3,
        //     then halt.

        // Here are some examples of instruction operation:
        //    If register C contains 9, the program 2,6 would set register B to 1.
        //    If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
        //    If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
        //    If register B contains 29, the program 1,7 would set register B to 26.
        //    If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.

        // run the given program, collecting any output produced by out instructions.
        // (Always join the values produced by out instructions with commas.)

        fun run(haltPrematurely: (List<Long>) -> Boolean = { false }): List<Long> {
            val output = mutableListOf<Long>()

            fun exp(a: Long, b: Long): Long = a.toDouble().pow(b.toDouble()).toLong()

            var instructionPointer = 0
            while (instructionPointer < program.size && !haltPrematurely(output)) {

                // The value of a literal operand is the operand itself. For example, the value of the literal operand 7 is the number 7.
                fun literalArg(): Long = program[instructionPointer + 1].toLong()

                // The value of a combo operand can be found as follows:
                //    Combo operands 0 through 3 represent literal values 0 through 3.
                //    Combo operand 4 represents the value of register A.
                //    Combo operand 5 represents the value of register B.
                //    Combo operand 6 represents the value of register C.
                //    Combo operand 7 is reserved and will not appear in valid programs.
                fun comboArg(): Long = when (val arg = program[instructionPointer + 1]) {
                    0, 1, 2, 3 -> arg.toLong()
                    4 -> regA
                    5 -> regB
                    6 -> regC
                    7 -> error("reserved combo operand 7")
                    else -> throw IllegalArgumentException("Invalid combo operand: $arg")
                }

                val opcode = InstructionOpcode.entries[program[instructionPointer]]
                when (opcode) {
                    op_adv -> {
                        val arg = comboArg()
                        val numerator = regA
                        val denominator = exp(2, arg)
                        val result = numerator / denominator
                        regA = result
                    }

                    op_bxl -> {
                        val arg = literalArg()
                        val result = regB xor arg
                        regB = result
                    }

                    op_bst -> {
                        val arg = comboArg()
                        val result = arg.mod(8).toLong()
                        regB = result
                    }

                    op_jnz -> {
                        if (regA != 0L) {
                            val arg = literalArg()
                            instructionPointer = arg.toInt()
                            continue // do not inc the pointer by +2
                        }
                    }

                    op_bxc -> {
                        val result = regB xor regC
                        regB = result
                    }

                    op_out -> {
                        val arg = comboArg()
                        val result = arg.mod(8).toLong()
                        output += result
                    }

                    op_bdv -> {
                        val arg = comboArg()
                        val numerator = regA
                        val denominator = exp(2, arg)
                        val result = numerator / denominator
                        regB = result
                    }

                    op_cdv -> {
                        val arg = comboArg()
                        val numerator = regA
                        val denominator = exp(2, arg)
                        val result = numerator / denominator
                        regC = result
                    }
                }

                // Except for jump instructions,
                // the instruction pointer increases by 2 after each instruction is processed (to move past the instruction's opcode and its operand)
                // halts, when it tries to read opcode past the end of the program
                instructionPointer += 2
            }

            return output
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
        fun parseDebugger(lines: List<String>): Debugger {
            val a: Long = lines[0].split(":", limit = 2)[1].toLongs(1).single()
            val b: Long = lines[1].split(":", limit = 2)[1].toLongs(1).single()
            val c: Long = lines[2].split(":", limit = 2)[1].toLongs(1).single()
            val program = lines[3].split(":", limit = 2)[1].toInts()

            return Debugger(a, b, c, program)
        }

    }

}
