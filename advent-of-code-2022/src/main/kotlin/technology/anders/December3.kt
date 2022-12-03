package technology.anders

fun main() {
    val rucksackPacker = RucksackPacker()
    rucksackPacker.solveFirstPart()
    rucksackPacker.solveSecondPart()
}

class RucksackPacker {

    private val letters = ('a'..'z').toList() + ('A'..'Z').toList()

    fun solveFirstPart() {
        val rucksacks = getRucksacksFromInput()
        val prioritySum = rucksacks.sumOf { rucksack ->
            rucksack.findDuplicateItem()?.let {
                getPriority(it)
            } ?: 0
        }
        println("First answer: $prioritySum")
    }

    fun solveSecondPart() {
        val rucksacks = getRucksacksFromInput()
        val badges = rucksacks.chunked(3).map {
            it.findBadge()
        }
        val prioritySum = badges.sumOf { badge ->
            badge?.let { getPriority(it) } ?: 0
        }
        println("Second answer: $prioritySum")
    }

    private fun getRucksacksFromInput(): List<Rucksack> {
        val lines = readLinesFromResourceFile("december_3_input.txt")
        val rucksacks = lines.map {
            val compartmentContents = it.chunked(it.count() / 2)
            Rucksack(
                firstCompartment = Compartment(compartmentContents.first()),
                secondCompartment = Compartment(compartmentContents.last()),
            )
        }
        return rucksacks
    }

    private fun getPriority(letter: Char): Int =
        letters.indexOf(letter) + 1

    private fun Rucksack.findDuplicateItem(): Char? =
        firstCompartment.content.firstOrNull {
            it in secondCompartment.content
        }

    private fun List<Rucksack>.findBadge(): Char? {
        val allItemsInRucksacks = this.map { it.allItems() }
        val firstRucksackItems = allItemsInRucksacks.first()
        val otherRucksacksItems = allItemsInRucksacks.subList(1, allItemsInRucksacks.count())

        return firstRucksackItems.firstOrNull { item ->
            otherRucksacksItems.all { items -> item in items }
        }
    }

    private fun Rucksack.allItems(): String =
        firstCompartment.content + secondCompartment.content
}

data class Rucksack(
    val firstCompartment: Compartment,
    val secondCompartment: Compartment
)

data class Compartment(
    val content: String
)