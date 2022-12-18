package technology.anders.december14

private val sandStartCoordinates = Pair(500, 0)

class CaveMap(
    initialRocks: List<CaveMapCoordinates>
) {
    private val map: MutableList<CaveMapCoordinates> = initialRocks.toMutableList()

    fun simulateSandFalling() {
        while(true) {
            val firstSolid = map.filter { it.x == sandStartCoordinates.first }.minBy { it.y }

            val restingCoordinates = findRestingCoordinates(
                fromX = sandStartCoordinates.first,
                fromY = firstSolid.y - 1
            ) ?: break

            map.add(CaveMapCoordinates(restingCoordinates.first, restingCoordinates.second, CaveMapUnit.SAND))
        }
    }

    fun getNumberOfRestingSandUnits() = map.count { it.unit == CaveMapUnit.SAND }

    private fun findRestingCoordinates(fromX: Int, fromY: Int): Pair<Int, Int>? {
        if (fromY > map.maxOf { it.y }) return null // Sand is falling into the abyss

        val downIsSolid = map.any { it.x == fromX && it.y == fromY + 1 }
        val leftDownIsSolid = map.any { it.x == fromX - 1 && it.y == fromY + 1 }
        val rightDownIsSolid = map.any { it.x == fromX + 1 && it.y == fromY + 1 }

        return when {
            downIsSolid && leftDownIsSolid && rightDownIsSolid -> Pair(fromX, fromY)
            !downIsSolid -> findRestingCoordinates(fromX, fromY + 1)
            !leftDownIsSolid -> findRestingCoordinates(fromX - 1, fromY + 1)
            else -> findRestingCoordinates(fromX + 1, fromY + 1)
        }
    }

}
