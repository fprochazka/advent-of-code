package aoc.utils.d2

data class Distance(val xDiff: Long, val yDiff: Long) {

    constructor(xDiff: Int, yDiff: Int) : this(xDiff.toLong(), yDiff.toLong())

    constructor(xDiff: String, yDiff: String) : this(xDiff.toLong(), yDiff.toLong())

    operator fun times(length: Int): Distance =
        Distance(xDiff * length, yDiff * length)

    fun asDirection(): Direction? =
        toDirectionCache[this]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Distance) return false
        return (xDiff == other.xDiff) && (yDiff == other.yDiff)
    }

    override fun hashCode(): Int =
        31 * xDiff.hashCode() + yDiff.hashCode()

    override fun toString(): String = "(x=$xDiff, y=$yDiff)"

    companion object {

        private val toDirectionCache: Map<Distance, Direction> by lazy {
            Direction.entries.associateBy { it.vector }
        }

    }

}
