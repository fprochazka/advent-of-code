package aoc.utils.d2

import aoc.utils.Resource
import aoc.utils.containers.PriorityQueueSet
import aoc.utils.containers.popAny
import java.util.*
import kotlin.collections.ArrayDeque

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

    fun anyShortestPathBfs(
        start: Position,
        end: Position,
    ): List<Position>? {
        val queue = ArrayDeque<Position>().apply { add(start) }
        val visited = mutableSetOf<Position>().apply { add(start) }

        val parents = mutableMapOf<Position, Position>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            visited.add(current)

            if (current == end) {
                val path = mutableListOf<Position>()
                var node: Position? = end
                while (node != null) {
                    path.add(node)
                    node = parents[node]
                }
                return path.reversed()
            }

            val neighbours = connectionsOf(current)
                .filter { it !in visited }

            for (neighbour in neighbours) {
                visited.add(neighbour)
                parents[neighbour] = current
                queue.add(neighbour)
            }
        }

        return null
    }

    fun anyShortestPathAStar(
        start: Position,
        end: Position,
    ): List<Position>? {
        fun reconstructPath(cameFrom: Map<Position, Position>, current: Position): List<Position> {
            val path = mutableListOf<Position>()
            var node: Position? = current
            while (node != null) {
                path.add(node)
                node = cameFrom[node]
            }
            return path.reversed()
        }

        // Parent map to reconstruct path
        val cameFrom = mutableMapOf<Position, Position>()

        val distanceFromStart = Matrix.empty<Long>(nodes.dims).also { it[start] = 0L }
        val estRemainingCost = Matrix.empty<Long>(nodes.dims).also { it[start] = start.manhattanDistanceTo(end) }
        val queue = PriorityQueueSet<Position>().also { it[start] = estRemainingCost.getValue(start) }

        while (queue.isNotEmpty()) {
            val currentPos = queue.removeFirst()
            if (currentPos == end) {
                return reconstructPath(cameFrom, currentPos)
            }

            for (neighborPos in connectionsOf(currentPos)) {
                val stepCost = distanceFromStart.getOrDefault(currentPos, INFINITE_COST) + (connectionWeight(currentPos, neighborPos) ?: 1)
                if (stepCost >= distanceFromStart.getOrDefault(neighborPos, INFINITE_COST)) {
                    continue
                }

                val estRemainingStepCost = stepCost + neighborPos.manhattanDistanceTo(end)

                // update distance and estimated cost
                distanceFromStart[neighborPos] = stepCost
                estRemainingCost[neighborPos] = estRemainingStepCost

                // Record the path
                cameFrom[neighborPos] = currentPos

                // Add to the open set if not already present
                if (neighborPos !in queue) {
                    queue[neighborPos] = estRemainingStepCost
                }
            }
        }

        return null
    }

    fun allShortestPaths(
        start: Position,
        startDir: Direction,
        end: Position,
        edgeCost: (PathStep, Direction) -> Long = { cursor, inDir -> connectionWeight(cursor.pos, cursor.pos + inDir)!! },
    ): Sequence<PathStep> = sequence {
        val minCosts = MinCostsMatrix()
        var shortestPathCost = INFINITE_COST

        val queue = PriorityQueue<PathStep>(compareBy { it.pathCost }).apply {
            add(PathStep(start, startDir, 0))
        }

        while (queue.isNotEmpty()) {
            val currentStep = queue.poll()

            if (currentStep.pathCost > shortestPathCost) {
                // the queue is sorted, therefore once it starts returning "too long" results we know we can throw away the rest
                break
            }

            if (currentStep.pathCost > minCosts[currentStep]) {
                // we've seen this path for cheaper
                continue
            }

            minCosts.update(currentStep)

            if (currentStep.pos == end) {
                shortestPathCost = minOf(shortestPathCost, currentStep.pathCost)
                yield(currentStep)
                continue
            }

            val neighbours = connectionsOf(currentStep.pos)
                .filter { it != currentStep.prev?.pos } // no 180 flips
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
    }

    inner class MinCostsMatrix() {

        private val minCosts = Matrix.empty<MutableMap<Direction, PathStep>>(nodes.dims)

        fun update(step: PathStep) {
            minCosts[step.pos] = (minCosts[step.pos] ?: mutableMapOf()).also {
                it.merge(step.inDir, step, { prev, next -> if (next.pathCost < prev.pathCost) next else prev })
            }
        }

        fun getPath(step: PathStep): PathStep? =
            minCosts[step.pos]?.get(step.inDir)

        operator fun get(step: PathStep): Long =
            getPath(step)?.pathCost ?: INFINITE_COST

    }

    fun createDeadEndEliminator(deadEndMarker: V, isPrunable: (Node) -> Boolean): DeadEndEliminator =
        DeadEndEliminator(deadEndMarker, isPrunable)

    inner class DeadEndEliminator(
        val deadEndMarker: V,
        val isPrunable: (Node) -> Boolean,
    ) {

        private val nodesByConnections = mutableMapOf<Int, MutableSet<Position>>().apply {
            for (i in 1..neighbourSides.size) {
                put(i, mutableSetOf())
            }

            for ((pos, node) in nodes.entries) {
                if (node.weightedConnections.isNotEmpty()) {
                    get(node.weightedConnections.size)!!.add(pos)
                }
            }
        }

        private fun updateCache(node: Node, previousSize: Int) {
            val newSize = node.weightedConnections.size
            if (previousSize != newSize) {
                nodesByConnections[previousSize]!!.remove(node.position)
                if (newSize > 0) {
                    nodesByConnections[newSize]!!.add(node.position)
                }
            }
        }

        fun runEliminationRound(): DeadEndEliminator {
            var eliminated = 0

            val nodesWithOneConnection = nodesByConnections[1]!!
            while (nodesWithOneConnection.isNotEmpty()) {
                for (deadEndPos in nodesWithOneConnection.toMutableList()) {
                    val deadEndNode = nodes[deadEndPos]!!
                    val previousNode = nodes[deadEndNode.connections.first()]!!

                    if (!isPrunable(deadEndNode)) {
                        nodesWithOneConnection.remove(deadEndPos)
                        continue
                    }

                    updateNode(deadEndNode) {
                        it.value = deadEndMarker
                        it.removeConnectionTo(previousNode)
                    }

                    updateNode(previousNode) {
                        it.removeConnectionTo(deadEndNode)
                    }

                    eliminated++
                }
            }

            // toPlainMatrix().let { println(it); println() }
            // println("Eliminated dead ends: $eliminated")

            return this
        }

        fun updateNodeAndMutuals(pos: Position, updater: (Node) -> Unit) {
            updateNodeAndMutuals(nodes[pos]!!, updater)
        }

        fun updateNodeAndMutuals(node: Node, updater: (Node) -> Unit) {
            val previousConnectionsWithSizes = node.neighbourPositionsValid()
                .mapNotNull { nodes[it.position] }
                .map { it to it.weightedConnections.size }

            updateNode(node, updater)

            for ((node, previousSize) in previousConnectionsWithSizes) {
                updateCache(node, previousSize)
            }
        }

        fun updateNode(pos: Position, updater: (Node) -> Unit) {
            updateNode(nodes[pos]!!, updater)
        }

        fun updateNode(node: Node, updater: (Node) -> Unit) {
            // remove from cache
            val previousSize = node.weightedConnections.size

            // update
            updater(node)

            // update cache
            updateCache(node, previousSize)
        }

    }

    fun groupToConnectedComponents(): List<Component> {
        val result = mutableListOf<Component>()

        val remainingPositions = nodes.positions.toMutableSet()
        while (remainingPositions.isNotEmpty()) {
            val connectedComponent = mutableSetOf<Position>()

            val neighboursQueue = ArrayDeque<Position>()
            neighboursQueue.add(remainingPositions.popAny())

            while (neighboursQueue.isNotEmpty()) {
                val position = neighboursQueue.removeFirst()

                remainingPositions.remove(position) // remove from candidates to find separate areas

                if (!connectedComponent.add(position)) {
                    continue // already visited
                }

                nodes[position]!!.connections
                    .filter { it !in connectedComponent }
                    .forEach { neighboursQueue.addLast(it) }
            }

            result.add(Component(connectedComponent))
        }

        return result
    }

    inner class Component(val positions: Set<Position>) {

        val nodes: List<Node>
            get() = positions.map { this@MatrixGraph.nodes[it]!! }

        val size: Int
            get() = positions.size

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
