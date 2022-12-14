package technology.anders.december12

class HillMapMapper {
    private val elevations = ('a'..'z').toList()

    fun mapLinesToHillMap(lines: List<String>): HillMap {
        var startCoordinates = Coordinates(-1, -1)
        var endCoordinates = Coordinates(-1, -1)
        val hills = HashMap<Coordinates, Hill>()
        lines.forEachIndexed { yIndex, line ->
            line.forEachIndexed { xIndex, letter ->
                val coordinates = Coordinates(xIndex, yIndex)
                val height = when (letter) {
                    'S' -> {
                        startCoordinates = coordinates
                        elevations.indexOf('a')
                    }

                    'E' -> {
                        endCoordinates = coordinates
                        elevations.indexOf('z')
                    }

                    else -> elevations.indexOf(letter)
                }

                hills[coordinates] = Hill(coordinates, height)
            }
        }
        return HillMap(
            hills,
            startCoordinates,
            endCoordinates
        )
    }
}