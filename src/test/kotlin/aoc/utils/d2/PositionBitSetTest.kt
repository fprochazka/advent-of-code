package aoc.utils.d2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionBitSetTest {

    @Test
    fun functional() {
        val positions = PositionBitSet(AreaDimensions(10, 11))

        assertThat(positions.size).isEqualTo(0)

        assertThat(positions.contains(Position(0, 0))).isFalse()
        assertThat(positions.contains(Position(5, 5))).isFalse()
        assertThat(positions.contains(Position(9, 10))).isFalse()
        assertThat(Position(9, 10) in positions).isFalse()

        assertThat(positions.remove(Position(5, 5))).isFalse()
        assertThat(positions.size).isEqualTo(0)

        assertThat(positions.toList()).isEmpty()

        assertThat(positions.add(Position(4, 4))).isTrue()
        assertThat(positions.add(Position(4, 4))).isFalse() // already contains

        positions.add(Position(0, 0))
        positions.add(Position(0, 10))
        positions.add(Position(5, 5))
        positions.add(Position(9, 10))

        assertThat(positions.toList()).containsOnly(
            Position(0, 0),
            Position(4, 4),
            Position(5, 5),
            Position(0, 10),
            Position(9, 10)
        )
        assertThat(positions.size).isEqualTo(5)

        assertThat(positions.remove(Position(0, 10))).isTrue()
        assertThat(positions.size).isEqualTo(4)

        positions.clear()
        assertThat(positions.size).isEqualTo(0)
        assertThat(positions.toList()).isEmpty()
    }

}
