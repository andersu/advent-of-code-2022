package technology.anders

fun main() {
    val treeHouse = TreeHouse()
    treeHouse.solveFirstPart()
    treeHouse.solveSecondPert()
}

class TreeHouse {
    fun solveFirstPart() {
        val lines = readLinesFromResourceFile("december_8_input.txt")
        val trees = mapLinesToTrees(lines)

        val numberOfVisibleTrees = trees.count { it.isVisible(trees) }
        println("First answer: $numberOfVisibleTrees")
    }

    fun solveSecondPert() {
        val lines = readLinesFromResourceFile("december_8_input.txt")
        val trees = mapLinesToTrees(lines)

        val bestScenicScore = trees.maxOf { it.calculateScenicScore(trees) }
        println("Second answer: $bestScenicScore")
    }

    private fun mapLinesToTrees(lines: List<String>): MutableList<Tree> {
        val trees = mutableListOf<Tree>()
        lines.forEachIndexed { lineIndex, line ->
            trees.addAll(line.mapIndexed { treeIndex, treeHeight ->
                Tree(treeIndex, lineIndex, treeHeight.digitToInt())
            })
        }
        return trees
    }
}

data class Tree(
    val x: Int,
    val y: Int,
    val height: Int
) {
    fun isVisible(trees: List<Tree>): Boolean {
        val treesAbove = trees.filter { it.x == x && it.y < y }
        val treesBelow = trees.filter { it.x == x && it.y > y }
        val treesToTheLeft = trees.filter { it.y == y && it.x < x }
        val treesToTheRight = trees.filter { it.y == y && it.x > x }

        return isVisibleOverTrees(treesAbove) ||
                isVisibleOverTrees(treesBelow) ||
                isVisibleOverTrees(treesToTheLeft) ||
                isVisibleOverTrees(treesToTheRight)
    }

    private fun isVisibleOverTrees(trees: List<Tree>): Boolean =
        height > (trees.maxOfOrNull { it.height } ?: -1)

    fun calculateScenicScore(trees: List<Tree>): Int {
        val treesAbove = trees.filter { it.x == x && it.y < y }.reversed()
        val treesBelow = trees.filter { it.x == x && it.y > y }
        val treesToTheLeft = trees.filter { it.y == y && it.x < x }.reversed()
        val treesToTheRight = trees.filter { it.y == y && it.x > x }

        return calculateScenicScoreForDirection(treesAbove) *
                calculateScenicScoreForDirection(treesBelow) *
                calculateScenicScoreForDirection(treesToTheLeft) *
                calculateScenicScoreForDirection(treesToTheRight)
    }

    private fun calculateScenicScoreForDirection(trees: List<Tree>): Int {
        var scenicScore = trees.takeWhile {
            it.height < height
        }.count()
        if (scenicScore < trees.size) {
            scenicScore += 1
        }
        return scenicScore
    }
}