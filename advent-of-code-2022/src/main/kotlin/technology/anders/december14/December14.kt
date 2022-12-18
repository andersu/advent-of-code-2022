package technology.anders.december14

import technology.anders.readLinesFromResourceFile

fun main() {
    val sandCounter = SandCounter()
    val firstAnswer = sandCounter.solveFirstPart()
    val secondAnswer = sandCounter.solveSecondPart()
    println("First answer: $firstAnswer")
    println("Second answer: $secondAnswer")
}

class SandCounter {
    private val caveMapParser = CaveMapParser()

    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_14_input.txt")
        val coordinateMap = caveMapParser.parseCaveMap(lines)
        val caveMap = CaveMap(coordinateMap, false)
        caveMap.simulateSandFalling()

        return caveMap.getNumberOfRestingSandUnits()
    }

    fun solveSecondPart(): Int {
        val lines = readLinesFromResourceFile("december_14_input.txt")
        val coordinateMap = caveMapParser.parseCaveMap(lines)
        val caveMap = CaveMap(coordinateMap, true)
        caveMap.simulateSandFalling()

        return caveMap.getNumberOfRestingSandUnits()
    }
}