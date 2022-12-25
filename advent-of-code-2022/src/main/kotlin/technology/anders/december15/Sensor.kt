package technology.anders.december15

import technology.anders.common.Coordinates

data class Sensor(
    val coordinates: Coordinates,
    val closestBeaconCoordinates: Coordinates
)
