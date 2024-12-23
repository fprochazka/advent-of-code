package aoc.utils.containers

import java.util.*

class BitSetK(initBits: Int? = null) : MutableSet<Int> {

    private var presence: BitSet = if (initBits != null) BitSet(initBits) else BitSet()
    private var sizeCache = 0

    override fun iterator(): MutableIterator<Int> = presence.stream().iterator()

    override fun add(id: Int): Boolean {
        val hadPreviously = presence[id]
        presence[id] = true
        if (!hadPreviously) sizeCache += 1
        return !hadPreviously
    }

    override fun remove(id: Int): Boolean {
        val hadPreviously = presence[id]
        presence[id] = false
        if (hadPreviously) sizeCache -= 1
        return hadPreviously
    }

    operator fun set(id: Int, value: Boolean) {
        if (value) {
            add(id)
        } else {
            remove(id)
        }
    }

    operator fun get(id: Int): Boolean =
        presence[id]

    override operator fun contains(id: Int): Boolean =
        presence[id]

    override fun containsAll(elements: Collection<Int>): Boolean =
        elements.all { contains(it) }

    override val size: Int
        get() = sizeCache

    override fun isEmpty(): Boolean =
        sizeCache == 0

    override fun clear() {
        presence.clear()
        sizeCache = 0
    }

    override fun addAll(elements: Collection<Int>): Boolean {
        var modified = false;
        for (element in elements) {
            if (add(element)) {
                modified = true
            }
        }
        return modified
    }

    override fun removeAll(elements: Collection<Int>): Boolean {
        var modified = false;
        for (element in elements) {
            if (remove(element)) {
                modified = true
            }
        }
        return modified
    }

    override fun retainAll(elements: Collection<Int>): Boolean =
        throw UnsupportedOperationException()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BitSetK) return false
        return size == other.size && presence == other.presence
    }

    override fun hashCode(): Int =
        presence.hashCode()

    override fun toString(): String =
        joinToString(",", prefix = "[", postfix = "]")

}
