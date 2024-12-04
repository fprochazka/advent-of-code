package aoc2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day03/example1.txt"))
    solve(Resource.named("aoc2024/day03/example2.txt"))
    solve(Resource.named("aoc2024/day03/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day3()

    input.assertResult("task1") { problem.mulInstructionEval }
    input.assertResult("task2") { problem.mulOrDoInstructionEval }
}

fun Resource.day3(): Day3 = Day3(
    content().replace("\\s+".toRegex(), "")
)

data class Day3(val memoryDump: String) {

    val mulInstructions by lazy {
        "mul\\(\\d+,\\d+\\)".toRegex()
            .findAll(memoryDump)
            .map { it.value }
            .map(::parseInstruction)
            .map { it as Instruction.Mul }
            .toList()
    }

    val mulInstructionEval by lazy {
        mulInstructions
            .sumOf { it.eval() }
    }

    val mulOrDoInstructions by lazy {
        "(mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\))".toRegex()
            .findAll(memoryDump)
            .map { it.value }
            .map(::parseInstruction)
            .toList()
    }

    val mulOrDoInstructionEval by lazy {
        var eval = 0
        var doIt = true
        for (instruction in mulOrDoInstructions) {
            when (instruction) {
                is Instruction.Mul -> if (doIt) {
                    eval += instruction.eval()
                }

                is Instruction.DoOrDont -> doIt = instruction.doIt
            }
        }

        return@lazy eval
    }

    fun parseInstruction(raw: String): Instruction = when {
        raw.startsWith("mul(") -> raw.replace("mul(", "").replace(")", "").split(",").let { (a, b) -> Instruction.Mul(a.toInt(), b.toInt()) }
        raw.startsWith("do(") -> Instruction.DoOrDont(true)
        raw.startsWith("don't(") -> Instruction.DoOrDont(false)
        else -> throw IllegalStateException("Unknown instruction: '$raw'")
    }

    sealed interface Instruction {
        data class Mul(val a: Int, val b: Int) : Instruction {
            fun eval() = a * b
        }

        data class DoOrDont(val doIt: Boolean) : Instruction
    }

}