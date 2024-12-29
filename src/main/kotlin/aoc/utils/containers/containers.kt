package aoc.utils.containers

fun <T> Collection<T>.headTail(): Pair<T, Sequence<T>> =
    first() to asSequence().drop(1)

fun <A, B> Pair<A, B>.reversed(): Pair<B, A> = second to first

fun <T> List<T>.middle(): T = this[this.size / 2]

operator fun <T> List<T>.plus(other: Pair<T, T>): List<T> = this + other.toList()

operator fun <T> Pair<T, T>.plus(other: List<T>): List<T> = this.toList() + other

fun <V> MutableList<V>.popFirst(): V = first().also { remove(it) }

fun <V> MutableSet<V>.popAny(): V = first().also { remove(it) }

fun <V> MutableCollection<V>.addAllNotNull(vararg values: V?) {
    for (value in values) {
        if (value != null) add(value)
    }
}

fun <V : Any> List<V>.isEqualTo(expected: List<V>): Boolean {
    if (this.size != expected.size) return false

    for ((index, expectedValue) in expected.withIndex()) {
        if (this[index] != expectedValue) return false
    }

    return true
}

fun <V> Collection<V>.allMinOf(selector: (V) -> Int): List<V> =
    this.groupBy { selector(it) }.entries.minBy { it.key }.value

fun Sequence<Long>.toLongArray(): LongArray = this.toList().toLongArray()

fun Sequence<Int>.toIntArray(): IntArray = this.toList().toIntArray()

fun Collection<Long>.toLongArray(): LongArray = LongArray(this.size).also { array ->
    this.forEachIndexed { i, value -> array[i] = value }
}

fun Collection<Int>.toIntArray(): IntArray = IntArray(this.size).also { array ->
    this.forEachIndexed { i, value -> array[i] = value }
}

fun <V : Any> Collection<V>.chunksCount(countOfResultingChunks: Int): List<List<V>> {
    if (size <= countOfResultingChunks) {
        return this.map { listOf(it) }
    }

    val chunkSize = size / countOfResultingChunks
    val remainder = size % countOfResultingChunks
    val lastIndex = countOfResultingChunks - 1

    val result = ArrayList<List<V>>(countOfResultingChunks)

    val iterator = this.iterator()
    for (i in 0 until countOfResultingChunks) {
        val currentChunkSize = chunkSize + if (i == lastIndex) remainder else 0
        val chunk = ArrayList<V>(currentChunkSize)

        repeat(currentChunkSize) {
            if (iterator.hasNext()) chunk.add(iterator.next())
        }

        result.add(chunk)
    }

    return result
}
