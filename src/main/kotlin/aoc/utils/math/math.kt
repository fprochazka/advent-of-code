package aoc.utils.math

import kotlin.math.absoluteValue

fun gcd(a: Int, b: Int): Long =
    gcd(a.toLong(), b.toLong())

fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b
    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }
    return x.absoluteValue
}

/**
 * Calculates the least non-negative remainder of `self` (mod `dim`).
 */
fun Long.remEuclid(size: Long): Long {
    require(size > 0) { "dim must be positive" }
    return ((this % size) + size) % size
}

/**
 * Calculates the least non-negative remainder of `self` (mod `dim`).
 */
fun Int.remEuclid(size: Int): Int =
    toLong().remEuclid(size.toLong()).toInt()
