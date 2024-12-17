package aoc.y2024

import aoc.AocTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Aoc2024Day17Test : AocTest() {

    @BeforeEach
    fun debug() {
        System.setProperty("aoc.debugInstructions", "true")
    }

    @Test
    fun instructions1() {
        //    If register C contains 9, the program 2,6 would set register B to 1.

        val d = Day17.Debugger(0, 0, 9, listOf(2, 6))
        assertThat(d.run()).isEmpty()
        assertThat(d.regA).isEqualTo(0)
        assertThat(d.regB).isEqualTo(1)
        assertThat(d.regC).isEqualTo(9)
    }

    @Test
    fun instructions2() {
        //    If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.

        val d = Day17.Debugger(10, 0, 0, listOf(5, 0, 5, 1, 5, 4))
        assertThat(d.run()).containsExactly(0, 1, 2)
        assertThat(d.regA).isEqualTo(10)
        assertThat(d.regB).isEqualTo(0)
        assertThat(d.regC).isEqualTo(0)
    }

    @Test
    fun instructions3() {
        //    If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.

        val d = Day17.Debugger(2024, 0, 0, listOf(0, 1, 5, 4, 3, 0))
        assertThat(d.run()).containsExactly(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0)
        assertThat(d.regA).isEqualTo(0)
        assertThat(d.regB).isEqualTo(0)
        assertThat(d.regC).isEqualTo(0)
    }

    @Test
    fun instructions4() {
        //    If register B contains 29, the program 1,7 would set register B to 26.

        val d = Day17.Debugger(0, 29, 0, listOf(1, 7))
        assertThat(d.run()).isEmpty()
        assertThat(d.regA).isEqualTo(0)
        assertThat(d.regB).isEqualTo(26)
        assertThat(d.regC).isEqualTo(0)
    }

    @Test
    fun instructions5() {
        //    If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.

        val d = Day17.Debugger(0, 2024, 43690, listOf(4, 0))
        assertThat(d.run()).isEmpty()
        assertThat(d.regA).isEqualTo(0)
        assertThat(d.regB).isEqualTo(44354)
        assertThat(d.regC).isEqualTo(43690)
    }

    @Test
    fun instructions6() {
        // the program 0,1,2,3 would
        //     run the instruction whose opcode is 0 and pass it the operand 1,
        //     run the instruction having opcode 2 and pass it the operand 3,
        //     then halt.

        val d = Day17.Debugger(0, 0, 0, listOf(0, 1, 2, 3))
        assertThat(d.run()).isEmpty()
        assertThat(d.regA).isEqualTo(0)
        assertThat(d.regB).isEqualTo(3)
        assertThat(d.regC).isEqualTo(0)
    }

    @Test
    fun example1() {
        resource2024(17, "example1").let { input ->
            println("input: $input")
            input.assertResult("task1") { input.day17().result1 }
        }
    }

    @Test
    fun example2() {
        System.setProperty("aoc.debugInstructions", "false")

        resource2024(17, "example2").let { input ->
            println("input: $input")
            input.assertResult("task1") { input.day17().result2 }
        }
    }

    @Test
    fun input() {
        System.setProperty("aoc.debugInstructions", "false")

        resource2024(17, "input").let { input ->
            println("input: $input")

            val problem = input.day17()

//            input.assertResult("task1") { problem.result1 }
            input.assertResult("task2") { problem.result2 }
        }
    }

}
