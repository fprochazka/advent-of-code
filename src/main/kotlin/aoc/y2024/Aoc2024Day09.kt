package aoc.y2024

import aoc.utils.Resource
import aoc.utils.ranges.indices
import aoc.utils.ranges.length
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
import it.unimi.dsi.fastutil.longs.Long2ObjectRBTreeMap

fun Resource.day09(): Day09 = Day09.parse(content().trim())

class Day09(val diskMap: IntArray) {

    val result1 by lazy {
        fragmentByFileSegments()
            .mapIndexed { position, fileId -> position.toLong() * fileId }
            .sum()
    }

    val result2 by lazy {
        optimizeByMovingWholeFiles()
            .mapIndexed { position, fileId -> position.toLong() * maxOf(0, fileId) }
            .sum()
    }

    val diskSize by lazy(LazyThreadSafetyMode.PUBLICATION) { diskMap.sum() }

    fun diskMapBlocks(): Sequence<DiskMapBlock> =
        diskMap.asSequence().mapIndexed { index, value -> DiskMapBlock((index.mod(2) == 0), value) }

    data class DiskMapBlock(val isFile: Boolean, val blockSize: Int)

    fun fragmentByFileSegments() =
        FileSegmentsCompactingOptimizer(diskSize, diskMapBlocks()).optimize()

    fun optimizeByMovingWholeFiles() =
        WholeFilesMovingOptimizer(diskSize, diskMapBlocks()).optimize()

    class FileSegmentsCompactingOptimizer(diskSize: Int, diskMapBlocks: Sequence<DiskMapBlock>) {

        // [position => fileId]
        val layout = IntArray(diskSize)
        var usedSpace = 0

        init {
            var layoutIndex = 0
            var fileId = 0
            for ((isFile, blockSize) in diskMapBlocks) {
                if (isFile) {
                    repeat(blockSize) {
                        layout[layoutIndex++] = fileId
                    }
                    fileId++
                    usedSpace += blockSize

                } else {
                    repeat(blockSize) {
                        layout[layoutIndex++] = -1
                    }
                }
            }
        }

        fun optimize(): IntArray {
            val result = IntArray(usedSpace + 1)
            var resultIndex = 0

            val fromStart = layout.indices.iterator()
            val fromEnd = layout.indices.reversed().iterator()

            var maxResultIndex = layout.size - 1
            for (i in fromStart) {
                if (i >= maxResultIndex) break

                if (layout[i] >= 0) {
                    result[resultIndex++] = layout[i]
                    continue
                }

                for (j in fromEnd) {
                    maxResultIndex = j
                    if (layout[j] >= 0) {
                        result[resultIndex++] = layout[j]
                        break
                    }
                }
            }

            return result
        }

    }

    class WholeFilesMovingOptimizer(diskSize: Int, diskMapBlocks: Sequence<DiskMapBlock>) {

        // [position => fileId]
        val layout = LongArray(diskSize)

        // [fileId => file]
        val fileMetadataByMaxId = Long2ObjectRBTreeMap<File>(Comparator.naturalOrder<Long>().reversed())

        init {
            var layoutIndex = 0
            var fileId = 0L
            for ((isFile, blockSize) in diskMapBlocks) {
                if (isFile) {
                    fileMetadataByMaxId[fileId] = File(fileId, blockSize, layoutIndex)

                    repeat(blockSize) {
                        layout[layoutIndex++] = fileId
                    }
                    fileId++

                } else {
                    repeat(blockSize) {
                        layout[layoutIndex++] = -1L
                    }
                }
            }
        }

        fun optimize(): LongArray {
            filesToMove.forEach { file ->
                tryMoveToGapClosestToDiskStart(file)
            }

            return layout
        }

        val filesToMove: Sequence<File> =
            fileMetadataByMaxId.values.asSequence()
                .takeWhile { file -> (leftMostGap?.start ?: 0) < file.originalPosition }

        val gaps: Sequence<IntRange>
            get() = layout.indices.iterator().let { iterator ->
                generateSequence {
                    while (iterator.hasNext()) {
                        val gapStartIncl = iterator.nextInt()
                        if (layout[gapStartIncl] != -1L) continue

                        var gapEndExcl = gapStartIncl + 1;
                        for (j in iterator) {
                            gapEndExcl = j
                            if (layout[j] != -1L) break
                        }

                        return@generateSequence gapStartIncl until gapEndExcl
                    }

                    return@generateSequence null
                }
            }

        // [size => [gaps]]
        val leftMostGapsIndex: Int2ObjectMap<Int2ObjectSortedMap<IntRange>> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            val result = Int2ObjectOpenHashMap<Int2ObjectSortedMap<IntRange>>()

            for (gap in gaps) {
                for (size in gap.fittingSizes()) {
                    result
                        .getOrPut(size) { Int2ObjectRBTreeMap() }
                        .put(gap.start, gap)
                }
            }

            return@lazy result
        }

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
                layout[gap.start + i] = layout[file.originalPosition + i]
                layout[file.originalPosition + i] = -1L
            }

            reindexGap(gap, file.size)
        }

        fun reindexGap(gap: IntRange, shorterBy: Int) {
            // remove the gap from the index
            for (size in gap.fittingSizes()) {
                leftMostGapsIndex[size]?.remove(gap.start)
            }

            val newStartIndex = gap.start + shorterBy
            val newGap = newStartIndex..gap.endInclusive

            // reindex the new gap
            for (size in newGap.fittingSizes()) {
                leftMostGapsIndex[size]!!.put(newGap.start, newGap)
            }
        }

        override fun toString(): String =
            layout
                .mapIndexed { index, value -> "$index: " + (if (value < 0L) "." else value.toString()) }
                .joinToString("\n")

        fun IntRange.fittingSizes(): List<Int> = if (this.length() > 0) indices.map { it + 1 } else emptyList()

        data class File(val id: Long, val size: Int, val originalPosition: Int)

    }

    companion object {

        fun parse(diskMap: String): Day09 = Day09(
            IntArray(diskMap.length) { diskMap[it].digitToInt() }
        )

    }

}
