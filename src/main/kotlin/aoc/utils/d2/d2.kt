package aoc.utils.d2

import aoc.utils.Resource
import aoc.utils.d2.Direction.entries
import aoc.utils.math.remEuclid
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.extension

open class Matrix<V : Any> protected constructor(
    val dims: Dimensions,
) {

    private val matrix: MutableList<V?> = MutableList(Math.toIntExact(dims.area)) { null }

    open fun allPositionsOfValue(value: V): Set<Position> =
        matrix.withIndex()
            .filter { it.value == value }
            .map { dims.matrixIndexToPosition(it.index) }
            .toSet()

    open fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
        matrix.withIndex()
            .filter { (_, value) -> value?.let(valueFilter) == true }
            .groupBy({ it.value!! }, { it.index })
            .mapValues { (_, indices) -> indices.map { dims.matrixIndexToPosition(it) }.toSet() }

    val entries: Sequence<Pair<Position, V>>
        get() = positions.map { it to this[it]!! }

    fun entriesInDirection(startInclusive: Position, direction: Direction): Sequence<Pair<Position, V>> =
        generateSequence(startInclusive) { it + direction }
            .takeWhile { it in this } // only for coordinates within matrix
            .map { it to this[it]!! }

    val positions: Sequence<Position>
        get() = dims.matrixPositions

    fun putAll(other: Matrix<V>) =
        other.entries.forEach { (position, value) -> this[position] = value }

    open operator fun set(position: Position, value: V) {
        require(position in this) { "$position is not in matrix${dims}" }
        matrix[dims.matrixIndex(position)] = value
    }

    operator fun get(position: Position): V? =
        if (position in this) matrix[dims.matrixIndex(position)] else null

    operator fun contains(position: Position): Boolean =
        position.x > -1 && position.y > -1
          && position.x <= dims.maxX && position.y <= dims.maxY

    fun copy(): Matrix<V> =
        Matrix<V>(dims).also {
            it.matrix.clear()
            it.matrix.addAll(this.matrix)
        }

    fun draw(file: Path, valueToPixel: (V) -> java.awt.Color) {
        ImageIO.write(draw(valueToPixel), file.extension.lowercase(), file.toFile())
    }

    fun draw(valueToPixel: (V) -> java.awt.Color): BufferedImage {
        val image = BufferedImage(dims.w.toInt(), dims.h.toInt(), BufferedImage.TYPE_INT_ARGB)

        entries.forEach { (pos, value) ->
            image.setRGB(pos.x.toInt(), pos.y.toInt(), valueToPixel(value).rgb)
        }

        return image
    }

    fun withValuesIndex(): WithValuesIndex<V> =
        WithValuesIndex<V>(dims).apply { putAll(this@apply) }

    override fun toString(): String =
        positions
            .map { "${this[it]}" + (if (it.x == dims.maxX) "\n" else "") }
            .joinToString("")

    companion object {

        fun <V : Any> empty(dims: Dimensions): Matrix<V> =
            Matrix<V>(dims)

        fun <V : Any> of(dims: Dimensions, initialValue: () -> V): Matrix<V> =
            Matrix<V>(dims).apply {
                positions.forEach { this[it] = initialValue() }
            }

        fun ofChars(cells: Resource.CharMatrix2d): Matrix<Char> =
            of(cells, { it })

        fun ofInts(cells: Resource.CharMatrix2d): Matrix<Int> =
            of(cells, { it.digitToInt() })

        fun <V : Any> of(cells: Resource.CharMatrix2d, toValue: (Char) -> V): Matrix<V> =
            Matrix<V>(cells.dims).apply {
                cells.entries.forEach { (pos, char) -> this[pos] = toValue(char) }
            }

    }

    class WithValuesIndex<V : Any> internal constructor(dims: Dimensions) : Matrix<V>(dims) {

        private val positionsByValue: MutableMap<V, MutableSet<Position>> = mutableMapOf()

        val uniqueValues: Set<V>
            get() = positionsByValue.keys.toSet()

        override fun allPositionsOfValue(value: V): Set<Position> =
            positionsByValue[value] ?: emptySet()

        override fun allPositionsByValues(valueFilter: (V) -> Boolean): Map<V, Set<Position>> =
            positionsByValue.filterKeys(valueFilter)

        override fun set(position: Position, value: V) {
            val oldValue = this[position]

            super.set(position, value)

            positionsByValue[oldValue]?.remove(position)
            positionsByValue.getOrPut(value) { mutableSetOf() }.add(position)
        }
    }
}

