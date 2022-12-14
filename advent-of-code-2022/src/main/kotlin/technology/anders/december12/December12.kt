package technology.anders.december12

import technology.anders.readLinesFromResourceFile

fun main() {
    val start = System.currentTimeMillis()
    val firstAnswer = solveFirstPart()
    val end = System.currentTimeMillis()
    println("${end - start} ms")
    println("First answer: $firstAnswer")
}

private fun solveFirstPart(): Int {
    val lines = readLinesFromResourceFile("december_12_input.txt")
    val hillMapMapper = HillMapMapper()
    val hillMap = hillMapMapper.mapLinesToHillMap(lines)

    val hillClimber = DijkstraHillClimber(hillMap)
    return hillClimber.findShortestRoute()
}

