package aoc

import aoc.utils.AocDebug
import aoc.utils.Resource
import org.junit.jupiter.api.BeforeEach

open class AocTest {

    @BeforeEach
    fun debug() {
        AocDebug.enabled = true
    }

    fun resource2024(day: Int, name: String): Resource =
        Resource.named("aoc2024/day${day.toString().padStart(2, '0')}/$name.txt")

}
