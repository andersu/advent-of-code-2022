package technology.anders

import java.io.File
import java.lang.IllegalArgumentException

enum class Shape {
    ROCK,
    PAPER,
    SCISSORS
}

enum class Outcome {
    WIN,
    DRAW,
    LOSS
}

data class Round(
    val myShape: Shape,
    val opponentShape: Shape
)

fun main() {
    val rockPaperScissors = RockPaperScissors()
    rockPaperScissors.followStrategyPart1()
    rockPaperScissors.followStrategyPart2()
}

class RockPaperScissors {
    fun followStrategyPart1() {
        val rounds = readLinesFromInput().map {
            val letters = it.split(" ")
            Round(
                myShape = mapLetterToShapePart1(letters[1]),
                opponentShape = mapLetterToShapePart1(letters[0])
            )
        }
        val totalScore = rounds.sumOf { calculateScore(it) }

        println("First answer: $totalScore")
    }

    fun followStrategyPart2() {
        val rounds = readLinesFromInput().map {
            val letters = it.split(" ")
            val opponentShape = mapLetterToShapePart1(letters[0])
            val preferredOutcome = mapLetterToOutcome(letters[1])
            Round(
                myShape = chooseShape(opponentShape, preferredOutcome),
                opponentShape = mapLetterToShapePart1(letters[0])
            )
        }
        val totalScore = rounds.sumOf { calculateScore(it) }

        println("Second answer: $totalScore")
    }

    private fun readLinesFromInput() = File("src/main/resources/december_2_input.txt").readLines()

    private fun mapLetterToShapePart1(letter: String): Shape =
        when (letter) {
            "A", "X" -> Shape.ROCK
            "B", "Y" -> Shape.PAPER
            "C", "Z" -> Shape.SCISSORS
            else -> throw IllegalArgumentException("Invalid input. Letter must be A, B, C, X, Y or Z")
        }

    private fun mapLetterToShapePart2(letter: String): Shape =
        when (letter) {
            "A" -> Shape.ROCK
            "B" -> Shape.PAPER
            "C" -> Shape.SCISSORS
            else -> throw IllegalArgumentException("Invalid input. Letter must be A, B or C")
        }

    private fun mapLetterToOutcome(letter: String): Outcome =
        when (letter) {
            "X" -> Outcome.LOSS
            "Y" -> Outcome.DRAW
            "Z" -> Outcome.WIN
            else -> throw IllegalArgumentException("Invalid input. Letter must be X, Y or Z")
        }

    private fun chooseShape(opponentShape: Shape, outcome: Outcome) =
        when (outcome) {
            Outcome.DRAW -> opponentShape
            Outcome.WIN -> when (opponentShape) {
                Shape.ROCK -> Shape.PAPER
                Shape.PAPER -> Shape.SCISSORS
                Shape.SCISSORS -> Shape.ROCK
            }

            Outcome.LOSS -> when (opponentShape) {
                Shape.ROCK -> Shape.SCISSORS
                Shape.PAPER -> Shape.ROCK
                Shape.SCISSORS -> Shape.PAPER
            }
        }

    private fun calculateScore(round: Round): Int {
        val scoreForShape = when (round.myShape) {
            Shape.ROCK -> 1
            Shape.PAPER -> 2
            Shape.SCISSORS -> 3
        }

        val isWin = round.myShape == Shape.ROCK && round.opponentShape == Shape.SCISSORS ||
                round.myShape == Shape.PAPER && round.opponentShape == Shape.ROCK ||
                round.myShape == Shape.SCISSORS && round.opponentShape == Shape.PAPER
        val isDraw = round.myShape == round.opponentShape
        val scoreForResult = when {
            isWin -> 6
            isDraw -> 3
            else -> 0
        }

        return scoreForShape + scoreForResult
    }
}