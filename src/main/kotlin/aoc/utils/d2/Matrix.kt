package aoc.utils.d2

import aoc.utils.Resource
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.extension

open class Matrix<V : Any> protected constructor(
    val dims: Dimensions,
) {

    private val matrix: MutableList<V?> = MutableList(Math.toIntExact(dims.area)) { null }

    open fun allPositionsOfValue(value: V): Set<Position> =
        matrix.withIndex()
            .filter { it.value == value }
            .map { dims.matrixIndexToPosition(it.index) }
            .toSet()

    open fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        matrix.withIndex()
            .filter { (_, value) -> value?.let(valueFilter) == true }
            .groupBy({ it.value!! }, { it.index })
            .mapValues { (_, indices) -> indices.map { dims.matrixIndexToPosition(it) }.toSet() }

    val entries: Sequence<Pair<Position, V>>
        get() = positions.map { it to this[it]!! }

    fun entriesInDirection(startInclusive: Position, direction: Direction): Sequence<Pair<Position, V>> =
        generateSequence(startInclusive) { it + direction }
            .takeWhile { it in this } // only for coordinates within matrix
            .map { it to this[it]!! }

    val positions: Sequence<Position>
        get() = dims.matrixPositions

    fun putAll(other: Matrix<V>) =
        other.entries.forEach { (position, value) -> this[position] = value }

    open operator fun set(position: Position, value: V) {
        require(position in this) { "$position is not in matrix${dims}" }
        matrix[dims.matrixIndex(position)] = value
    }

    operator fun get(position: Position): V? =
        if (position in this) matrix[dims.matrixIndex(position)] else null

    fun getValue(position: Position): V =
        this[position] ?: error("$position has no associated value")

    fun getOrDefault(position: Position, default: V): V =
        this[position] ?: default

    operator fun contains(position: Position): Boolean =
        position.x > -1 && position.y > -1
          && position.x <= dims.maxX && position.y <= dims.maxY

    fun copy(): Matrix<V> =
        Matrix<V>(dims).also {
            it.matrix.clear()
            it.matrix.addAll(this.matrix)
        }

    fun draw(file: Path, valueToPixel: (V) -> Color) {
        ImageIO.write(draw(valueToPixel), file.extension.lowercase(), file.toFile())
    }

    fun draw(valueToPixel: (V) -> Color): BufferedImage {
        val image = BufferedImage(dims.w.toInt(), dims.h.toInt(), BufferedImage.TYPE_INT_ARGB)

        entries.forEach { (pos, value) ->
            image.setRGB(pos.x.toInt(), pos.y.toInt(), valueToPixel(value).rgb)
        }

        return image
    }

    fun withValuesIndex(): WithValuesIndex<V> =
        WithValuesIndex<V>(dims).apply { putAll(this@apply) }

    override fun toString(): String =
        positions
            .map { "${this[it]}" + (if (it.x == dims.maxX) "\n" else "") }
            .joinToString("")

    companion object {

        fun <V : Any> empty(dims: Dimensions): Matrix<V> =
            Matrix<V>(dims)

        fun <V : Any> of(dims: Dimensions, initialValue: () -> V): Matrix<V> =
            Matrix<V>(dims).apply {
                positions.forEach { this[it] = initialValue() }
            }

        fun ofChars(cells: Resource.CharMatrix2d): Matrix<Char> =
            of(cells, { it })

        fun ofInts(cells: Resource.CharMatrix2d): Matrix<Int> =
            of(cells, { it.digitToInt() })

        fun <V : Any> of(cells: Resource.CharMatrix2d, toValue: (Char) -> V): Matrix<V> =
            Matrix<V>(cells.dims).apply {
                cells.entries.forEach { (pos, char) -> this[pos] = toValue(char) }
            }

    }

    class WithValuesIndex<V : Any> internal constructor(dims: Dimensions) : Matrix<V>(dims) {

        private val positionsByValue: MutableMap<V, MutableSet<Position>> = mutableMapOf()

        val uniqueValues: Set<V>
            get() = positionsByValue.keys.toSet()

        override fun allPositionsOfValue(value: V): Set<Position> =
            positionsByValue[value] ?: emptySet()

        override fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
            positionsByValue.filterKeys(valueFilter)

        override fun set(position: Position, value: V) {
            val oldValue = this[position]

            super.set(position, value)

            positionsByValue[oldValue]?.remove(position)
            positionsByValue.getOrPut(value) { mutableSetOf() }.add(position)
        }
    }
}
