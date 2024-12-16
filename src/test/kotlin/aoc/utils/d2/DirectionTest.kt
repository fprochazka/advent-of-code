package aoc.utils.d2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DirectionTest {

    @Test
    fun turnClockwiseFunctional() {
        assertThat(Direction.UP.turnClockwise()).isEqualTo(Direction.RIGHT_UP)
        assertThat(Direction.UP.turnClockwise(2)).isEqualTo(Direction.RIGHT)
        assertThat(Direction.RIGHT.turnClockwise()).isEqualTo(Direction.RIGHT_DOWN)
        assertThat(Direction.RIGHT.turnClockwise(2)).isEqualTo(Direction.DOWN)
        assertThat(Direction.DOWN.turnClockwise()).isEqualTo(Direction.LEFT_DOWN)
        assertThat(Direction.DOWN.turnClockwise(2)).isEqualTo(Direction.LEFT)
        assertThat(Direction.LEFT.turnClockwise()).isEqualTo(Direction.LEFT_UP)
        assertThat(Direction.LEFT.turnClockwise(2)).isEqualTo(Direction.UP)

        assertThat(Direction.RIGHT_UP.turnClockwise()).isEqualTo(Direction.RIGHT)
        assertThat(Direction.RIGHT_UP.turnClockwise(2)).isEqualTo(Direction.RIGHT_DOWN)
        assertThat(Direction.RIGHT_DOWN.turnClockwise()).isEqualTo(Direction.DOWN)
        assertThat(Direction.RIGHT_DOWN.turnClockwise(2)).isEqualTo(Direction.LEFT_DOWN)
        assertThat(Direction.LEFT_DOWN.turnClockwise()).isEqualTo(Direction.LEFT)
        assertThat(Direction.LEFT_DOWN.turnClockwise(2)).isEqualTo(Direction.LEFT_UP)
        assertThat(Direction.LEFT_UP.turnClockwise()).isEqualTo(Direction.UP)
        assertThat(Direction.LEFT_UP.turnClockwise(2)).isEqualTo(Direction.RIGHT_UP)
    }

    @Test
    fun turnCounterClockwiseFunctional() {
        assertThat(Direction.UP.turnCounterClockwise()).isEqualTo(Direction.LEFT_UP)
        assertThat(Direction.UP.turnCounterClockwise(2)).isEqualTo(Direction.LEFT)
        assertThat(Direction.RIGHT.turnCounterClockwise()).isEqualTo(Direction.RIGHT_UP)
        assertThat(Direction.RIGHT.turnCounterClockwise(2)).isEqualTo(Direction.UP)
        assertThat(Direction.DOWN.turnCounterClockwise()).isEqualTo(Direction.RIGHT_DOWN)
        assertThat(Direction.DOWN.turnCounterClockwise(2)).isEqualTo(Direction.RIGHT)
        assertThat(Direction.LEFT.turnCounterClockwise()).isEqualTo(Direction.LEFT_DOWN)
        assertThat(Direction.LEFT.turnCounterClockwise(2)).isEqualTo(Direction.DOWN)

        assertThat(Direction.RIGHT_UP.turnCounterClockwise()).isEqualTo(Direction.UP)
        assertThat(Direction.RIGHT_UP.turnCounterClockwise(2)).isEqualTo(Direction.LEFT_UP)
        assertThat(Direction.RIGHT_DOWN.turnCounterClockwise()).isEqualTo(Direction.RIGHT)
        assertThat(Direction.RIGHT_DOWN.turnCounterClockwise(2)).isEqualTo(Direction.RIGHT_UP)
        assertThat(Direction.LEFT_DOWN.turnCounterClockwise()).isEqualTo(Direction.DOWN)
        assertThat(Direction.LEFT_DOWN.turnCounterClockwise(2)).isEqualTo(Direction.RIGHT_DOWN)
        assertThat(Direction.LEFT_UP.turnCounterClockwise()).isEqualTo(Direction.LEFT)
        assertThat(Direction.LEFT_UP.turnCounterClockwise(2)).isEqualTo(Direction.LEFT_DOWN)
    }

    @Test
    fun turnFunctional() {
        assertThat(Direction.UP.turnRight90()).isEqualTo(Direction.RIGHT)
        assertThat(Direction.UP.turnRight45()).isEqualTo(Direction.RIGHT_UP)

        assertThat(Direction.UP.turnLeft90()).isEqualTo(Direction.LEFT)
        assertThat(Direction.UP.turnLeft45()).isEqualTo(Direction.LEFT_UP)
    }

}
