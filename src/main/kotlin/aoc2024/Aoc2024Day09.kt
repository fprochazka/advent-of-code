package aoc2024

import utils.Resource
import java.util.*

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
        moveWholeFiles(unpacked)
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
        var fileBlock = true
        for (blockSize in data.trim().asSequence().map { it.digitToInt() }) {
            if (fileBlock) {
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

            fileBlock = !fileBlock
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

    fun moveWholeFiles(unpacked: Pair<List<Long?>, Map<Long, Pair<Int, Int>>>): List<Long?> {

        fun List<Long?>.gapsSequence(fromIndex: Int, toIndex: Int): Sequence<Pair<Int, Int>> {
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

                    yield(gapStartIncl to gapEndExcl)
                }
            }
        }

        val result = unpacked.first.toMutableList()

        // [fileId => length to position]
        val availableFilesByMaxId = unpacked.second.toSortedMap(Comparator.naturalOrder<Long>().reversed()) as TreeMap<Long, Pair<Int, Int>>
        val movableFiles = availableFilesByMaxId.entries.iterator()

        var leftMostGap = 0
        while (movableFiles.hasNext()) {

            for (i in leftMostGap until result.size) {
                if (result[i] != null) continue

                leftMostGap = i
                break
            }

            val (fileId, fileMetadata) = movableFiles.next()
            val (fileSize, originalFilePosition) = fileMetadata

            if (leftMostGap > originalFilePosition) {
                break // cannot move files before the leftmost gap
            }

            for ((gapStartIncl, gapEndExcl) in result.gapsSequence(leftMostGap, originalFilePosition)) {
                val gapSize = gapEndExcl - gapStartIncl
                if (fileSize > gapSize) {
                    continue
                }

                for (i in gapStartIncl until (gapStartIncl + fileSize)) {
                    result[i] = fileId
                }

                for (i in originalFilePosition until (originalFilePosition + fileSize)) {
                    result[i] = null
                }

                break; // move done, attempt next file
            }
        }

        return result
    }

}
