package aoc.utils.math

import org.junit.jupiter.api.Test

@OptIn(ExperimentalStdlibApi::class)
class ExperimentsTest {

    @Test
    fun experimentsShr() {
        for (i in 1..1000) {
            for (j in 1..6) {
                val r1 = i / pow2(j)
                val r2 = i shr j

                val s1 = "${i.toString().padStart(4)} / 2^$j = ${r1.toString().padStart(3)}"
                val s2 = "${i.toString().padStart(4)} shr $j = ${r2.toString().padStart(3)}"

                val s3 = "${i.toHexString().padStart(12)} shr ${j.toString().padStart(2)} = ${r2.toHexString().padStart(12)}"
                val s4 = "b${i.toString(2).padStart(8, '0')} shr ${j.toString().padStart(2)} = b${r2.toString(2).padStart(8, '0')}"

                println("${s1}      ....   ${s2}      ....   ${s3}      ....   ${s4}      ....   ${if (r1 == r2.toLong()) "OK" else "FAIL"}")
            }
        }
    }

    @Test
    fun experimentsXor() {
        for (j in 1..10) {
            for (i in 0..1000) {
                val r1 = i xor j

                val s1 = "${i.toString().padStart(4)} xor ${j.toString().padStart(2)} = ${r1.toString().padStart(3)}"

                val s2 = "b${i.toString(2).padStart(8, '0')} xor b${j.toString(2).padStart(8, '0')} = b${r1.toString(2).padStart(8, '0')}"

                println("${s1}      ....     ${s2}")
            }
        }
    }

}
