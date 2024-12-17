package aoc.utils.math

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExpTest {

    @Test
    fun functional() {
        assertThat(exp(1, 0)).isEqualTo(1)
        assertThat(exp(1, 1)).isEqualTo(1)
        assertThat(exp(2, 0)).isEqualTo(1)
        assertThat(exp(2, 1)).isEqualTo(2)
        assertThat(exp(2, 2)).isEqualTo(4)
        assertThat(exp(2, 3)).isEqualTo(8)
        assertThat(exp(2, 4)).isEqualTo(16)
        assertThat(exp(3, 2)).isEqualTo(9)
    }

}
