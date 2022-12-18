package technology.anders.december13

import technology.anders.readLinesFromResourceFile
import java.lang.Integer.max

fun main() {
    val distressSignal = DistressSignal()
    val firstAnswer = distressSignal.solveFirstPart()
    val secondAnswer = distressSignal.solveSecondPart()
    println("First answer: $firstAnswer")
    println("Second answer: $secondAnswer")
}

class DistressSignal {
    private val parser = Parser()
    fun solveFirstPart(): Int {
        val lines = readLinesFromResourceFile("december_13_input.txt")
        val packetPairs = mapLinesToPacketPairs(lines)
        val indicesInRightOrder = packetPairs.mapIndexed { index, packetPair ->
            println()
            println("== Pair ${index + 1} ==")
            val isInRightOrder = packetPair.isInRightOrder()
            if (isInRightOrder) {
                println("    - Inputs are in the right order")
            } else {
                println("    - Inputs are NOT in the right order")
            }

            if (isInRightOrder) index + 1 else 0
        }.filter { it != 0 }

        println("Indices: $indicesInRightOrder")

        return indicesInRightOrder.sum()
    }

    fun solveSecondPart(): Int {
        val lines = readLinesFromResourceFile("december_13_input.txt")
        val dividerPacketLines = listOf("[[2]]", "[[6]]")
        val sortedPackets = (lines + dividerPacketLines)
            .filter { it.isNotBlank() }
            .map { Packet(parser.parsePacketContent(it)) }
            .sortedWith(PacketComparator())

        val firstDividerIndex = sortedPackets.indexOfFirst { it.values == listOf(listOf(2))} + 1
        val secondDividerIndex = sortedPackets.indexOfFirst { it.values == listOf(listOf(6)) } + 1
        return firstDividerIndex * secondDividerIndex
    }

    private fun mapLinesToPacketPairs(lines: List<String>) =
        lines.chunked(3).map {
            val firstListString = it[0]
            val secondListString = it[1]

            PacketPair(
                Packet(parser.parsePacketContent(firstListString)),
                Packet(parser.parsePacketContent(secondListString))
            )
        }
}

data class PacketPair(
    val first: Packet,
    val second: Packet
) {
    fun isInRightOrder(): Boolean =
        PacketComparator().compare(first, second) <= 0
}

class PacketComparator: Comparator<Packet> {
    override fun compare(o1: Packet, o2: Packet): Int {
        return compareValues(o1.values, o2.values)
    }

    private fun compareValues(left: Any, right: Any): Int {
        println("  - Compare $left vs $right")
        return when {
            left is Int && right is Int -> left - right
            left is Int -> compareValues(listOf(left), right)
            right is Int -> compareValues(left, listOf(right))
            else -> {
                left as List<Any>
                right as List<Any>
                for (i in 0.until(max(left.size, right.size))) {
                    if (i >= left.size) {
                        println("    - Left side ran out of items")
                        return -1
                    }
                    if (i >= right.size) {
                        println("    - Right side ran out of items")
                        return 1
                    }
                    val itemCompare = compareValues(left[i], right[i])
                    if (itemCompare != 0) return itemCompare
                }

                return 0
            }
        }
    }
}

data class Packet(
    val values: List<Any>
)