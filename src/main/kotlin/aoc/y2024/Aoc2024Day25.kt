package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.matrix.Matrix

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

    class DoorLock(val pins: IntArray, val height: Int)

    class DoorKey(val heights: IntArray) {

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
                    val pins = IntArray(5)
                    var pinsIndex = 0
                    for (x in 0..dims.maxX) {
                        var pin = 0
                        for (y in 0..dims.maxY) {
                            if (matrix[dims.positionFor(x, y)] != '#') break
                            pin = maxOf(pin, y.toInt())
                        }
                        pins[pinsIndex++] = pin
                    }
                    locks.add(DoorLock(pins, dims.maxY.toInt()))

                } else { // key
                    val heights = IntArray(5)
                    var heightsIndex = 0
                    for (x in 0..dims.maxX) {
                        var height = 0
                        for (y in dims.maxY downTo 0) {
                            if (matrix[dims.positionFor(x, y)] != '#') break
                            height = maxOf(height, (dims.maxY - y).toInt())
                        }
                        heights[heightsIndex++] = height
                    }
                    keys.add(DoorKey(heights))
                }
            }

            return Day25(locks, keys)
        }
    }

}
