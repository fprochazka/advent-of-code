package aoc.y2024

import aoc.utils.Resource
import aoc.utils.containers.addAllNotNull
import aoc.utils.d2.*

fun Resource.day15(): Day15 = Day15.parse(content())

data class Day15(
    val smallWarehouse: Matrix<Char>,
    val moves: List<Direction>,
) {

    val bigWarehouse by lazy { smallWarehouse.scaleUp() }

    val result1 by lazy {
        smallWarehouse.copy()
            .also { it.applyMoves(moves) { robotPos, move -> it.smallWarehouseMoveBoxes(robotPos, move) } }
            .allPositionsOfValue(SMALL_BOX)
            .sumOf { it.boxGps() }
    }

    val result2 by lazy {
        bigWarehouse.copy()
            .also { it.applyMoves(moves) { robotPos, move -> it.bigWarehouseMoveBoxes(robotPos, move) } }
            .allPositionsOfValue(BIG_BOX_LEFT)
            .sumOf { it.boxGps() }
    }

    // #######
    // #...O..    100 * 1 + 4 = 104
    // #......
    fun Position.boxGps(): Long = (100 * y) + x

    fun Matrix<Char>.entriesInDirection(startExclusive: Position, dir: Direction): Sequence<Pair<Position, Char>> =
        generateSequence(startExclusive + dir) { it + dir }
            .takeWhile { it in this } // only for coordinates within matrix
            .map { it to this[it]!! }

    fun Matrix<Char>.smallWarehouseMoveBoxes(robotPos: Position, move: Direction): Position {
        fun findFirstEmptySpaceInDirection(move: Direction): Position? {
            for ((pos, value) in entriesInDirection(robotPos, move)) {
                when (value) {
                    WALL -> return null // if we find a wall before we find empty spot, we cannot move the boxes
                    EMPTY -> return pos // we can move the boxes into this empty slot
                }
            }

            throw IllegalStateException("No WALL or EMPTY found from $robotPos -> $move")
        }

        val emptySpaceForBox = findFirstEmptySpaceInDirection(move)
            ?: return robotPos // nowhere to move the boxes

        val robotWantsToMoveTo = robotPos + move

        // we don't have to move all individual boxes, just jump the first one to the empty slot, it looks the same
        this.removeBox(robotWantsToMoveTo)
        this.placeSmallBox(emptySpaceForBox)

        return robotWantsToMoveTo
    }

    fun Matrix<Char>.bigWarehouseMoveBoxes(robotPos: Position, move: Direction): Position {
        fun collectBoxesAffectedByMoveForHorizontal(move: Direction): Set<Position>? {
            val result = mutableSetOf<Position>()

            for ((pos, value) in entriesInDirection(robotPos, move)) {
                when (value) {
                    WALL -> return null // if we find a wall before we find empty spot, we cannot move the boxes
                    EMPTY -> break // we've reached an empty spot
                    else -> result.add(boxPositionAt(pos) ?: error("expected box at $pos, but got $value"))
                }
            }

            return result
        }

        fun collectBoxesAffectedByMoveForVertical(move: Direction): Set<Position>? {
            fun nextBoxesRow(row: Set<Position>): Set<Position>? {
                var nextRow = mutableSetOf<Position>()

                for ((nextLeft, nextRight) in row.map { (it + move).bothBoxPositions() }) {
                    if (this[nextLeft] == WALL || this[nextRight] == WALL) {
                        return null // cannot move boxes into a wall
                    }

                    nextRow.addAllNotNull(
                        boxPositionAt(nextLeft),
                        boxPositionAt(nextRight)
                    )
                }

                return nextRow
            }

            val result = mutableSetOf<Position>()

            var boxesRow = setOf(boxPositionAt(robotPos + move)!!)
            while (boxesRow.isNotEmpty()) {
                result.addAll(boxesRow)
                boxesRow = nextBoxesRow(boxesRow) ?: return null // cannot move boxes into a wall
            }

            return result
        }

        fun collectBoxesAffectedByMove(): Set<Position>? = when {
            move.isHorizontal() -> collectBoxesAffectedByMoveForHorizontal(move)
            move.isVertical() -> collectBoxesAffectedByMoveForVertical(move)
            else -> error("Unexpected direction $move")
        }

        val affectedBoxes = collectBoxesAffectedByMove()
            ?: return robotPos // nowhere to move the boxes

        // pick up and move the boxes
        affectedBoxes.forEach { this.removeBox(it) }
        affectedBoxes.map { it + move }.forEach { this.placeBigBox(it) }

        return robotPos + move
    }

    fun Position.bothBoxPositions() = this to this + toRight1

    fun Matrix<Char>.boxPositionAt(at: Position): Position? = when (this[at]) {
        SMALL_BOX -> at

        // big box positions are always the box's left side
        BIG_BOX_LEFT -> at
        BIG_BOX_RIGHT -> at + toLeft1

        else -> null
    }

    fun Matrix<Char>.removeBox(at: Position) {
        when (this[at]) {
            SMALL_BOX -> {
                this[at] = EMPTY
            }

            BIG_BOX_LEFT -> {
                this[at] = EMPTY
                this[at + toRight1] = EMPTY
            }

            else -> error("Unexpected value ${this[at]} at $at")
        }
    }

    fun Matrix<Char>.placeSmallBox(at: Position) {
        this[at] = SMALL_BOX
    }

    fun Matrix<Char>.placeBigBox(at: Position) {
        this[at] = BIG_BOX_LEFT
        this[at + toRight1] = BIG_BOX_RIGHT
    }

    fun Matrix<Char>.applyMoves(moves: List<Direction>, tryMovingBoxes: (Position, Direction) -> Position): Matrix<Char> {
        var robotPos = allPositionsOfValue(ROBOT).first()
        this[robotPos] = EMPTY

        for (move in moves) {
            val robotWantsToMoveTo = robotPos + move
            when (this[robotWantsToMoveTo]) {
                WALL -> continue // cannot move into wall

                EMPTY -> {
                    robotPos = robotWantsToMoveTo
                    continue // empty space, naively go for it
                }

                else -> {
                    robotPos = tryMovingBoxes(robotPos, move)
                }
            }
        }

        this[robotPos] = ROBOT

        return this
    }

    fun Matrix<Char>.scaleUp(): Matrix<Char> {
        fun Char.scaleUp(): Pair<Char, Char> = when (this) {
            EMPTY, WALL -> this to this
            SMALL_BOX -> BIG_BOX_LEFT to BIG_BOX_RIGHT
            ROBOT -> this to EMPTY
            else -> error("Unexpected value $this")
        }

        val result = Matrix.empty<Char>(dims.let { Dimensions(it.w * 2, it.h) })
        for ((bigPos, value) in entries.map { (smallPos, value) -> smallPos.copy(x = smallPos.x * 2) to value }) {
            value.scaleUp().let { (left, right) ->
                result[bigPos] = left
                result[bigPos + toRight1] = right
            }
        }

        return result
    }

    companion object {

        fun parse(input: String): Day15 =
            input.split("\n\n", limit = 2)
                .map { it.trim() }
                .let { (warehouse, moves) ->
                    Day15(
                        parseWarehouse(warehouse),
                        parseMoves(moves),
                    )
                }

        fun parseWarehouse(raw: String): Matrix<Char> =
            Matrix.ofChars(Resource.CharMatrix2d.fromContent(raw))

        fun parseMoves(raw: String): List<Direction> =
            raw.mapNotNull {
                when (it) {
                    '^' -> Direction.UP
                    'v' -> Direction.DOWN
                    '>' -> Direction.RIGHT
                    '<' -> Direction.LEFT
                    else -> null
                }
            }

        val toRight1 = Distance(1, 0)
        val toLeft1 = Distance(-1, 0)

        const val WALL = '#'
        const val EMPTY = '.'
        const val ROBOT = '@'

        const val SMALL_BOX = 'O'

        const val BIG_BOX_LEFT = '['
        const val BIG_BOX_RIGHT = ']'

    }

}
