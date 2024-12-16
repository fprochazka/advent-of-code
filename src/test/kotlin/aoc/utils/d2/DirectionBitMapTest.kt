package aoc.utils.d2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DirectionBitMapTest {

    @Test
    fun functional() {
        val bitmap = DirectionBitSet()

        Assertions.assertFalse { Direction.UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT in bitmap }
        Assertions.assertFalse { Direction.DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        bitmap.add(Direction.UP)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT in bitmap }
        Assertions.assertFalse { Direction.DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        bitmap.add(Direction.DOWN)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT in bitmap }
        Assertions.assertTrue { Direction.DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        bitmap.add(Direction.LEFT)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT in bitmap }
        Assertions.assertTrue { Direction.DOWN in bitmap }
        Assertions.assertTrue { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        bitmap.add(Direction.RIGHT)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertTrue { Direction.RIGHT in bitmap }
        Assertions.assertTrue { Direction.DOWN in bitmap }
        Assertions.assertTrue { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        bitmap.add(Direction.RIGHT_DOWN)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertTrue { Direction.RIGHT in bitmap }
        Assertions.assertTrue { Direction.DOWN in bitmap }
        Assertions.assertTrue { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertTrue { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        assertThat(bitmap.toSortedSet())
            .containsExactly(
                Direction.UP,
                Direction.RIGHT,
                Direction.RIGHT_DOWN,
                Direction.DOWN,
                Direction.LEFT,
            )

        bitmap.remove(Direction.DOWN)
        bitmap.remove(Direction.RIGHT)

        Assertions.assertTrue { Direction.UP in bitmap }
        Assertions.assertFalse { Direction.RIGHT in bitmap }
        Assertions.assertFalse { Direction.DOWN in bitmap }
        Assertions.assertTrue { Direction.LEFT in bitmap }
        Assertions.assertFalse { Direction.RIGHT_UP in bitmap }
        Assertions.assertTrue { Direction.RIGHT_DOWN in bitmap }
        Assertions.assertFalse { Direction.LEFT_UP in bitmap }
        Assertions.assertFalse { Direction.LEFT_DOWN in bitmap }

        assertThat(bitmap.toSortedSet())
            .containsExactly(
                Direction.UP,
                Direction.RIGHT_DOWN,
                Direction.LEFT,
            )
    }

}
