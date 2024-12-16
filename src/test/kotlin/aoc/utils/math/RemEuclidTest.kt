package aoc.utils.math

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemEuclidTest {

    @Test
    fun functional() {
        assertThat(0.remEuclid(10)).isEqualTo(0)
        assertThat(1.remEuclid(10)).isEqualTo(1)
        assertThat(2.remEuclid(10)).isEqualTo(2)
        assertThat(9.remEuclid(10)).isEqualTo(9)
        assertThat(10.remEuclid(10)).isEqualTo(0)
        assertThat(11.remEuclid(10)).isEqualTo(1)

        // value wraparound
        assertThat((-1).remEuclid(10)).isEqualTo(9)
        assertThat((-2).remEuclid(10)).isEqualTo(8)
        assertThat((-8).remEuclid(10)).isEqualTo(2)
        assertThat((-9).remEuclid(10)).isEqualTo(1)
        assertThat((-10).remEuclid(10)).isEqualTo(0)
        assertThat((-11).remEuclid(10)).isEqualTo(9)
    }

}
