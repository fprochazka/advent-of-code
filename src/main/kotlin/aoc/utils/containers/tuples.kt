package aoc.utils.containers

data class Tuple3<T : Any>(val a: T, val b: T, val c: T) {
    override fun toString(): String = "($a, $b, $c)"
}

data class Tuple4<T : Any>(val a: T, val b: T, val c: T, val d: T) {
    override fun toString(): String = "($a, $b, $c, $d)"
}

data class Tuple5<T : Any>(val a: T, val b: T, val c: T, val d: T, val f: T) {
    override fun toString(): String = "($a, $b, $c, $d, $f)"
}
