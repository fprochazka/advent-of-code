package aoc.y2024

import utils.Resource
import java.util.*

fun main() {
    solve(Resource.named("aoc2024/day09/example1.txt"))
    solve(Resource.named("aoc2024/day09/input.txt"))
    solve(Resource.named("aoc2024/day09/perf_test_1.txt"))
    solve(Resource.named("aoc2024/day09/perf_test_2.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day09()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day09(): Day09 = Day09(
    content().trim()
)

data class Day09(val diskMap: String) {

    val result1 by lazy {
        fragmentByFileSegments()
            .mapIndexed { position, fileId -> position * fileId }
            .sum()
    }

    val result2 by lazy {
        moveWholeFiles()
            .mapIndexed { position, fileId -> position * (fileId ?: 0) }
            .sum()
    }

    fun fragmentByFileSegments(): List<Long> = buildList {
        val layout = diskContentsByPosition()

        val fromStart = layout.indices.iterator()
        val fromEnd = layout.indices.reversed().iterator()

        var maxResultIndex = layout.size - 1
        for (i in fromStart) {
            if (i >= maxResultIndex) break

            if (layout[i] != null) {
                add(layout[i]!!)
                continue
            }

            for (j in fromEnd) {
                maxResultIndex = j
                if (layout[j] != null) {
                    add(layout[j]!!)
                    break
                }
            }
        }
    }

    fun diskContentsByPosition(): MutableList<Long?> {
        val layout = mutableListOf<Long?>()

        var fileId = 0L
        for ((isFile, blockSize) in diskMapBlocks()) {
            if (isFile) {
                repeat(blockSize) {
                    layout.add(fileId)
                }
                fileId++

            } else {
                repeat(blockSize) {
                    layout.add(null)
                }
            }
        }
        return layout
    }

    fun diskMapBlocks(): Sequence<DiskMapBlock> =
        diskMap.asSequence()
            .map { it.digitToInt() }
            .mapIndexed { index, value -> DiskMapBlock((index.mod(2) == 0), value) }

    data class DiskMapBlock(val isFile: Boolean, val blockSize: Int)

    fun moveWholeFiles(): List<Long?> {
        data class File(
            val id: Long,
            val size: Int,
            val originalPosition: Int,
        )

        class State(
            // [position => fileId]
            val result: MutableList<Long?>,
            // [fileId => file]
            val fileMetadataByMaxId: SortedMap<Long, File>,
        ) {

            val gaps: Sequence<IntRange>
                get() = sequence {
                    val iterator = result.indices.iterator()
                    for (gapStartIncl in iterator) {
                        if (result[gapStartIncl] != null) continue

                        var gapEndExcl = gapStartIncl + 1;
                        for (j in iterator) {
                            gapEndExcl = j
                            if (result[j] != null) break
                        }

                        yield(gapStartIncl until gapEndExcl)
                    }
                }

            // [size => [gaps]]
            val leftMostGapsIndex: MutableMap<Int, SortedMap<Int, IntRange>> by lazy {
                val result = mutableMapOf<Int, SortedMap<Int, IntRange>>()

                for (gap in gaps) {
                    for (size in gap.fittingSizes()) {
                        result
                            .getOrPut(size) { sortedMapOf(Comparator.naturalOrder<Int>()) }
                            .put(gap.start, gap)
                    }
                }

                return@lazy result
            }

            val filesToMove: Sequence<File> =
                fileMetadataByMaxId.values.asSequence()
                    .takeWhile { file -> (leftMostGap?.start ?: 0) < file.originalPosition }

            val leftMostGap: IntRange?
                get() = getLeftMostGapForSize(1)

            fun getLeftMostGapForSize(size: Int): IntRange? =
                leftMostGapsIndex[size]?.firstEntry()?.value

            fun tryMoveToGapClosestToDiskStart(file: File) {
                findGapForFile(file)?.let { gap ->
                    moveFileToGap(file, gap)
                }
            }

            fun findGapForFile(file: File): IntRange? =
                getLeftMostGapForSize(file.size)
                    ?.takeIf { it.start <= file.originalPosition }

            fun moveFileToGap(file: File, gap: IntRange) {
                require(file.size <= gap.length()) { "File must fit into the gap" }

                for (i in 0 until file.size) {
                    result[gap.start + i] = result[file.originalPosition + i]
                    result[file.originalPosition + i] = null
                }

                reindexGap(gap, file.size)
            }

            fun reindexGap(gap: IntRange, shorterBy: Int) {
                // remove the gap from the index
                for (size in gap.fittingSizes()) {
                    leftMostGapsIndex[size]!!.remove(gap.start)
                }

                val newStartIndex = gap.start + shorterBy
                val newGap = newStartIndex..gap.endInclusive

                // reindex the new gap
                for (size in newGap.fittingSizes()) {
                    leftMostGapsIndex[size]!!.put(newGap.start, newGap)
                }
            }

            override fun toString(): String =
                result
                    .mapIndexed { index, value -> "$index: " + (value?.toString() ?: ".") }
                    .joinToString("\n")

            fun IntRange.fittingSizes(): List<Int> = if (this.length() > 0) toList().indices.map { it + 1 } else emptyList()

        }

        val layout = mutableListOf<Long?>()
        val metadata = sortedMapOf<Long, File>(Comparator.naturalOrder<Long>().reversed())

        var fileId = 0L
        for ((isFile, blockSize) in diskMapBlocks()) {
            if (isFile) {
                metadata[fileId] = File(fileId, blockSize, layout.size)

                repeat(blockSize) {
                    layout.add(fileId)
                }
                fileId++

            } else {
                repeat(blockSize) {
                    layout.add(null)
                }
            }
        }

        val state = State(layout, metadata)
        state.filesToMove.forEach { file ->
            state.tryMoveToGapClosestToDiskStart(file)
        }

        return state.result
    }

    fun IntRange.length() = last - first + 1

    fun <T> Sequence<T>.withLongIndex(): Sequence<Pair<Long, T>> {
        return this.withIndex().map { (index, value) -> index.toLong() to value }
    }

}
