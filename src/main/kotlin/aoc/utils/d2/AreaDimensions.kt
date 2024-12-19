package aoc.utils.d2

data class AreaDimensions(val w: Long, val h: Long) {

    constructor(w: Int, h: Int) : this(w.toLong(), h.toLong())

    constructor(w: String, h: String) : this(w.toLong(), h.toLong())

    val maxX = (w - 1)
    val maxY = (h - 1)

    val area = w * h

    val topLeft: Position
        get() = positionFor(x = 0, y = 0)

    val bottomRight: Position
        get() = positionFor(x = maxX, y = maxY)

    fun matrixIndex(position: Position): Int =
        matrixIndex(position.x, position.y)

    fun matrixIndex(x: Long, y: Long): Int =
        Math.toIntExact((y * w) + x)

    val matrixIndices: Sequence<Long>
        get() = (0L until area).asSequence()

    val matrixPositions: Sequence<Position>
        get() = (0 until area.toInt()).asSequence().map { positionFor(it) }

    fun contains(x: Long, y: Long): Boolean =
        x > -1 && y > -1 && x <= maxX && y <= maxY

    operator fun contains(pos: Position): Boolean =
        contains(pos.x, pos.y)

    fun positionFor(index: Long): Position =
        positionFor(index.toInt())

    fun positionFor(index: Int): Position =
        positionFor(index % w, index / w)

    fun positionFor(x: Long, y: Long): Position =
        Position(x, y)

    override fun toString(): String = "($w x $h)"

}
