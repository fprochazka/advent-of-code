package aoc.utils.ranges

fun IntRange.length() = last - first + 1

fun LongRange.length() = last - first + 1

fun IntRange.chunksCount(countOfResultingChunks: Int): List<IntRange> {
    val rangeLength = length()
    if (rangeLength <= countOfResultingChunks) {
        return this.map { it..it }
    }

    val chunkSize = rangeLength / countOfResultingChunks
    val remainder = rangeLength % countOfResultingChunks
    val lastIndex = countOfResultingChunks - 1
    return (0 until countOfResultingChunks).map {
        val start = it * chunkSize
        val end = start + chunkSize + (if (it == lastIndex) remainder else 0)
        return@map start until end
    }
}
