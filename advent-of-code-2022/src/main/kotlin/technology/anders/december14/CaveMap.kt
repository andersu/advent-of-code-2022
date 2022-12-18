package technology.anders.december14

private val sandStartCoordinates = Pair(500, 0)

class CaveMap(
    initialRocks: Map<Coordinates, CaveMapUnit>,
    private val withFloor: Boolean
) {
    private val solidCoordinatesMap: MutableMap<Coordinates, CaveMapUnit> = initialRocks.toMutableMap()

    private val floorY = initialRocks.keys.maxOf { it.y } + 2

    fun simulateSandFalling() {
        while (true) {
            val restingCoordinates = findRestingCoordinates(
                fromX = sandStartCoordinates.first,
                fromY = sandStartCoordinates.second
            ) ?: break

            val restingSandCoordinates = Coordinates(restingCoordinates.first, restingCoordinates.second)
            solidCoordinatesMap[restingSandCoordinates] = CaveMapUnit.SAND
            if (restingCoordinates == sandStartCoordinates) break
        }
    }

    fun getNumberOfRestingSandUnits() = solidCoordinatesMap.values.count { it == CaveMapUnit.SAND }

    private fun findRestingCoordinates(fromX: Int, fromY: Int): Pair<Int, Int>? {
        if (!withFloor && fromY > floorY - 2) return null // Sand is falling into the abyss

        if (withFloor && fromY == floorY - 1) return Pair(fromX, fromY)

        val downIsSolid = solidCoordinatesMap[Coordinates(fromX, fromY + 1)] != null
        val leftDownIsSolid = solidCoordinatesMap[Coordinates(fromX - 1, fromY + 1)] != null
        val rightDownIsSolid = solidCoordinatesMap[Coordinates(fromX + 1, fromY + 1)] != null

        return when {
            downIsSolid && leftDownIsSolid && rightDownIsSolid -> Pair(fromX, fromY)
            !downIsSolid -> findRestingCoordinates(fromX, fromY + 1)
            !leftDownIsSolid -> findRestingCoordinates(fromX - 1, fromY + 1)
            else -> findRestingCoordinates(fromX + 1, fromY + 1)
        }
    }
}