enum class Direction(val vector: Distance) {
    UP(Distance(0, -1)),
    RIGHT_UP(Distance(1, -1)),
    RIGHT(Distance(1, 0)),
    RIGHT_DOWN(Distance(1, 1)),
    DOWN(Distance(0, 1)),
    LEFT_DOWN(Distance(-1, 1)),
    LEFT(Distance(-1, 0)),
    LEFT_UP(Distance(-1, -1)),
    ;

    fun isHorizontal(): Boolean = when (this) {
        RIGHT, LEFT -> true
        else -> false
    }

    fun isVertical(): Boolean = when (this) {
        UP, DOWN -> true
        else -> false
    }

    fun isCardinal(): Boolean = this in entriesCardinal

    fun isDiagonal(): Boolean = this in entriesDiagonal

    fun turnClockwise(steps: Int = 1): Direction = entriesByOrdinal[(ordinal + steps).remEuclid(size)]

    fun turnCounterClockwise(steps: Int = 1): Direction = entriesByOrdinal[(ordinal - steps).remEuclid(size)]

    fun turnRight90(): Direction = turnClockwise(2)

    fun turnLeft90(): Direction = turnCounterClockwise(2)

    fun turnRight45(): Direction = turnClockwise(1)

    fun turnLeft45(): Direction = turnCounterClockwise(1)

    companion object {

        val size by lazy {
            entries.size
        }

        val entriesDiagonal = setOf(RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN)
        val entriesCardinal = setOf(UP, RIGHT, DOWN, LEFT)

        private val entriesByOrdinal: List<Direction> = entries.toList()

    }

}

class DirectionBitSet : Iterable<Direction> {

    private var set = 0

    operator fun contains(dir: Direction): Boolean =
        set and maskOf(dir) != 0

    fun add(dir: Direction) {
        set = set or maskOf(dir)
    }

    fun remove(dir: Direction) {
        set = set and maskOf(dir).inv()
    }

    override fun iterator(): Iterator<Direction> =
        Direction.entries.asSequence()
            .filter { it in this }
            .iterator()

    companion object {

        fun maskOf(dir: Direction): Int =
            1 shl dir.ordinal

    }

}

/**
 * zero indexed
 */
data class Position(val x: Long, val y: Long) {

    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    constructor(x: String, y: String) : this(x.toLong(), y.toLong())

    operator fun plus(other: Position): Position =
        Position(this.x + other.x, this.y + other.y)

    operator fun plus(other: Direction): Position =
        this.plus(other.vector)

    operator fun plus(other: Distance): Position =
        Position(this.x + other.xDiff, this.y + other.yDiff)

    /**
     * Fits the position into given dimensions which represent a (w * h) matrix
     */
    operator fun rem(dims: Dimensions): Position =
        Position(
            x.remEuclid(dims.w),
            y.remEuclid(dims.h)
        )

    fun relativeDirectionTo(other: Position): Direction? =
        other.distanceTo(this).asDirection()

    fun distanceTo(other: Position): Distance =
        Distance(this.x - other.x, this.y - other.y)

    fun vectorInDirection(direction: Direction, length: Int): List<Position> =
        List(length) { if (it == 0) this else stepInDirection(direction, it) }

    fun stepInDirection(direction: Direction, length: Int): Position = when (length) {
        1 -> this.plus(direction)
        else -> Position(this.x + (direction.vector.xDiff * length), this.y + (direction.vector.yDiff * length))
    }

    val left: Position
        get() = plus(Direction.LEFT)

    val right: Position
        get() = plus(Direction.RIGHT)

    val up: Position
        get() = plus(Direction.UP)

    val down: Position
        get() = plus(Direction.DOWN)

    override fun toString(): String = "(x=$x, y=$y)"

    companion object {

        fun comparatorXThenY(): Comparator<Position> =
            Comparator.comparing(Position::x, Long::compareTo)
                .thenComparing(Position::y, Long::compareTo)

        fun comparatorYThenX(): Comparator<Position> =
            Comparator.comparing(Position::y, Long::compareTo)
                .thenComparing(Position::x, Long::compareTo)

    }

}

