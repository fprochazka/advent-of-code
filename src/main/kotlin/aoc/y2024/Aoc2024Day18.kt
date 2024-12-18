package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.Dimensions
import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.strings.toLongs

fun Resource.day18(dims: Dimensions, simulateInitiallyCorrupted: Int): Day18 = Day18(
    Day18.parsePositions(nonBlankLines()),
    dims,
    simulateInitiallyCorrupted,
)

data class Day18(
    val corruptedBytes: List<Position>,
    val dims: Dimensions,
    val simulateInitiallyCorrupted: Int
) {

    val initiallyCorruptedMemory by lazy {
        simulateFallingBytes(simulateInitiallyCorrupted)
//            .also { println(it.toPlainMatrix()); println() }
    }

    val result1 by lazy {
        shortestPathTo(
            initiallyCorruptedMemory,
            dims.topLeft,
            dims.bottomRight
        )?.size ?: error("No path found")
    }

    val result2 by lazy {
        findFirstCorruptedByteThatCutsOffAllPaths(
            initiallyCorruptedMemory,
            corruptedBytes.drop(simulateInitiallyCorrupted),
            dims.topLeft,
            dims.bottomRight
        )?.let { p -> "${p.x},${p.y}" } ?: error("No such byte")
    }

    fun findFirstCorruptedByteThatCutsOffAllPaths(
        memory: MatrixGraph<Char>,
        moreCorruptedBytes: List<Position>,
        start: Position,
        end: Position
    ): Position? {
        fun corrupt(adr: Position) {
            memory[adr] = CORRUPTED_BYTE

            val corruptedNode = memory[adr]!!
            for (neighbourNodes in corruptedNode.connectedNodes) {
                neighbourNodes.weightedConnections.remove(corruptedNode.position)
            }
            corruptedNode.weightedConnections.clear()
        }

        var goodPath = shortestPathTo(memory, start, end)?.toSet() ?: error("No good path found")
        for (adr in moreCorruptedBytes) {
            corrupt(adr)

            if (adr in goodPath) {
                val otherPath = shortestPathTo(memory, start, end)?.toSet()
                if (otherPath == null) {
                    return adr
                } else {
                    goodPath = otherPath
                }
            }
        }

        return null
    }

    fun shortestPathTo(memory: MatrixGraph<Char>, start: Position, end: Position): List<Position>? =
        memory.anyShortestPathBfs(start, end)?.drop(1)

    fun simulateFallingBytes(rounds: Int): MatrixGraph<Char> {
        val memory = MatrixGraph.empty<Char>(dims, Direction.entriesCardinal)

        for (adr in memory.positions) {
            memory[adr] = EMPTY
        }

        for (adr in corruptedBytes.take(rounds)) {
            memory[adr] = CORRUPTED_BYTE
        }

        memory.updateAllConnections { a, b ->
            when {
                a.value == EMPTY && b.value == EMPTY -> true to 1
                else -> false to MatrixGraph.INFINITE_COST
            }
        }

        return memory
    }

    // starting at (0,0)
    companion object {

        const val EMPTY = '.'
        const val CORRUPTED_BYTE = '#'

        // list of distances
        fun parsePositions(lines: List<String>): List<Position> =
            lines
                .map { it.toLongs(limit = 2) }
                .map { (x, y) -> Position(x, y) }
    }

}
