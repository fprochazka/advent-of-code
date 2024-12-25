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
