package aoc.utils.math

import kotlin.math.absoluteValue
import kotlin.math.pow

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


// WHEN: result = (val mod 8)
// THEN: val = (8 * val + result) where val in (0..inf)
fun deMod(result: Long, mod: Int, i: Long = 0): Long =
    (mod * i + result).toLong()

fun deMod(result: Int, mod: Int, i: Long = 0): Long =
    deMod(result.toLong(), mod, i)

/**
 * a^b
 */
fun exp(a: Long, b: Int): Long = when {
    a == 2L -> pow2(b)
    else -> a.toDouble().pow(b.toDouble()).toLong()
}

/**
 * a^b
 */
fun exp(a: Long, b: Long): Long = exp(a, b.toInt())

/**
 * 2^n
 */
fun pow2(n: Int): Long = when {
    n == 0 -> 1L
    n > 0 -> 1L shl (n.toInt())
    else -> (2.0).pow(n).toLong()
}

/**
 * 2^n
 */
fun pow2(n: Long): Long = pow2(n.toInt())

fun Long.digitCount(): Int {
    val n = this.absoluteValue
    return when {
        n < 10L -> 1
        n < 100L -> 2
        n < 1_000L -> 3
        n < 10_000L -> 4
        n < 100_000L -> 5
        n < 1_000_000L -> 6
        n < 10_000_000L -> 7
        n < 100_000_000L -> 8
        n < 1_000_000_000L -> 9
        n < 10_000_000_000L -> 10
        n < 100_000_000_000L -> 11
        n < 1_000_000_000_000L -> 12
        else -> Math.log10(n.toDouble()).toInt() + 1 // Fallback for large numbers
    }
}

fun Long.scale10(n: Int): Long = this * (exp(10, n).toLong())
