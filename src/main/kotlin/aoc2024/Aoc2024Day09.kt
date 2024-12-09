package aoc2024

import utils.Resource

fun main() {
    solve(Resource.named("aoc2024/day09/example1.txt"))
    solve(Resource.named("aoc2024/day09/input.txt"))
}

private fun solve(input: Resource) {
    println("input: $input")

    val problem = input.day09()

    input.assertResult("task1") { problem.result1 }
    input.assertResult("task2") { problem.result2 }
}

fun Resource.day09(): Day09 = Day09(
    content()
)

data class Day09(val diskMap: String) {

    val unpacked by lazy {
        unpack(diskMap)
    }

    val result1 by lazy {
        fragmentByFileSegments(unpacked.first)
            .mapIndexed { position, fileId -> position * fileId }
            .sum()
    }

    val result2 by lazy {
        moveWholeFiles(unpacked.first, unpacked.second)
            .mapIndexed { position, fileId -> position * (fileId ?: 0) }
            .sum()
    }

    /**
     * left => [list of fileIds, indexes are positions]
     * right => [fileId => length to position]
     */
    fun unpack(data: String): Pair<List<Long?>, Map<Long, Pair<Int, Int>>> {
        val layout = mutableListOf<Long?>()
        val metadata = mutableMapOf<Long, Pair<Int, Int>>()

        var fileId = 0L
        for ((isFileBlock, blockSize) in data.trim().asSequence().map { it.digitToInt() }.mapIndexed { index, value -> (index.mod(2) == 0) to value }) {
            if (isFileBlock) {
                metadata[fileId] = blockSize to layout.size

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

        return layout to metadata
    }

    fun fragmentByFileSegments(layout: List<Long?>): List<Long> = buildList {
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

    fun moveWholeFiles(layout: List<Long?>, metadata: Map<Long, Pair<Int, Int>>): List<Long?> {
        // [position => fileId]
        val result = layout.toMutableList()

        // [fileId => length to position]
        val fileMetadataByMaxId = metadata.toSortedMap(Comparator.naturalOrder<Long>().reversed())

        var leftMostGap = 0
        for ((fileId, fileMetadata) in fileMetadataByMaxId.entries) {
            val (fileSize, originalFilePosition) = fileMetadata

            // leftmost gap can only move right
            leftMostGap = (leftMostGap until result.size).firstOrNull { result[it] == null } ?: leftMostGap

            if (leftMostGap > originalFilePosition) {
                break // cannot move files before the leftmost gap
            }

            for (gap in result.gapsSequence(leftMostGap, originalFilePosition)) {
                if (fileSize > gap.length()) {
                    continue
                }

                for (i in 0 until fileSize) {
                    result[gap.start + i] = fileId
                    result[originalFilePosition + i] = null
                }

                break; // move done, attempt next file
            }
        }

        return result
    }

    /**
     * Creates a lazy sequence of gap index ranges in O(n)
     */
    fun List<Long?>.gapsSequence(fromIndex: Int, toIndex: Int): Sequence<IntRange> {
        val list = this
        return sequence {
            val iterator = (fromIndex..toIndex).iterator()
            for (gapStartIncl in iterator) {
                if (list[gapStartIncl] != null) continue

                var gapEndExcl = gapStartIncl + 1;
                for (j in iterator) {
                    gapEndExcl = j
                    if (list[j] != null) break
                }

                yield(gapStartIncl until gapEndExcl)
            }
        }
    }

    fun IntRange.length() = last - first + 1

}
