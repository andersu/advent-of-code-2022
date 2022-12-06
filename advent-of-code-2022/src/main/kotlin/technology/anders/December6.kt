package technology.anders

fun main() {
    val dataStreamDecoder = DataStreamDecoder()
    dataStreamDecoder.solveFirstPart()
    dataStreamDecoder.solveSecondPart()
}

class DataStreamDecoder {
    fun solveFirstPart() {
        val dataStream = readLinesFromResourceFile("december_6_input.txt").first()
        val markerLength = 4

        val index = findIndexAfterFirstMarker(dataStream, markerLength)
        println("First answer: $index")
    }

    fun solveSecondPart() {
        val dataStream = readLinesFromResourceFile("december_6_input.txt").first()
        val markerLength = 14

        val index = findIndexAfterFirstMarker(dataStream, markerLength)
        println("Second answer: $index")
    }

    private fun findIndexAfterFirstMarker(dataStream: String, markerLength: Int): Int =
        dataStream.windowed(markerLength).indexOfFirst {
            it.toSet().size == markerLength
        } + markerLength
}