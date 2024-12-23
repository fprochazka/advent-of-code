package aoc.utils.d2

import aoc.utils.containers.BitSetK
import org.apache.commons.collections4.iterators.TransformIterator

class PositionBitSet(private val dims: AreaDimensions) : MutableSet<Position> {

    private val presence = BitSetK(dims.area.toInt())

    override fun add(element: Position): Boolean {
        require(dims.contains(element)) { "Position $element is not within dimensions $dims" }
        return presence.add(dims.matrixIndex(element))
    }

    override fun remove(element: Position): Boolean {
        return presence.remove(dims.matrixIndex(element))
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
        TransformIterator(presence.iterator()) { dims.positionFor(it) }

    override fun removeAll(elements: Collection<Position>): Boolean =
        throw UnsupportedOperationException()

    override fun retainAll(elements: Collection<Position>): Boolean =
        throw UnsupportedOperationException()

    override fun clear() =
        presence.clear()

    override val size: Int
        get() = presence.size

    override fun isEmpty(): Boolean =
        presence.isEmpty()

    override operator fun contains(element: Position): Boolean =
        element in dims && presence[dims.matrixIndex(element)]

    override fun containsAll(elements: Collection<Position>): Boolean =
        elements.all { contains(it) }

}
