package technology.anders.december14

import technology.anders.readLinesFromResourceFile

fun main() {
    val sandCounter = SandCounter()
    val firstAnswer = sandCounter.solveFirstPart()
    println("First answer: $firstAnswer")
}

class SandCounter {
    private val caveMapParser = CaveMapParser()
    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_14_input.txt")
        val caveMap = caveMapParser.parseCaveMap(lines)
        caveMap.simulateSandFalling()

        return caveMap.getNumberOfRestingSandUnits()
    }
}