data class Dimensions(val w: Long, val h: Long) {

    constructor(w: Int, h: Int) : this(w.toLong(), h.toLong())

    constructor(w: String, h: String) : this(w.toLong(), h.toLong())

    val maxX = (w - 1)
    val maxY = (h - 1)

    val area = w * h

    fun matrixIndex(position: Position): Int =
        Math.toIntExact((position.y * w) + position.x)

    fun matrixIndexToPosition(index: Int): Position =
        matrixIndexToPosition(index.toLong())

    fun matrixIndexToPosition(index: Long): Position =
        Position(index % w, index / w)

    val matrixIndices: Sequence<Long>
        get() = (0L until area).asSequence()

    val matrixPositions: Sequence<Position>
        get() = matrixIndices.map { matrixIndexToPosition(it) }

    override fun toString(): String = "($w x $h)"

}

data class Distance(val xDiff: Long, val yDiff: Long) {

    constructor(xDiff: Int, yDiff: Int) : this(xDiff.toLong(), yDiff.toLong())

    constructor(xDiff: String, yDiff: String) : this(xDiff.toLong(), yDiff.toLong())

    operator fun times(length: Int): Distance =
        Distance(xDiff * length, yDiff * length)

    fun asDirection(): Direction? =
        toDirectionCache[this]

    override fun toString(): String = "(x=$xDiff, y=$yDiff)"

    companion object {

        private val toDirectionCache: Map<Distance, Direction> by lazy {
            Direction.entries.associateBy { it.vector }
        }

    }

}

data class OrientedPosition(var position: Position, var direction: Direction) {

    fun turnRight90(): OrientedPosition =
        OrientedPosition(position, direction.turnRight90())

    fun turnLeft90(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft90())

    fun turnRight45(): OrientedPosition =
        OrientedPosition(position, direction.turnRight45())

    fun turnLeft45(): OrientedPosition =
        OrientedPosition(position, direction.turnLeft45())

    fun step(count: Int = 1): OrientedPosition =
        OrientedPosition(
            position.stepInDirection(direction, count),
            direction
        )

    override fun toString(): String = "$position -> $direction"

}

class MatrixGraph<V : Any>(dims: Dimensions, neighbourSides: Set<Direction>) {

    val neighbourSides = DirectionBitSet().apply {
        neighbourSides.forEach { add(it) }
    }

    val nodes: Matrix<Node> = Matrix.empty<Node>(dims)

    operator fun set(position: Position, value: V) {
        nodes[position] = Node(value, position)
    }

    operator fun get(position: Position): Node? =
        nodes[position]

    fun connectionsOf(pos: Position): List<Position> =
        nodes[pos]?.connections ?: emptyList()

    fun connectionWeight(from: Position, to: Position): Long? =
        nodes[from]?.weightedConnections?.get(to)

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

    fun anyShortestPath(
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
                queue.add(PathStep(neighbourPos, neighbourDir, stepCost = edgeCost(currentStep, neighbourDir), prev = currentStep))
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

    inner class Node(
        var value: V,
        val position: Position,
    ) {

        // position to weight
        val weightedConnections = mutableMapOf<Position, Long>()

        val connections
            get() = weightedConnections.keys.toList()

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
    ) {

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

        override fun toString(): String = "($inDir -> $pos, cost=$pathCost, prev=${prev?.pos})"

    }

    companion object {

        const val INFINITE_COST = (Long.MAX_VALUE / 2)

        fun <V : Any> of(
            matrix: Resource.CharMatrix2d,
            neighbourSides: Set<Direction>,
            nodeValues: (Char) -> V,
            edges: (MatrixGraph<V>.Node, MatrixGraph<V>.Node) -> Pair<Boolean, Long>,
        ): MatrixGraph<V> {
            val result = MatrixGraph<V>(matrix.dims, neighbourSides)

            for ((position, value) in matrix.entries) {
                result[position] = nodeValues(value)
            }

            for (position in result.nodes.positions) {
                val node = result[position]!!

                for (neighbourPosition in node.neighbourPositionsValid()) {
                    val candidate = result[neighbourPosition.position]!!
                    val (isConnected, weight) = edges(node, candidate)
                    if (isConnected) {
                        node.weightedConnections[candidate.position] = weight
                    }
                }
            }

            return result
        }

    }

}
