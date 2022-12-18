package technology.anders.december14

import kotlin.math.max
import kotlin.math.min

class CaveMapParser {

    fun parseCaveMap(lines: List<String>): Map<Coordinates, CaveMapUnit> {
        val initialRockCoordinates = mutableSetOf<Coordinates>()
        lines.forEach { line ->
            val coordinateStrings = line.split("->").map { it.trim() }
            val coordinatesList = coordinateStrings.map {
                val coordinates = it.split(",")
                Pair(coordinates.first().toInt(), coordinates.last().toInt())
            }

            coordinatesList.windowed(2).forEach {
                val firstX = it.first().first
                val firstY = it.first().second
                val secondX = it.last().first
                val secondY = it.last().second
                if (firstX != secondX) {
                    for (x in min(firstX, secondX)..max(firstX, secondX)) {
                        initialRockCoordinates.add(Coordinates(x, firstY))
                    }
                } else if (firstY != secondY) {
                    for (y in min(firstY, secondY)..max(firstY, secondY)) {
                        initialRockCoordinates.add(Coordinates(firstX, y))
                    }
                }
            }
        }

        val map = HashMap<Coordinates, CaveMapUnit>()
        initialRockCoordinates.forEach { map[it] = CaveMapUnit.ROCK }
        return map
    }
}