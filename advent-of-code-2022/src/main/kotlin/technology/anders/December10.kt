package technology.anders

fun main() {
    val cathodeRayTubeController = CathodeRayTubeController()
    cathodeRayTubeController.solveFirstPart()

}

class CathodeRayTubeController {
    fun solveFirstPart() {
        val lines = readLinesFromResourceFile("december_10_input.txt")
        val cathodeRayTube = CathodeRayTube()
        lines.forEach {
            when {
                it == "noop" -> cathodeRayTube.performNoop()
                it.startsWith("addx") -> {
                    val value = it.split(" ").last().toInt()
                    cathodeRayTube.performAddX(value)
                }
            }
        }

        val firstAnswer = executeSecretSignalStrengthAlgorithm(cathodeRayTube.xHistory)
        println("First answer: $firstAnswer")
        cathodeRayTube.printScreen()
    }

    private fun executeSecretSignalStrengthAlgorithm(xHistory: List<Int>) =
        listOf(20, 60, 100, 140, 180, 220).sumOf { xHistory.getSignalStrength(it) }

    private fun List<Int>.getSignalStrength(index: Int) = index * this[index - 1]
}

class CathodeRayTube {
    private var x = 1
    private val _xHistory = mutableListOf(x)
    private var cycle = 0

    private var pixels = MutableList(6 * 40) { '.' }

    val xHistory get() = _xHistory

    fun performNoop() {
        performCycle()
        _xHistory += x
    }

    fun performAddX(value: Int) {
        performCycle()
        _xHistory += x
        performCycle()
        x += value
        _xHistory += x
    }

    private fun performCycle() {
        drawPixel()
        cycle += 1
    }

    private fun drawPixel() {
        if (cycle % 40 in x-1..x+1) {
            pixels[cycle] = '#'
        }
    }

    fun printScreen() {
        println()
        pixels.chunked(40).forEach {
            println (it.joinToString(" "))
        }
    }
}