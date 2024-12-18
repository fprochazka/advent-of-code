package aoc.utils.d2

import aoc.utils.Resource
import java.util.*

class MatrixGraph<V : Any>(dims: Dimensions, neighbourSides: Set<Direction>) {

    val neighbourSides = DirectionBitSet().apply {
        neighbourSides.forEach { add(it) }
    }

    val nodes: Matrix<Node> = Matrix.empty<Node>(dims)

    val positions: Sequence<Position>
        get() = nodes.dims.matrixPositions

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

    fun connectionsOf(pos: Position): List<Position> =
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
        for (neighbourPos in node.neighbourPositionsValid()) {
            this[neighbourPos.position]?.weightedConnections?.remove(node.position)
        }
        // remove all connections going out from this node
        node.weightedConnections.clear()
    }

    fun updateAllConnections(edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>) {
        for (position in positions) {
            val node = this[position]!!
            node.weightedConnections.clear()

            for (neighbourPosition in node.neighbourPositionsValid()) {
                val candidate = this[neighbourPosition.position]!!
                val (isConnected, weight) = edges(node, candidate)
                if (isConnected) {
                    node.weightedConnections[candidate.position] = weight
                }
            }
        }
    }

    fun toPlainMatrix(): Matrix<V> = let { graph ->
        Matrix.empty<V>(nodes.dims).also { copy ->
            for ((pos, node) in graph.nodes.entries) {
                copy[pos] = node.value
            }
        }
    }

    fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        nodes.allPositionsByValues { valueFilter(it.value) }
            .map { entry -> entry.key.value to entry.value }
            .toMap()

    fun anyShortestPathWeighted(
        start: Position,
        startDir: Direction,
        end: Position,
        edgeCost: (PathStep, Direction) -> Long = { cursor, inDir -> connectionWeight(cursor.pos, cursor.pos + inDir)!! },
    ): PathStep? {
        val queue = PriorityQueue<PathStep>(compareBy { it.pathCost }).apply {
            add(PathStep(start, startDir, 0))
        }

        val visited = mutableSetOf<Position>()

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()!!
            visited.add(currentStep.pos)

            if (currentStep.pos == end) {
                return currentStep
            }

            val neighbours = connectionsOf(currentStep.pos)
                .filter { it != currentStep.prev?.pos } // no 180 flips
                .filter { it !in visited }
                .map { currentStep.pos.relativeDirectionTo(it)!! to it }

            for ((neighbourDir, neighbourPos) in neighbours) {
                val nextStep = PathStep(
                    neighbourPos,
                    neighbourDir,
                    stepCost = edgeCost(currentStep, neighbourDir),
                    prev = currentStep
                )
                queue.add(nextStep)
            }
        }

        return null
    }

    inner class Node(
        var value: V,
        val position: Position,
    ) {

        /**
         * DO NOT MODIFY THIS DIRECTLY
         */
        val weightedConnections = mutableMapOf<Position, Long>()

        val connections
            get() = weightedConnections.keys.toList()

        val connectedNodes
            get() = weightedConnections.keys.mapNotNull { nodes[it] }

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
            val result = mutableSetOf<OrientedPosition>()

            for (neighbourPosition in neighbourPositionsIncludingOutOfMatrix()) {  // O(4)
                if (neighbourPosition.position !in weightedConnections) {
                    result.add(neighbourPosition)
                }
            }

            return result
        }

        fun vacantSidesValid(): Iterable<OrientedPosition> = // O(4)
            vacantSidesIncludingOutOfMatrix().filter { it.position in nodes }

        fun neighbourPositionsIncludingOutOfMatrix(): Iterable<OrientedPosition> = // O(4)
            neighbourSides.map { OrientedPosition(position + it, it) }

        fun neighbourPositionsValid(): Iterable<OrientedPosition> = // O(4)
            neighbourPositionsIncludingOutOfMatrix().filter { it.position in nodes }

        override fun toString(): String = "'$value' at $position"

    }

    data class PathStep(
        val pos: Position,
        val inDir: Direction,
        val stepCost: Long,
        val pathCost: Long,
        val prev: PathStep? = null
    ) : Comparable<PathStep> {

        constructor(pos: Position, inDir: Direction, stepCost: Long) : this(pos, inDir, stepCost, stepCost, prev = null)

        constructor(pos: Position, inDir: Direction, stepCost: Long, prev: PathStep) : this(
            pos,
            inDir,
            pathCost = prev.pathCost + stepCost,
            stepCost = stepCost,
            prev = prev
        )

        fun toList(): List<PathStep> =
            generateSequence(this) { it.prev }.toList().reversed()

        override fun compareTo(other: PathStep): Int =
            this.pathCost.compareTo(other.pathCost)

        override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

    }

    companion object {

        const val INFINITE_COST = (Long.MAX_VALUE / 2)

        fun <V : Any> empty(
            dims: Dimensions,
            neighbourSides: Set<Direction>,
        ): MatrixGraph<V> {
            return MatrixGraph<V>(dims, neighbourSides)
        }

        fun <V : Any> of(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            nodeValues: (Char) -> V,
            edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Boolean,
        ): MatrixGraph<V> =
            withWeights(matrix, neighbourSides, nodeValues) { node, neighbour ->
                when (edges(node, neighbour)) {
                    true -> true to 1L
                    false -> false to INFINITE_COST
                }
            }

        fun <V : Any> withWeights(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            nodeValues: (Char) -> V,
            edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>,
        ): MatrixGraph<V> {
            val result = MatrixGraph<V>(matrix.dims, neighbourSides)

            for ((position, value) in matrix.entries) {
                result[position] = nodeValues(value)
            }

            result.updateAllConnections(edges)

            return result
        }

    }

}
