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

    fun matrixIndex(pos: Position): Int =
        matrixIndex(pos.x, pos.y)

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

    fun positionFor(x: Long, y: Long): Position =
        positionFor(matrixIndex(x, y))

    fun positionFor(x: Int, y: Int): Position =
        positionFor(x.toLong(), y.toLong())

    fun positionFor(x: String, y: String): Position =
        positionFor(x.toLong(), y.toLong())

    fun positionFor(index: Int): Position =
        positionsCache[index] ?: createPositionFromMatrixIndex(index).also {
            positionsCache[index] = it
        }

    private val positionsCache = Array<Position?>(area.toInt()) { null }

    private fun createPositionFromMatrixIndex(index: Int): Position =
        Position(index % w, index / w, areaDims = this)

    override fun toString(): String = "($w x $h)"

}

fun AreaDimensions?.positionFlyweight(x: Long, y: Long): Position =
    if (this != null && contains(x, y)) positionFor(x, y) else Position(x, y, this)
