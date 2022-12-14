package technology.anders.december12

data class HillMap(
    val hills: Map<Coordinates, Hill>,
    val start: Coordinates,
    val end: Coordinates
) {
    val rows = hills.maxOf { it.key.y + 1 }
    val columns = hills.maxOf { it.key.x + 1 }

    fun reset() {
        hills.values.forEach { it.clearVisited() }
    }
}