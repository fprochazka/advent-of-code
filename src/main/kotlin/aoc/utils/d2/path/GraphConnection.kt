package aoc.utils.d2.path

sealed interface GraphConnection {

    class None internal constructor() : GraphConnection

    data class Edge(val cost: Long) : GraphConnection {
        init {
            require(cost >= 0) { "Cost must be non-negative" }
        }
    }

    companion object {

        private val none = None()
        private val costOne = Edge(1)

        fun none(): GraphConnection = none

        fun edge(): GraphConnection = costOne

        fun edge(cost: Long): GraphConnection = when (cost) {
            1L -> costOne
            else -> Edge(cost)
        }

        fun edge(cost: Int): GraphConnection = edge(cost.toLong())

        fun edgeIf(condition: Boolean, cost: Long = 1): GraphConnection = if (condition) edge(cost) else none
        fun edgeIf(condition: () -> Boolean, cost: Long = 1): GraphConnection = edgeIf(condition())

    }

}
