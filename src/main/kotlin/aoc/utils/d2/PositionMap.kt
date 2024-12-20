package aoc.utils.d2

import java.util.*

class PositionMap<V : Any>(
    private val dims: AreaDimensions,
    private val valueClass: Class<V>,
) : MutableMap<Position, V> {


    private val yIndices = 0..(dims.maxY.toInt())
    private val xIndices = 0..(dims.maxX.toInt())

    private val w = dims.w.toInt()
    private val h = dims.h.toInt()

    // inner[Y][X] = V
    @Suppress("UNCHECKED_CAST")
    private val inner: Array<Array<V?>?> = java.lang.reflect.Array.newInstance(valueClass.arrayType(), h) as Array<Array<V?>?>

    private var innerSize = 0

    override fun clear() {
        for (y in yIndices) {
            val yValues = inner[y]
            if (yValues != null) {
                Arrays.fill(yValues, null)
            }
        }
        innerSize = 0
    }

    override fun put(key: Position, value: V): V? =
        put(key.x.toInt(), key.y.toInt(), value)

    fun put(x: Int, y: Int, value: V): V? {
        var yValues = inner[y]
        if (yValues == null) {
            @Suppress("UNCHECKED_CAST")
            yValues = java.lang.reflect.Array.newInstance(valueClass, w) as Array<V?>
            inner[y] = yValues
        }

        val previousVal = yValues[x]
        yValues[x] = value
        if (previousVal == null) innerSize++
        return previousVal
    }

    override fun putAll(from: Map<out Position, V>) {
        for ((key, value) in from.entries) {
            put(key, value)
        }
    }

    override fun remove(key: Position): V? =
        remove(key.x.toInt(), key.y.toInt())

    fun remove(x: Int, y: Int): V? {
        val yValues = inner[y]
        if (yValues == null) return null
        val previousVal = yValues[x]
        yValues[x] = null
        if (previousVal != null) innerSize--
        return previousVal
    }

    override val entries: EntrySet<V> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        EntrySet(this)
    }

    override val keys: KeySet by lazy(LazyThreadSafetyMode.PUBLICATION) {
        KeySet(this)
    }

    override val values: Values<V> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        Values(this)
    }

    private fun keysIterator(): MutableIterator<Position> = KeyItr(this)

    private fun valuesIterator(): MutableIterator<V> = ValueItr(this)

    private fun entriesIterator(): MutableIterator<MutableMap.MutableEntry<Position, V>> = EntryItr(this)

    override val size: Int get() = innerSize

    override fun isEmpty(): Boolean = innerSize == 0

    override fun containsKey(key: Position): Boolean = get(key) != null

    override fun containsValue(value: V): Boolean {
        for (y in yIndices) {
            for (x in xIndices) {
                inner[y]?.get(x)?.let { if (it == value) return true }
            }
        }
        return false
    }

    override fun get(key: Position): V? =
        get(key.x.toInt(), key.y.toInt())

    fun get(x: Int, y: Int): V? {
        val xValues = inner[y.toInt()]
        return if (xValues != null) xValues[x.toInt()] else null
    }

    fun copy(): PositionMap<V> =
        PositionMap<V>(dims, valueClass).also { copy ->
            for (y in yIndices) {
                val yValues = inner[y]
                if (yValues != null) {
                    copy.inner[y] = yValues.copyOf()
                }
            }
            copy.innerSize = innerSize
        }

    class Entry<V : Any>(
        val x: Int,
        val y: Int,
        private val backing: PositionMap<V>
    ) : MutableMap.MutableEntry<Position, V> {

        override fun setValue(newValue: V): V =
            backing.put(x, y, newValue)!!

        override val key: Position
            get() = backing.dims.positionFor(x, y)

        override val value: V
            get() = backing.get(x, y) ?: throw NoSuchElementException("No value at $key")
    }

    abstract class Itr<V : Any>(
        internal val backing: PositionMap<V>
    ) {

        internal var x = -1
        private val maxX = backing.dims.maxX.toInt()

        internal var y = 0
        private val maxY = backing.dims.maxY.toInt()

        internal var finished: Boolean = false // 0 = initial, 1 = continue, 2 = done

        init {
            initNext()
        }

        fun initNext() {
            while (y <= maxY && x <= maxX) {
                x++
                if (x > maxX) {
                    x = -1
                    y++
                    continue
                }

                val yValues = backing.inner[y]
                if (yValues == null) { // empty row
                    x = -1
                    y++
                    continue
                }

                val nextValue = yValues[x]
                if (nextValue != null) {
                    finished = false
                    return
                }
            }

            finished = true
        }

        fun hasNext(): Boolean = !finished

        fun remove() {
            backing.remove(position())
        }

        fun value(): V? = backing.get(x, y)

        fun position(): Position = backing.dims.positionFor(x, y)

        fun entry(): Entry<V> = Entry(x, y, backing)

    }

    class KeyItr<V : Any>(backing: PositionMap<V>) : Itr<V>(backing), MutableIterator<Position> {

        override fun next(): Position {
            if (!hasNext()) throw NoSuchElementException()
            return position().also { initNext() }
        }

    }

    class ValueItr<V : Any>(backing: PositionMap<V>) : Itr<V>(backing), MutableIterator<V> {

        override fun next(): V {
            if (!hasNext()) throw NoSuchElementException()
            return value().also { initNext() } ?: throw ConcurrentModificationException("Removed value")
        }

    }

    class EntryItr<V : Any>(backing: PositionMap<V>) : Itr<V>(backing), MutableIterator<MutableMap.MutableEntry<Position, V>> {

        override fun next(): MutableMap.MutableEntry<Position, V> {
            if (!hasNext()) throw NoSuchElementException()
            return entry().also { initNext() }
        }

    }

    class Values<V : Any>(private val backing: PositionMap<V>) : MutableCollection<V> {

        override fun add(element: V): Boolean = throw UnsupportedOperationException()

        override fun addAll(elements: Collection<V>): Boolean = throw UnsupportedOperationException()

        override fun iterator(): MutableIterator<V> = backing.valuesIterator()

        override fun remove(element: V): Boolean = throw UnsupportedOperationException()

        override fun removeAll(elements: Collection<V>): Boolean = throw UnsupportedOperationException()

        override fun retainAll(elements: Collection<V>): Boolean = throw UnsupportedOperationException()

        override fun clear(): Unit = backing.clear()

        override val size: Int get() = backing.size

        override fun isEmpty(): Boolean = backing.isEmpty()

        override fun contains(element: V): Boolean = throw UnsupportedOperationException()

        override fun containsAll(elements: Collection<V>): Boolean = throw UnsupportedOperationException()

    }

    class KeySet(private val backing: PositionMap<*>) : MutableSet<Position> {

        override fun iterator(): MutableIterator<Position> = backing.keysIterator()

        override fun add(element: Position): Boolean = throw UnsupportedOperationException()

        override fun remove(element: Position): Boolean = throw UnsupportedOperationException()

        override fun addAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

        override fun removeAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

        override fun retainAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

        override fun clear(): Unit = backing.clear()

        override val size: Int get() = backing.size

        override fun isEmpty(): Boolean = backing.isEmpty()

        override fun contains(element: Position): Boolean = backing.containsKey(element)

        override fun containsAll(elements: Collection<Position>): Boolean = throw UnsupportedOperationException()

    }

    class EntrySet<V : Any>(private val backing: PositionMap<V>) : MutableSet<MutableMap.MutableEntry<Position, V>> {

        override fun add(element: MutableMap.MutableEntry<Position, V>): Boolean = throw UnsupportedOperationException()

        override fun addAll(elements: Collection<MutableMap.MutableEntry<Position, V>>): Boolean = throw UnsupportedOperationException()

        override fun clear() = backing.clear()

        override fun iterator(): MutableIterator<MutableMap.MutableEntry<Position, V>> = backing.entriesIterator()

        override fun remove(element: MutableMap.MutableEntry<Position, V>): Boolean = throw UnsupportedOperationException()

        override fun removeAll(elements: Collection<MutableMap.MutableEntry<Position, V>>): Boolean = throw UnsupportedOperationException()

        override fun retainAll(elements: Collection<MutableMap.MutableEntry<Position, V>>): Boolean = throw UnsupportedOperationException()

        override val size: Int get() = backing.size

        override fun isEmpty(): Boolean = backing.isEmpty()

        override fun contains(element: MutableMap.MutableEntry<Position, V>): Boolean = backing.contains(element.key) && backing[element.key] == element.value

        override fun containsAll(elements: Collection<MutableMap.MutableEntry<Position, V>>): Boolean = throw UnsupportedOperationException()

    }

}

inline fun <reified V : Any> PositionMap(dims: AreaDimensions): PositionMap<V> =
    PositionMap(dims, V::class.java)
