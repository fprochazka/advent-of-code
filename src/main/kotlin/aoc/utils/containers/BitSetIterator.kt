package aoc.utils.containers

import java.util.*

class BitSetIterator(private val bitSet: BitSet) : MutableIterator<Int> {

    private var nextValue = bitSet.nextSetBit(0)
    private var prevValue = -1

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        prevValue = nextValue
        nextValue = bitSet.nextSetBit(nextValue + 1)
        return prevValue
    }

    override fun hasNext(): Boolean =
        nextValue >= 0

    override fun remove() {
        if (prevValue < 0) throw NoSuchElementException()
        bitSet.clear(prevValue)
        prevValue = -1
    }

}
