package technology.anders.december12

import technology.anders.common.Coordinates

data class Hill(
    val coordinates: Coordinates,
    val height: Int
) {
    val shortestDistance: Int get() = _shortestDistance
    private var _shortestDistance: Int = Integer.MAX_VALUE

    val visited: Boolean get() = _visited
    private var _visited: Boolean = false

    fun markAsVisited() {
        _visited = true
    }

    fun clearVisited() {
        _visited = false
    }

    fun updateShortestDistance(newShortestDistance: Int) {
        _shortestDistance = newShortestDistance
    }
}
