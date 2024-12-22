package aoc.utils.containers

class CircularBufferFixedSize4<V : Any> {

    private var value0: V? = null
    private var value1: V? = null
    private var value2: V? = null
    private var value3: V? = null

    private var cursor = 0

    fun add(value: V) {
        when (cursor) {
            0 -> value0 = value
            1 -> value1 = value
            2 -> value2 = value
            3 -> value3 = value
        }
        cursor = (cursor + 1) % 4
    }

    // after we've inserted last number, the cursor resets to 0, so 0 is naively 1,2,3,4 and from there it shifts
    fun get(): Tuple4<V> = get(::Tuple4)

    // after we've inserted last number, the cursor resets to 0, so 0 is naively 1,2,3,4 and from there it shifts
    fun <R : Any> get(factory: (V, V, V, V) -> R): R = when (cursor) {
        0 -> factory(value0!!, value1!!, value2!!, value3!!)
        1 -> factory(value1!!, value2!!, value3!!, value0!!)
        2 -> factory(value2!!, value3!!, value0!!, value1!!)
        3 -> factory(value3!!, value0!!, value1!!, value2!!)
        else -> error("Invalid cursor")
    }

    fun value1(): V = when (cursor) {
        0 -> value0!!
        1 -> value1!!
        2 -> value2!!
        3 -> value3!!
        else -> error("Invalid cursor")
    }

    fun value2(): V = when (cursor) {
        0 -> value1!!
        1 -> value2!!
        2 -> value3!!
        3 -> value0!!
        else -> error("Invalid cursor")
    }

    fun value3(): V = when (cursor) {
        0 -> value2!!
        1 -> value3!!
        2 -> value0!!
        3 -> value1!!
        else -> error("Invalid cursor")
    }

    fun value4(): V = when (cursor) {
        0 -> value3!!
        1 -> value0!!
        2 -> value1!!
        3 -> value2!!
        else -> error("Invalid cursor")
    }

    override fun toString(): String = "(${value1()}, ${value2()}, ${value3()}, ${value4()}) at ${cursor + 1}nth"

}
