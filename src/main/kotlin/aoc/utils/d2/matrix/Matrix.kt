package aoc.utils.d2.matrix

import aoc.utils.Resource
import aoc.utils.d2.AreaDimensions
import aoc.utils.d2.Direction
import aoc.utils.d2.Position
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.extension

open class Matrix<V : Any> protected constructor(
    val dims: AreaDimensions,
    private val valueClass: Class<V>,
) {

    @Suppress("UNCHECKED_CAST")
    private val matrix: Array<V?> = Math.toIntExact(dims.area).let { size ->
        when {
            !valueClass.isPrimitive -> java.lang.reflect.Array.newInstance(valueClass, size)
            valueClass.isPrimitiveOf<Char>() -> arrayOfNulls<Char?>(size)
            valueClass.isPrimitiveOf<Int>() -> arrayOfNulls<Int?>(size)
            valueClass.isPrimitiveOf<Long>() -> arrayOfNulls<Long?>(size)
            valueClass.isPrimitiveOf<Boolean>() -> arrayOfNulls<Boolean?>(size)
            valueClass.isPrimitiveOf<Double>() -> arrayOfNulls<Double?>(size)
            valueClass.isPrimitiveOf<Short>() -> arrayOfNulls<Short?>(size)
            valueClass.isPrimitiveOf<Byte>() -> arrayOfNulls<Byte?>(size)
            else -> TODO("not supported")
        } as Array<V?>
    }

    inline fun <reified R : Any> Class<V>.isPrimitiveOf() = this == R::class.javaPrimitiveType

    val positions: Sequence<Position>
        get() = dims.matrixPositions

    open fun allPositionsOfValue(value: V): Set<Position> =
        matrix.withIndex()
            .filter { it.value == value }
            .map { dims.positionFor(it.index) }
            .toSet()

    open fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        matrix.withIndex()
            .filter { (_, value) -> value?.let(valueFilter) == true }
            .groupBy({ it.value!! }, { it.index })
            .mapValues { (_, indices) -> indices.map { dims.positionFor(it) }.toSet() }

    val entries: Sequence<Pair<Position, V>>
        get() = positions.map { it to this[it]!! }

    fun entriesInDirection(startInclusive: Position, direction: Direction): Sequence<Pair<Position, V>> =
        generateSequence(startInclusive) { it + direction }
            .takeWhile { it in dims } // only for coordinates within matrix
            .map { it to this[it]!! }

    fun putAll(other: Matrix<V>) =
        other.entries.forEach { (position, value) -> this[position] = value }

    open operator fun set(position: Position, value: V) {
        matrix[dims.matrixIndex(position)] = value
    }

    operator fun get(position: Position): V? =
        if (position in dims) matrix[dims.matrixIndex(position)] else null

    fun getOrPut(position: Position, defaultValue: () -> V): V {
        val value = get(position)
        return value ?: defaultValue().also { set(position, it) }
    }

    fun getValue(position: Position): V =
        this[position] ?: error("$position has no associated value")

    fun getOrDefault(position: Position, default: V): V =
        this[position] ?: default

    fun copy(): Matrix<V> =
        Matrix<V>(dims, valueClass).also {
            this.matrix.copyInto(it.matrix)
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

    override fun toString(): String =
        positions
            .map { "${this[it]}" + (if (it.x == dims.maxX) "\n" else "") }
            .joinToString("")

    companion object {

        inline fun <reified V : Any> empty(dims: AreaDimensions): Matrix<V> =
            empty(dims, V::class.java)

        fun <V : Any> empty(dims: AreaDimensions, valueClass: Class<V>): Matrix<V> =
            Matrix<V>(dims, valueClass)

        inline fun <reified V : Any> of(dims: AreaDimensions, noinline initialValue: () -> V): Matrix<V> =
            of(dims, V::class.java, initialValue)

        fun <V : Any> of(dims: AreaDimensions, valueClass: Class<V>, initialValue: () -> V): Matrix<V> =
            Matrix<V>(dims, valueClass).apply {
                positions.forEach { this[it] = initialValue() }
            }

        fun ofChars(cells: Resource.CharMatrix2d): Matrix<Char> =
            of(cells, Char::class.java, { it })

        fun ofInts(cells: Resource.CharMatrix2d): Matrix<Int> =
            of(cells, Int::class.java, { it.digitToInt() })

        inline fun <reified V : Any> of(cells: Resource.CharMatrix2d, noinline toValue: (Char) -> V): Matrix<V> =
            of(cells, V::class.java, toValue)

        fun <V : Any> of(cells: Resource.CharMatrix2d, valueClass: Class<V>, toValue: (Char) -> V): Matrix<V> =
            Matrix<V>(cells.dims, valueClass).apply {
                cells.entries.forEach { entry -> this[entry.pos] = toValue(entry.value) }
            }

    }

}
