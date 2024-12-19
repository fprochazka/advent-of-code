package aoc.utils.d2

data class AreaDimensions(val w: Long, val h: Long) {

    constructor(w: Int, h: Int) : this(w.toLong(), h.toLong())

    constructor(w: String, h: String) : this(w.toLong(), h.toLong())

    val maxX = (w - 1)
    val maxY = (h - 1)

    val area = w * h

    val topLeft: Position
        get() = Position(0, 0)

    val bottomRight: Position
        get() = Position(maxX, maxY)

    fun matrixIndex(position: Position): Int =
        Math.toIntExact((position.y * w) + position.x)

    fun matrixIndexToPosition(index: Int): Position =
        matrixIndexToPosition(index.toLong())

    fun matrixIndexToPosition(index: Long): Position =
        Position(index % w, index / w)

    val matrixIndices: Sequence<Long>
        get() = (0L until area).asSequence()

    val matrixPositions: Sequence<Position>
        get() = matrixIndices.map { matrixIndexToPosition(it) }

    fun contains(x: Long, y: Long): Boolean =
        x > -1 && y > -1 && x <= maxX && y <= maxY

    operator fun contains(pos: Position): Boolean =
        contains(pos.x, pos.y)

    override fun toString(): String = "($w x $h)"

}
