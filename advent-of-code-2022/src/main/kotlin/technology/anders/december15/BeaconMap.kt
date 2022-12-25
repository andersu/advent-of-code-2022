package technology.anders.december15

import technology.anders.common.Coordinates
import kotlin.math.abs

class BeaconMap(private val sensors: List<Sensor>) {

    private val notDistressBeaconCoordinates = mutableSetOf<Coordinates>()

    private val beaconAndSensorCoordinates =
        sensors.map { it.coordinates } + sensors.map { it.closestBeaconCoordinates }.toSet()

    fun getNumberOfCoordinatesWhereDistressBeaconCannotBeInRow(targetY: Int): Int {
        sensors.forEach { sensor ->
            val manhattanDistance = getManhattanDistance(sensor.closestBeaconCoordinates, sensor.coordinates)
            if (targetY in sensor.coordinates.y - manhattanDistance..sensor.coordinates.y + manhattanDistance) {
                for (x in sensor.coordinates.x - manhattanDistance..sensor.coordinates.x + manhattanDistance) {
                    val coordinates = Coordinates(x, targetY)
                    if (coordinates !in beaconAndSensorCoordinates &&
                        getManhattanDistance(coordinates, sensor.coordinates) <= manhattanDistance
                    ) {
                        notDistressBeaconCoordinates.add(coordinates)
                    }
                }
            }
        }

        return notDistressBeaconCoordinates.count()
    }

    private fun getManhattanDistance(from: Coordinates, to: Coordinates) =
        abs(to.x - from.x) + abs(to.y - from.y)

    enum class MapUnit {
        BEACON,
        SENSOR,
        NOT_BEACON
    }
}