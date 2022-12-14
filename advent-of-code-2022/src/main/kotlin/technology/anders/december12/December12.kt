package technology.anders.december12

import technology.anders.readLinesFromResourceFile

fun main() {
    val firstAnswer = solveFirstPart()
    println("First answer: $firstAnswer")
    val secondAnswer = solveSecondPart()
    println("Second answer: $secondAnswer")
}

private fun solveFirstPart(): Int {
    val lines = readLinesFromResourceFile("december_12_input.txt")
    val hillMapMapper = HillMapMapper()
    val hillMap = hillMapMapper.mapLinesToHillMap(lines)

    val hillClimber = DijkstraHillClimber(hillMap)
    return hillClimber.findShortestRoute(fromAnyHeight0 = false)
}

private fun solveSecondPart(): Int {
    val lines = readLinesFromResourceFile("december_12_input.txt")
    val hillMapMapper = HillMapMapper()
    val hillMap = hillMapMapper.mapLinesToHillMap(lines)

    val hillClimber = DijkstraHillClimber(hillMap)
    return hillClimber.findShortestRoute(fromAnyHeight0 = true)
}

