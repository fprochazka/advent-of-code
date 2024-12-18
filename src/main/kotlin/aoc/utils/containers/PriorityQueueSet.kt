package aoc.utils.containers

import java.util.*

class PriorityQueueSet<V : Any>(comparator: Comparator<Entry<V>> = naturalOrder()) : Collection<V> {

    private val queue = PriorityQueue<Entry<V>>(comparator)
    private val valuesSet = HashSet<V>()

    operator fun set(value: V, priority: Long) {
        if (!valuesSet.add(value)) {
            // already contains with different priority
            queue.removeIf { it.value == value }
        }
        queue.add(Entry(value, priority))
    }

    fun pollFirstEntry(): Entry<V>? =
        queue.poll()
            .also { valuesSet.remove(it.value) }

    fun pollFirst(): V? =
        pollFirstEntry()?.value

    fun removeFirst(): V =
        pollFirst() ?: throw NoSuchElementException("No element available")

    val entries: Sequence<Entry<V>>
        get() = queue.asSequence()

    val values: Sequence<V>
        get() = entries.map { it.value }

    override fun iterator(): Iterator<V> =
        valuesSet.iterator()

    override val size: Int
        get() = valuesSet.size

    override fun isEmpty(): Boolean =
        valuesSet.isEmpty()

    override operator fun contains(value: V): Boolean = valuesSet.contains(value)

    override fun containsAll(elements: Collection<V>): Boolean =
        valuesSet.containsAll(elements)

    data class Entry<V>(val value: V, val priority: Long) : Comparable<Entry<V>> {

        override fun compareTo(other: Entry<V>): Int =
            this.priority.compareTo(other.priority)

    }

}
