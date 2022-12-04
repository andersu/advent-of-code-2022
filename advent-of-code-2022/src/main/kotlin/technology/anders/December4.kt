package technology.anders

fun main() {
    val campCleaner = CampCleaner()
    campCleaner.solveFirstPart()
    campCleaner.solveSecondPart()
}

class CampCleaner {

    fun solveFirstPart() {
        val elfPairs = getElfPairsFromInput()
        val numberOfPairsWithCompleteOverlaps = elfPairs.count {
            (it.secondElfSections - it.firstElfSections).isEmpty() ||
                    (it.firstElfSections - it.secondElfSections).isEmpty()
        }
        println("First answer: $numberOfPairsWithCompleteOverlaps")
    }

    fun solveSecondPart() {
        val elfPairs = getElfPairsFromInput()
        val numberOfPairsWithAnyOverlap = elfPairs.count {
            it.firstElfSections.intersect(it.secondElfSections).isNotEmpty()
        }
        println("Second answer: $numberOfPairsWithAnyOverlap")
    }

    private fun getElfPairsFromInput(): List<ElfPair> {
        val lines = readLinesFromResourceFile("december_4_input.txt")
        val elfPairs = lines.map {
            val rangeStrings = it.split(",")
            if (rangeStrings.count() != 2) {
                throw IllegalArgumentException(
                    "Each line in the input must be a pair of ranges separated by a comma. Example: 1-2,3-4"
                )
            }
            ElfPair(
                firstElfSections = mapRangeStringToRange(rangeStrings.first()),
                secondElfSections = mapRangeStringToRange(rangeStrings.last()),
            )
        }
        return elfPairs
    }

    private fun mapRangeStringToRange(rangeString: String): IntRange {
        val edgeValues = rangeString.split("-")
        if (edgeValues.count() != 2) {
            throw IllegalArgumentException("Range string must have format x-y where x and y are integers")
        }

        return edgeValues.first().toInt()..edgeValues.last().toInt()
    }
}

data class ElfPair(
    val firstElfSections: IntRange,
    val secondElfSections: IntRange
)