package technology.anders.december12

class DijkstraHillClimber(private val hillMap: HillMap) {

    fun findShortestRoute(fromAnyHeight0: Boolean): Int {
        hillMap.hills[hillMap.end]!!.updateShortestDistance(0)

        return if (fromAnyHeight0) {
            while (!hillMap.hills.values.any { it.height == 0 && it.visited }) {
                climb()
            }
            hillMap.hills.values.filter { it.height == 0 && it.visited }.minOf { it.shortestDistance }
        } else {
            while (!hillMap.hills[hillMap.start]!!.visited) {
                climb()
            }
            hillMap.hills[hillMap.start]!!.shortestDistance
        }
    }

    private fun climb() {
        val nextHillToCheck = hillMap.hills.values.filter { !it.visited }.minBy { it.shortestDistance }
        nextHillToCheck.markAsVisited()

        val currentCoordinates = nextHillToCheck.coordinates

        val reachableNeighbors = currentCoordinates.getNeighbors().filter { coordinates ->
            hillMap.hills[coordinates]!!.height >= hillMap.hills[currentCoordinates]!!.height - 1
                    && coordinates !in hillMap.hills.values.filter { it.visited }.map { it.coordinates }
        }

        reachableNeighbors.forEach {
            val currentShortestPath = hillMap.hills[it]!!.shortestDistance

            val newPathLength = hillMap.hills[currentCoordinates]!!.shortestDistance + 1
            if (newPathLength < currentShortestPath) {
                hillMap.hills[it]!!.updateShortestDistance(newPathLength)
            }
        }
    }

    private fun Coordinates.getNeighbors(): List<Coordinates> {
        val neighbors = mutableListOf<Coordinates>()
        if (x > 0) {
            neighbors.add(Coordinates(x - 1, y))
        }
        if (x < hillMap.columns - 1) {
            neighbors.add(Coordinates(x + 1, y))
        }
        if (y > 0) {
            neighbors.add(Coordinates(x, y - 1))
        }
        if (y < hillMap.rows - 1) {
            neighbors.add(Coordinates(x, y + 1))
        }

        return neighbors
    }
}


