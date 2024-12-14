package aoc.utils.numbers

import aoc.utils.strings.half

fun Long.digitHalfs(): Pair<Long, Long> =
    toString().half()
        .let { (left, right) -> left.toLong() to right.toLong() }
