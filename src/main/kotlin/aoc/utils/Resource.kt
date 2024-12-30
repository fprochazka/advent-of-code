package aoc.utils

import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.*

data class Resource(val name: String) {

    val path: Path by lazy { resourcesDir.resolve(name) }

    fun stream(): InputStream = path.toFile().inputStream()

    fun content(): String = stream().bufferedReader().use { it.readText() }

    fun allLines(): List<String> = stream().bufferedReader().readLines()

    fun nonBlankLines(): List<String> = allLines().filter { it.isNotBlank() }

    fun matrix2d(): CharMatrix2d =
        CharMatrix2d.fromLines(nonBlankLines())

    fun assertResult(name: String, compute: () -> Any) {
        this.resultNamed(name)
            .snapshotResult(compute)
            .also { println("${name}: $it") }
    }

    private fun snapshotResult(compute: () -> Any): String {
        val result = compute().toString()
        snapshotResult(result)
        return result
    }

    private fun snapshotResult(result: String) {
        val toSnapshot = result.trimEnd() + "\n"

        if (!path.exists()) {
            path.writeText(toSnapshot)
            return
        }

        val existingContent = path.readText()
        if (existingContent != toSnapshot) {
            val diff = TextDiff.of(existingContent, toSnapshot)
            throw IllegalStateException("Snapshot file has changed. Please delete the file and it will be overwritten.\n\nSee differences for '$name':\n\n${diff.formatted}\n")
        }
    }

    private fun resultNamed(suffix: String): Resource {
        var relativePath = Path.of(name)
        val filename = "${relativePath.fileName.nameWithoutExtension}_${suffix}.${relativePath.fileName.extension}"
        val siblingPath = relativePath.resolveSibling(filename)
        return Resource(siblingPath.toString())
    }

    override fun toString(): String = name

    class CharMatrix2d(
        val lines: List<String>,
        val dims: aoc.utils.d2.AreaDimensions
    ) {

        val entries: Sequence<MatrixChar>
            get() = lines.asSequence()
                .flatMapIndexed { y, line ->
                    line.mapIndexed { x, char -> MatrixChar(x, y, char) }
                }

        inner class MatrixChar(val x: Int, val y: Int, val value: Char) {
            val pos: aoc.utils.d2.Position
                get() = dims.positionFor(x, y)
        }

        companion object {

            fun fromContent(content: String): CharMatrix2d =
                fromLines(content.lines())

            fun fromLines(lines: List<String>): CharMatrix2d =
                CharMatrix2d(
                    lines,
                    aoc.utils.d2.AreaDimensions(lines.first().length, lines.size)
                )

        }

    }

    companion object {

        @JvmStatic
        fun named(filename: String): Resource = Resource(filename)

        private val projectRoot by lazy { findProjectRoot() }

        private val resourcesDir by lazy { projectRoot.resolve("src/main/resources") }

        private fun findProjectRoot(): Path {
            val cwd = Path.of("").toAbsolutePath().normalize()
            var dir = cwd
            while (!dir.resolve("settings.gradle.kts").exists()) {
                dir = dir.parent
                if (dir == null || dir == dir.fileSystem.rootDirectories.first()) {
                    throw IllegalStateException("Cannot locale project root")
                }
            }
            return dir
        }

    }

}
