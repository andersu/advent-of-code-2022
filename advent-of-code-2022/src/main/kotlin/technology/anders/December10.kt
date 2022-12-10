package technology.anders

fun main() {
    val cathodeRayTube = CathodeRayTube()
    val firstAnswer = cathodeRayTube.solveFirstPart()
    println("First answer: $firstAnswer")
}

class CathodeRayTube {
    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_10_input.txt")
        val cpu = Cpu()
        lines.forEach {
            when  {
                it == "noop" -> cpu.performNoop()
                it.startsWith("addx") -> {
                    val value = it.split(" ").last().toInt()
                    cpu.performAddX(value)
                }
            }
        }

        println(cpu.xHistory)
        return executeSecretSignalStrengthAlgorithm(cpu.xHistory)
    }

    private fun executeSecretSignalStrengthAlgorithm(xHistory: List<Int>) =
        listOf(20, 60, 100, 140, 180, 220).sumOf { xHistory.getSignalStrength(it) }

    private fun List<Int>.getSignalStrength(index: Int) = index * this[index - 1]
}

class Cpu {
    private var x = 1
    private val _xHistory = mutableListOf(x)
    private var cycle = 0

    val xHistory get() = _xHistory

    fun performNoop() {
        cycle += 1
        _xHistory += x
    }

    fun performAddX(value: Int) {
        cycle += 1
        _xHistory += x
        cycle += 1
        x += value
        _xHistory += x
    }
}