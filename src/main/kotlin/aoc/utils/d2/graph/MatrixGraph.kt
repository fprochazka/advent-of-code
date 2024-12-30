package aoc.utils.d2.graph

import aoc.utils.Resource
import aoc.utils.d2.*
import aoc.utils.d2.matrix.Matrix

class MatrixGraph<V : Any>(
    dims: AreaDimensions,
    neighbourSides: Set<Direction>,
    private val valueClass: Class<V>,
) {

    val neighbourSides = DirectionBitSet().apply {
        neighbourSides.forEach { add(it) }
    }

    val nodes: Matrix<Node> = Matrix.empty<Node>(dims)

    val dims: AreaDimensions
        get() = nodes.dims

    val positions: Sequence<Position>
        get() = dims.matrixPositions

    operator fun set(position: Position, value: V) {
        val existingNode = nodes[position]
        if (existingNode != null) {
            existingNode.value = value
        } else {
            nodes[position] = Node(value, position)
        }
    }

    operator fun get(position: Position): Node? =
        nodes[position]

    fun connectionsFrom(pos: Position): List<Position> =
        nodes[pos]?.connections ?: emptyList()

    fun connectionWeight(from: Position, to: Position): Long? =
        nodes[from]?.weightedConnections?.get(to)

    fun disconnectMutually(a: Position, b: Position) {
        disconnectMutually(nodes[a]!!, nodes[b]!!)
    }

    fun disconnectMutually(a: Node, b: Node) {
        a.weightedConnections.remove(b.position)
        b.weightedConnections.remove(a.position)
    }

    fun disconnectAll(pos: Position) {
        disconnectAll(nodes[pos]!!)
    }

    fun disconnectAll(node: Node) {
        // remove any connections pointing at the node
        for (neighbourPos in node.neighbourPositions) {
            this[neighbourPos]?.weightedConnections?.remove(node.position)
        }
        // remove all connections going out from this node
        node.weightedConnections.clear()
    }

    fun updateAllConnections(edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>) {
        for (position in positions) {
            val node = this[position]!!
            node.weightedConnections.clear()

            for (neighbourPos in node.neighbourPositions) {
                val candidate = this[neighbourPos]!!
                val (isConnected, weight) = edges(node, candidate)
                if (isConnected) {
                    node.weightedConnections[candidate.position] = weight
                }
            }
        }
    }

    fun toPlainMatrix(): Matrix<V> = let { graph ->
        Matrix.empty<V>(dims, valueClass).also { copy ->
            for ((pos, node) in graph.nodes.entries) {
                copy[pos] = node.value
            }
        }
    }

    fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        nodes.allPositionsByValues { valueFilter(it.value) }
            .map { entry -> entry.key.value to entry.value }
            .toMap()

    inner class Node(
        var value: V,
        val position: Position,
    ) {

        /**
         * DO NOT MODIFY THIS DIRECTLY
         */
        val weightedConnections: MutableMap<Position, Long> = HashMap<Position, Long>(neighbourSides.size, 1.0f)

        val connections
            get() = weightedConnections.keys.toList()

        val connectedNodes
            get() = weightedConnections.keys.mapNotNullTo(HashSet<Node>(neighbourSides.size, 1.0f)) { nodes[it] }

        fun disconnectAll() {
            disconnectAll(this)
        }

        fun removeConnectionTo(node: Node) {
            removeConnectionTo(node.position)
        }

        fun removeConnectionTo(pos: Position) {
            weightedConnections.remove(pos)
        }

        fun vacantSidesIncludingOutOfMatrix(): Iterable<OrientedPosition> { // O(4 + 4)
            val result = HashSet<OrientedPosition>(neighbourSides.size, 1.0f)

            for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix) {  // O(4)
                if (neighbourPosition.position !in weightedConnections) {
                    result.add(neighbourPosition)
                }
            }

            return result
        }

        /**
         * Neighbour means on matrix, doesn't mean the nodes are connected
         */
        val neighbourPositions: List<Position>
            get() = buildList(neighbourSides.size) {
                for (side in neighbourSides) {
                    (position + side).takeIf { it in dims }?.let { add(it) }
                }
            }

        /**
         * Neighbour means on matrix, doesn't mean the nodes are connected
         */
        val neighbourPositionsIncludingOutOfMatrix: List<OrientedPosition> by lazy(LazyThreadSafetyMode.NONE) {
            buildList(neighbourSides.size) {
                for (side in neighbourSides) {
                    add(OrientedPosition(position + side, side))
                }
            }
        }

        override fun toString(): String = "'$value' at $position"

    }

    companion object {

        const val INFINITE_COST = (Long.MAX_VALUE / 2)

        inline fun <reified V : Any> empty(
            dims: AreaDimensions,
            neighbourSides: Set<Direction>,
        ): MatrixGraph<V> {
            return empty(dims, neighbourSides, V::class.java)
        }

        fun <V : Any> empty(
            dims: AreaDimensions,
            neighbourSides: Set<Direction>,
            valueClass: Class<V>,
        ): MatrixGraph<V> {
            return MatrixGraph<V>(dims, neighbourSides, valueClass)
        }

        inline fun <reified V : Any> of(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            noinline nodeValues: (Char) -> V,
            noinline edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Boolean,
        ): MatrixGraph<V> =
            of(matrix, neighbourSides, V::class.java, nodeValues, edges)

        fun <V : Any> of(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            valueClass: Class<V>,
            nodeValues: (Char) -> V,
            edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Boolean,
        ): MatrixGraph<V> =
            withWeights(matrix, neighbourSides, valueClass, nodeValues) { node, neighbour ->
                when (edges(node, neighbour)) {
                    true -> true to 1L
                    false -> false to INFINITE_COST
                }
            }

        inline fun <reified V : Any> withWeights(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            noinline nodeValues: (Char) -> V,
            noinline edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>,
        ): MatrixGraph<V> =
            withWeights(matrix, neighbourSides, V::class.java, nodeValues, edges)

        fun <V : Any> withWeights(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            valueClass: Class<V>,
            nodeValues: (Char) -> V,
            edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>,
        ): MatrixGraph<V> {
            val result = MatrixGraph<V>(matrix.dims, neighbourSides, valueClass)

            for ((position, value) in matrix.entries) {
                result[position] = nodeValues(value)
            }

            result.updateAllConnections(edges)

            return result
        }

    }

}
