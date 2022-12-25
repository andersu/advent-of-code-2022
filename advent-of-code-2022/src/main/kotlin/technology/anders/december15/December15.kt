package technology.anders.december15

import technology.anders.common.Coordinates
import technology.anders.readLinesFromResourceFile

fun main() {
    val beaconExclusionZone = BeaconExclusionZone()
    val firstAnswer = beaconExclusionZone.solveFirstPart()
    println("First answer: $firstAnswer")
}

class BeaconExclusionZone {

    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_15_input.txt")
        val sensors = lines.map { line ->
            val regex = Regex("x=-?\\d+, y=-?\\d+")
            val coordinatesStrings =
                regex.findAll(line).map { matchResult -> matchResult.value.filterNot { it in "xy= " } }
            val sensorCoordinates = coordinatesStrings.first().split(",")
            val closestBeaconCoordinates = coordinatesStrings.last().split(",")
            Sensor(
                coordinates = Coordinates(
                    sensorCoordinates.first().toInt(),
                    sensorCoordinates.last().toInt()
                ),
                closestBeaconCoordinates = Coordinates(
                    closestBeaconCoordinates.first().toInt(),
                    closestBeaconCoordinates.last().toInt()
                )
            )
        }
        val beaconMap = BeaconMap(sensors)

        return beaconMap.getNumberOfCoordinatesWhereDistressBeaconCannotBeInRow(2000000)
    }
}