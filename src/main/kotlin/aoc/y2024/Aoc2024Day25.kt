package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Matrix

fun Resource.day25(): Day25 = Day25.parse(content())

data class Day25(
    val locks: List<DoorLock>,
    val keys: List<DoorKey>
) {

    val result1 by lazy { howManyUniqueLockKeyPairsFitTogetherWithoutOverlappingInAnyColumn() }

    fun howManyUniqueLockKeyPairsFitTogetherWithoutOverlappingInAnyColumn(): Long {
        var result = 0L
        for (lock in locks) {
            for (key in keys) {
                if (!key.overlapsWith(lock)) {
                    result += 1
                }
            }
        }
        return result
    }

    data class DoorLock(val pins: List<Int>, val height: Int)

    data class DoorKey(val heights: List<Int>) {

        fun overlapsWith(lock: DoorLock): Boolean {
            for ((i, h) in heights.withIndex()) {
                val p = lock.pins[i]
                if ((p + h) >= lock.height) {
                    return true
                }
            }
            return false
        }

    }

    companion object {
        fun parse(input: String): Day25 {
            val schemas = input.split("\n\n").map { Matrix.ofChars(Resource.CharMatrix2d.fromContent(it.trim())) }

            val keys = mutableListOf<DoorKey>()
            val locks = mutableListOf<DoorLock>()

            for (matrix in schemas) {
                val dims = matrix.dims
                if (matrix[dims.positionFor(0, 0)] == '#') { // lock
                    val pins = ArrayList<Int>(5)
                    for (x in 0..dims.maxX) {
                        var pin = 0
                        for (y in 0..dims.maxY) {
                            if (matrix[dims.positionFor(x, y)] != '#') break
                            pin = maxOf(pin, y.toInt())
                        }
                        pins.add(pin)
                    }
                    locks.add(DoorLock(pins, dims.maxY.toInt()))

                } else { // key
                    val heights = ArrayList<Int>(5)
                    for (x in 0..dims.maxX) {
                        var height = 0
                        for (y in dims.maxY downTo 0) {
                            if (matrix[dims.positionFor(x, y)] != '#') break
                            height = maxOf(height, (dims.maxY - y).toInt())
                        }
                        heights.add(height)
                    }
                    keys.add(DoorKey(heights))
                }
            }

            return Day25(locks, keys)
        }
    }

}
