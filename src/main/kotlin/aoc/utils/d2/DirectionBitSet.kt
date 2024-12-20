package aoc.utils.d2

class DirectionBitSet : Iterable<Direction> {

    private var set = 0

    val size: Int
        get() = set.countOneBits()

    operator fun contains(dir: Direction): Boolean =
        set and maskOf(dir) != 0

    fun add(dir: Direction): Boolean {
        val dirMask = maskOf(dir)
        val contains = set and dirMask != 0
        set = set or dirMask
        return !contains
    }

    fun remove(dir: Direction) {
        set = set and maskOf(dir).inv()
    }

    override fun iterator(): Iterator<Direction> =
        Direction.entries.asSequence()
            .filter { it in this }
            .iterator()

    override fun toString(): String = toSet().toString()

    companion object {

        fun maskOf(dir: Direction): Int =
            1 shl dir.ordinal

    }

}
