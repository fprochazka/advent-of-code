package aoc.utils.d2.graph

import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position

fun <V : Any> MatrixGraph<V>.createDeadEndEliminator(
    deadEndMarker: V,
    isPrunable: (MatrixGraph<V>.Node) -> Boolean
): DeadEndEliminator<V> =
    DeadEndEliminator(this, deadEndMarker, isPrunable)

class DeadEndEliminator<V : Any>(
    private val graph: MatrixGraph<V>,
    val deadEndMarker: V,
    val isPrunable: (MatrixGraph<V>.Node) -> Boolean,
) {

    private val nodesByConnections = mutableMapOf<Int, MutableSet<Position>>().apply {
        for (i in 1..graph.neighbourSides.size) {
            put(i, mutableSetOf())
        }

        for ((pos, node) in graph.nodes.entries) {
            if (node.weightedConnections.isNotEmpty()) {
                get(node.weightedConnections.size)!!.add(pos)
            }
        }
    }

    private fun updateCache(node: MatrixGraph<V>.Node, previousSize: Int) {
        val newSize = node.weightedConnections.size
        if (previousSize != newSize) {
            nodesByConnections[previousSize]!!.remove(node.position)
            if (newSize > 0) {
                nodesByConnections[newSize]!!.add(node.position)
            }
        }
    }

    fun runEliminationRound(): DeadEndEliminator<V> {
        var eliminated = 0

        val nodesWithOneConnection = nodesByConnections[1]!!
        while (nodesWithOneConnection.isNotEmpty()) {
            for (deadEndPos in nodesWithOneConnection.toMutableList()) {
                val deadEndNode = graph.nodes[deadEndPos]!!
                val previousNode = graph.nodes[deadEndNode.connections.first()]!!

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

    fun updateNodeAndMutuals(pos: Position, updater: (MatrixGraph<V>.Node) -> Unit) {
        updateNodeAndMutuals(graph.nodes[pos]!!, updater)
    }

    fun updateNodeAndMutuals(node: MatrixGraph<V>.Node, updater: (MatrixGraph<V>.Node) -> Unit) {
        val previousConnectionsWithSizes = node.neighbourPositions
            .mapNotNull { graph.nodes[it] }
            .map { it to it.weightedConnections.size }

        updateNode(node, updater)

        for ((node, previousSize) in previousConnectionsWithSizes) {
            updateCache(node, previousSize)
        }
    }

    fun updateNode(pos: Position, updater: (MatrixGraph<V>.Node) -> Unit) {
        updateNode(graph.nodes[pos]!!, updater)
    }

    fun updateNode(node: MatrixGraph<V>.Node, updater: (MatrixGraph<V>.Node) -> Unit) {
        // remove from cache
        val previousSize = node.weightedConnections.size

        // update
        updater(node)

        // update cache
        updateCache(node, previousSize)
    }

}
