package aoc.utils.containers

data class Tuple3<T1 : Any, T2 : Any, T3 : Any>(val a: T1, val b: T2, val c: T3) {
    override fun toString(): String = "($a, $b, $c)"
}

data class Tuple4<T1 : Any, T2 : Any, T3 : Any, T4 : Any>(val a: T1, val b: T2, val c: T3, val d: T4) {
    override fun toString(): String = "($a, $b, $c, $d)"
}

data class Tuple5<T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any>(val a: T1, val b: T2, val c: T3, val d: T4, val e: T5) {
    override fun toString(): String = "($a, $b, $c, $d, $e)"
}
