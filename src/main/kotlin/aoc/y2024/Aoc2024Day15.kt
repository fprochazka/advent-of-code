package aoc.y2024

import aoc.utils.Resource
import aoc.utils.d2.*

fun Resource.day15(): Day15 = Day15.parse(content())

data class Day15(
    val smallWarehouse: Matrix<Char>,
    val moves: List<Direction>,
) {

    val bigWarehouse by lazy { smallWarehouse.scaleUp() }

    val result1 by lazy {
        smallWarehouse.copy()
            .smallWarehouseApplyMoves(moves)
            .allPositionsOfValue(SMALL_BOX)
            .sumOf { it.boxGps() }
    }

    val result2 by lazy {
        bigWarehouse.copy()
            .bigWarehouseApplyMoves(moves)
            .allPositionsOfValue(BIG_BOX_LEFT)
            .sumOf { it.boxGps() }
    }

    // #######
    // #...O..    100 * 1 + 4 = 104
    // #......
    fun Position.boxGps(): Long = (100 * y) + x

    fun Matrix<Char>.smallWarehouseApplyMoves(moves: List<Direction>): Matrix<Char> {
        var robotPos = allPositionsOfValue(ROBOT).first()
        this[robotPos] = EMPTY

        fun findFirstEmptySpaceInDirection(move: Direction): Position? {
            val positionsInDirectionOfMove = generateSequence(robotPos + move) { it + move }
                .takeWhile { it in this } // only for coordinates within matrix

            for (pos in positionsInDirectionOfMove) {
                when (this[pos]) {
                    WALL -> return null // if we find a wall before we find empty spot, we cannot move the boxes
                    EMPTY -> return pos // we can move the boxes into this empty slot
                }
            }

            throw IllegalStateException("No WALL or EMPTY found from $robotPos -> $move")
        }

        for (move in moves) {
            val robotWantsToMoveTo = robotPos + move
            when (this[robotWantsToMoveTo]) {
                WALL -> continue // cannot move into wall

                EMPTY -> {
                    robotPos = robotWantsToMoveTo
                    continue // empty space, naively go for it
                }

                SMALL_BOX -> {
                    val firstEmptySpaceInMoveDirection = findFirstEmptySpaceInDirection(move)
                    if (firstEmptySpaceInMoveDirection == null) {
                        continue // nowhere to move the boxes
                    }

                    // we don't have to move all individual boxes, just jump the first one to the empty slot, it looks the same
                    this[robotWantsToMoveTo] = EMPTY
                    this[firstEmptySpaceInMoveDirection] = SMALL_BOX

                    robotPos = robotWantsToMoveTo
                }

                else -> error("Unexpected value ${this[robotWantsToMoveTo]} at $robotWantsToMoveTo")
            }
        }

        this[robotPos] = ROBOT

        return this
    }

    fun Matrix<Char>.bigWarehouseApplyMoves(moves: List<Direction>): Matrix<Char> {
        var robotPos = allPositionsOfValue(ROBOT).first()
        this[robotPos] = EMPTY

        val toRight1 = Distance(1, 0)
        val toLeft1 = Distance(-1, 0)

        // box positions are always the BOXes left side
        fun boxPositionAt(at: Position): Position? = when (this[at]) {
            BIG_BOX_LEFT -> at
            BIG_BOX_RIGHT -> at + toLeft1
            else -> null
        }

        fun collectBoxesAffectedByMoveIfTheyAreNotBlockedByWall(move: Direction): Set<Position> {
            val result = mutableSetOf<Position>()

            val robotWantsToMoveTo = robotPos + move

            if (move == Direction.LEFT || move == Direction.RIGHT) {
                val positionsInDirectionOfMove = generateSequence(robotWantsToMoveTo) { it + move }
                    .takeWhile { it in this } // only for coordinates within matrix

                for (pos in positionsInDirectionOfMove) {
                    if (this[pos] == WALL) return emptySet() // if we find a wall before we find empty spot, we cannot move the boxes
                    if (this[pos] == EMPTY) break // we've reached an empty spot
                    result.add(boxPositionAt(pos) ?: error("expected box at $pos, but got ${this[pos]}"))
                }

                return result
            }

            var boxesRow = setOf(boxPositionAt(robotWantsToMoveTo)!!)
            while (true) {
                var nextBoxesRow = mutableSetOf<Position>()

                var foundMoreBoxesToMove = false
                for (position in boxesRow) {
                    val nextPosition = position + move

                    if (this[nextPosition] == WALL || this[nextPosition + toRight1] == WALL) {
                        return emptySet() // cannot move boxes into a wall
                    }

                    val nextAffectedBoxes = setOfNotNull(boxPositionAt(nextPosition), boxPositionAt(nextPosition + toRight1))
                    nextBoxesRow.addAll(nextAffectedBoxes)
                    foundMoreBoxesToMove = foundMoreBoxesToMove || nextAffectedBoxes.isNotEmpty()
                }

                result.addAll(boxesRow)
                boxesRow = nextBoxesRow

                if (!foundMoreBoxesToMove) {
                    break
                }
            }

            return result
        }

        for (move in moves) {
            val robotWantsToMoveTo = robotPos + move
            if (this[robotWantsToMoveTo] == WALL) {
                continue // cannot move into wall
            }
            if (this[robotWantsToMoveTo] == EMPTY) {
                robotPos = robotWantsToMoveTo
                continue // empty space, naively go for it
            }

            // box LEFT or RIGHT
            val boxPositions = collectBoxesAffectedByMoveIfTheyAreNotBlockedByWall(move)
            if (boxPositions.isEmpty()) {
                continue // nowhere to move the boxes
            }

            // pick up the boxes
            for (boxPosition in boxPositions) {
                this[boxPosition] = EMPTY
                this[boxPosition + toRight1] = EMPTY
            }

            // place them all
            for (boxPosition in boxPositions) {
                this[boxPosition + move] = BIG_BOX_LEFT
                this[boxPosition + toRight1 + move] = BIG_BOX_RIGHT
            }

            robotPos = robotWantsToMoveTo
        }

        this[robotPos] = ROBOT

        return this
    }

    fun Matrix<Char>.scaleUp(): Matrix<Char> {
        val bigWarehouse = Matrix.empty<Char>(dims.let { Dimensions(it.w * 2, it.h) })

        val toRight = Distance(1, 0)
        for ((smallPos, value) in entries) {
            val bigPos = Position(smallPos.x * 2, smallPos.y)
            when (value) {
                EMPTY, WALL -> {
                    bigWarehouse[bigPos] = value
                    bigWarehouse[bigPos + toRight] = value
                }

                SMALL_BOX -> {
                    bigWarehouse[bigPos] = BIG_BOX_LEFT
                    bigWarehouse[bigPos + toRight] = BIG_BOX_RIGHT
                }

                ROBOT -> {
                    bigWarehouse[bigPos] = value
                    bigWarehouse[bigPos + toRight] = EMPTY
                }
            }
        }

        return bigWarehouse
    }

    companion object {

        fun parse(input: String): Day15 {
            val (warehouse, moves) = input.split("\n\n", limit = 2).map { it.trim() }

            return Day15(
                parseWarehouse(warehouse),
                parseMoves(moves),
            )
        }

        fun parseWarehouse(string: String): Matrix<Char> {
            return Matrix.ofChars(string.lines().let { Resource.CharMatrix2d.fromLines(it) })
        }

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

        const val WALL = '#'
        const val EMPTY = '.'
        const val ROBOT = '@'

        const val SMALL_BOX = 'O'

        const val BIG_BOX_LEFT = '['
        const val BIG_BOX_RIGHT = ']'

    }

}
