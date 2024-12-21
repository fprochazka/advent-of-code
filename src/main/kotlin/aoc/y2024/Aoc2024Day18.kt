package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.AreaDimensions
import aoc.utils.d2.Direction
import aoc.utils.d2.MatrixGraph
import aoc.utils.d2.Position
import aoc.utils.d2.graph.createDeadEndEliminator
import aoc.utils.d2.graph.anyShortest.anyShortestPathBfs
import aoc.utils.strings.toLongs

fun Resource.day18(): Day18 = day18Custom(
    dims = Day18.DEFAULT_DIMENSIONS,
    simulateInitiallyCorrupted = Day18.DEFAULT_SIMULATE_INITIALLY_CORRUPTED
)

fun Resource.day18Custom(dims: AreaDimensions, simulateInitiallyCorrupted: Int): Day18 = Day18(
    dims,
    Day18.parsePositions(nonBlankLines(), dims),
    simulateInitiallyCorrupted,
)

data class Day18(
    val dims: AreaDimensions,
    val corruptedBytes: List<Position>,
    val simulateInitiallyCorrupted: Int
) {

    val initiallyCorruptedMemory by lazy {
        initMemoryWithCorruptedBytes(simulateInitiallyCorrupted)
        // .also { println(it.toPlainMatrix()); println() }
    }

    val remainingCorruptedBytes by lazy {
        corruptedBytes.drop(simulateInitiallyCorrupted)
    }

    val result1 by lazy {
        initiallyCorruptedMemory.shortestPathToEnd()
            ?.size
            ?: error("No path found")
    }

    val result2 by lazy {
        findFirstCorruptedByteThatCutsOffAllPaths(initiallyCorruptedMemory, remainingCorruptedBytes)
            ?.let { p -> "${p.x},${p.y}" }
            ?: error("No such byte")
    }

    fun findFirstCorruptedByteThatCutsOffAllPaths(
        memory: MatrixGraph<Char>,
        moreCorruptedBytes: List<Position>
    ): Position? {
        val start = dims.topLeft
        val end = dims.bottomRight

        val deadEndEliminator = memory
            .createDeadEndEliminator(DEAD_END) { it.position != start && it.position != end }
            .runEliminationRound()

        fun corrupt(adr: Position) {
            deadEndEliminator.updateNodeAndMutuals(adr) {
                it.disconnectAll()
                it.value = CORRUPTED_BYTE
            }
        }

        var goodPath = memory.shortestPathToEnd() ?: error("No good path found")
        for (adr in moreCorruptedBytes) {
            corrupt(adr)

            if (adr in goodPath) {
                deadEndEliminator.runEliminationRound()

                val otherPath = memory.shortestPathToEnd()
                if (otherPath == null) {
                    return adr
                } else {
                    goodPath = otherPath
                }

                // memory.also { println(it.toPlainMatrix()); println() }
            }
        }

        return null
    }

    fun MatrixGraph<Char>.shortestPathToEnd(): Set<Position>? =
        anyShortestPathBfs(dims.topLeft, dims.bottomRight)?.toPositions()?.drop(1)?.toSet()

    fun initMemoryWithCorruptedBytes(rounds: Int): MatrixGraph<Char> {
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
        const val DEAD_END = 'x'

        // list of distances
        fun parsePositions(lines: List<String>, dims: AreaDimensions): List<Position> =
            lines
                .map { it.toLongs(limit = 2) }
                .map { (x, y) -> dims.positionFor(x, y) }

        val DEFAULT_DIMENSIONS = AreaDimensions(71, 71)
        const val DEFAULT_SIMULATE_INITIALLY_CORRUPTED = 1024

    }

}
