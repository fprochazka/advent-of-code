package aoc.utils.containers

import aoc.utils.ranges.length

class RangesCounter(size: Int, ranges: (Int) -> IntRange) {

    private val ranges = MutableList<IntRange>(size) { ranges(it) }
    private val counts = MutableList<Int>(size) { 0 }

    private var finished = false

    fun get(): List<Int> = counts

    fun hasNext(): Boolean = !finished

    fun next() {
        if (finished) throw NoSuchElementException("No more elements")
        increment(0)
    }

    private fun increment(index: Int) {
        if (index == counts.size) {
            // trying to overflow outside of counters size
            finished = true
            return
        }

        counts[index]++
        if (counts[index] !in ranges[index]) {
            counts[index] = 0
            increment(index + 1)
        }
    }

    override fun toString(): String =
        "("  + counts.joinToString(",") { it.toString() } + ") out of (" + ranges.joinToString(",") { it.length().toString() } + ")"

}
