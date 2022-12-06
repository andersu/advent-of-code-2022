package technology.anders

fun main() {
    CrateStacker().solveFirstPart()
}

class CrateStacker {

    fun solveFirstPart() {
        val stackerInput = parseInput()
        val updatedStacks = moveCrates(stackerInput)
        val topCrates = updatedStacks.values.map { it.first() }.joinToString("")
        println("First answer: $topCrates")
    }

    private fun parseInput(): StackerInput {
        val lines = readLinesFromResourceFile("december_5_input.txt")

        val emptyLineIndex = lines.indexOfFirst { it.isEmpty() }
        val numberOfCrates = lines[emptyLineIndex - 1].trim().last().digitToInt()
        val linesWithCrates = lines.subList(0, emptyLineIndex - 1)
        val stacks = getStacksFromLines(numberOfCrates, linesWithCrates)

        val linesWithMoves = lines.subList(emptyLineIndex + 1, lines.size)
        val moves = getMovesFromLines(linesWithMoves)

        return StackerInput(stacks, moves)
    }

    private fun getStacksFromLines(
        numberOfCrates: Int,
        lines: List<String>
    ): Map<Int, String> {
        val stacks = mutableMapOf<Int, String>()
        (1..numberOfCrates).forEach {
            stacks[it] = ""
        }

        lines.forEach { line ->
            val crates = line.chunked(4).map {
                it[1]
            }
            crates.forEachIndexed { index, crate ->
                if (!crate.isWhitespace()) {
                    stacks[index + 1] = stacks[index + 1] + crate
                }
            }
        }
        return stacks
    }

    private fun getMovesFromLines(lines: List<String>): List<Move> =
        lines.map {
            val values = it.split(" ")
            Move(
                amount = values[1].toInt(),
                fromStack = values[3].toInt(),
                toStack = values[5].toInt()
            )
        }

    private fun moveCrates(stackerInput: StackerInput): Map<Int,String> {
        val updatedStacks = stackerInput.stacks.toMutableMap()
        stackerInput.moves.forEach { move ->
            repeat(move.amount) {
                val fromStack = updatedStacks[move.fromStack]
                val toStack = updatedStacks[move.toStack]
                if (fromStack == null || toStack == null) {
                    println("Skipping invalid move: $move")
                } else {
                    val crateToMove = fromStack.first()
                    updatedStacks[move.fromStack] = fromStack.substring(1)
                    updatedStacks[move.toStack] = crateToMove + toStack
                }
            }
        }
        return updatedStacks
    }
}

data class StackerInput(
    val stacks: Map<Int, String>,
    val moves: List<Move>
)

data class Move(
    val amount: Int,
    val fromStack: Int,
    val toStack: Int
)

