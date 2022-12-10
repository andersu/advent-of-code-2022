package technology.anders

import kotlin.math.abs

fun main() {
    val ropeBridge = RopeBridge()
    val firstAnswer = ropeBridge.solveFirstPart()
    val secondAnswer = ropeBridge.solveSecondPart()
    println("First answer: $firstAnswer")
    println("Second answer: $secondAnswer")

}

class RopeBridge {
    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_9_input.txt")
        val moves = mapLinesToMoves(lines)

        val ropeTracker = RopeTracker(numberOfKnots = 2)
        moves.forEach { move ->
            ropeTracker.move(move)
        }
        return ropeTracker.tailPositionsVisited.count()
    }

    fun solveSecondPart(): Int {
        val lines = readLinesFromResourceFile("december_9_input.txt")
        val moves = mapLinesToMoves(lines)

        val ropeTracker = RopeTracker(numberOfKnots = 10)
        moves.forEach { move ->
            ropeTracker.move(move)
        }

        return ropeTracker.tailPositionsVisited.count()
    }

    private fun mapLinesToMoves(lines: List<String>) = lines.map {
        val parts = it.split(" ")
        val direction = when (parts.first()) {
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            "R" -> Direction.RIGHT
            "L" -> Direction.LEFT
            else -> throw IllegalArgumentException("Invalid direction: ${parts.first()}. Must be U, D, R or L.")
        }
        Move(steps = parts.last().toInt(), direction = direction)
    }

    enum class Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    data class Move(
        val steps: Int,
        val direction: Direction
    )
}

class RopeTracker(numberOfKnots: Int) {
    private val knotPositions = MutableList(numberOfKnots) { Position(0, 0) }

    private val _tailPositionsVisited = mutableSetOf(knotPositions.last())
    val tailPositionsVisited: Set<Position> get() = _tailPositionsVisited

    fun move(move: RopeBridge.Move) {
        repeat(move.steps) {
            moveHead(move.direction)
            moveRestOfRope()
        }
    }

    private fun moveHead(direction: RopeBridge.Direction) {
        val headPosition = knotPositions[0]
        val newPosition = when (direction) {
            RopeBridge.Direction.UP -> headPosition.copy(y = headPosition.y - 1)
            RopeBridge.Direction.DOWN -> headPosition.copy(y = headPosition.y + 1)
            RopeBridge.Direction.LEFT -> headPosition.copy(x = headPosition.x - 1)
            RopeBridge.Direction.RIGHT -> headPosition.copy(x = headPosition.x + 1)
        }
        knotPositions[0] = newPosition
    }

    private fun moveRestOfRope() {
        for (i in 0..knotPositions.size - 2) {
            knotPositions[i + 1]  = calculateNewPositionForNextKnot(i)

            val isTail = i + 1 == knotPositions.size - 1
            if (isTail) {
                _tailPositionsVisited += knotPositions.last()
            }
        }
    }

    private fun calculateNewPositionForNextKnot(knotIndex: Int): Position {
        val nextKnotPosition = knotPositions[knotIndex + 1]
        val thisKnotPosition = knotPositions[knotIndex]

        val xDistance = thisKnotPosition.x - nextKnotPosition.x
        val yDistance = thisKnotPosition.y - nextKnotPosition.y

        return when {
            abs(xDistance) == 2 && abs(yDistance) == 2 -> {
                Position(nextKnotPosition.x + xDistance / 2, nextKnotPosition.y + yDistance / 2)
            }

            abs(xDistance) == 2 -> {
                Position(nextKnotPosition.x + xDistance / 2, thisKnotPosition.y)
            }

            abs(yDistance) == 2 -> {
                Position(thisKnotPosition.x, nextKnotPosition.y + yDistance / 2)
            }

            else -> nextKnotPosition
        }
    }
}

data class Position(
    val x: Int,
    val y: Int
)

