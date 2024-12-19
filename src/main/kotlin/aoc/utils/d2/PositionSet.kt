package aoc.utils.d2

class PositionSet(dims: AreaDimensions) : MutableSet<Position> {

    private val map = PositionMap<Any>(dims, Any::class.java)

    override fun iterator(): MutableIterator<Position> = map.keys.iterator()

    override fun add(element: Position): Boolean =
        map.put(element, PRESENT) == null

    override fun remove(element: Position): Boolean =
        map.remove(element) == PRESENT

    override fun addAll(elements: Collection<Position>): Boolean {
        var modified = false;
        for (e in elements) {
            if (add(e)) {
                modified = true
            }
        }
        return modified;
    }

    override fun removeAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

    override fun retainAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

    override fun clear() = map.clear()

    override val size: Int get() = map.size

    override fun isEmpty(): Boolean = map.isEmpty()

    override fun contains(element: Position): Boolean = map.containsKey(element)

    override fun containsAll(elements: Collection<Position>): Boolean = map.keys.containsAll(elements)

    companion object {

        private val PRESENT = Any()

    }

}

fun PositionSet(dims: AreaDimensions, from: Set<Position>): PositionSet {
    val set = PositionSet(dims)
    set.addAll(from)
    return set
}
