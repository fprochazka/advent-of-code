package aoc.utils.d2

import java.util.*

class PositionBitSet(private val dims: AreaDimensions) : MutableSet<Position> {

    private val presence = BitSet(dims.area.toInt())
    private var present = 0

    override fun add(element: Position): Boolean {
        require(dims.contains(element)) { "Position $element is not within dimensions $dims" }
        val index = dims.matrixIndex(element)
        val hadPreviously = presence[index]
        presence[index] = true
        if (!hadPreviously) present++
        return !hadPreviously
    }

    override fun remove(element: Position): Boolean {
        val index = dims.matrixIndex(element)
        val hadPreviously = presence[index]
        presence[index] = false
        if (hadPreviously) present--
        return hadPreviously
    }

    override fun addAll(elements: Collection<Position>): Boolean {
        var modified = false;
        for (e in elements) {
            if (add(e)) {
                modified = true
            }
        }
        return modified;
    }

    override fun iterator(): MutableIterator<Position> =
        presence.stream().mapToObj { dims.positionFor(it) }.iterator()

    override fun removeAll(elements: Collection<Position>): Boolean =
        throw UnsupportedOperationException()

    override fun retainAll(elements: Collection<Position>): Boolean =
        throw UnsupportedOperationException()

    override fun clear() =
        presence.clear().also { present = 0 }

    override val size: Int
        get() = present

    override fun isEmpty(): Boolean =
        present > 0

    override operator fun contains(element: Position): Boolean =
        element in dims && presence[dims.matrixIndex(element)]

    override fun containsAll(elements: Collection<Position>): Boolean =
        elements.all { contains(it) }

}
