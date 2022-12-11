package technology.anders

fun main() {
    val monkeyBusiness = MonkeyBusiness()
    monkeyBusiness.solveFirstPart()
}

class MonkeyBusiness {
    private val monkeyMapper = MonkeyMapper()

    fun solveFirstPart() {
        val lines = readLinesFromResourceFile("december_11_input.txt")
        val monkeys = monkeyMapper.mapLinesToMonkeys(lines)
        println(monkeys)
        val keepAway = KeepAway(monkeys, 20)
        keepAway.startGame()
        val activity =
            keepAway.monkeys.map { it.activity }
                .sortedDescending()
                .take(2)
                .reduce { acc, activity -> acc * activity }
        println("First answer: $activity")
    }

    class Monkey(
        startingItems: List<Item>,
        val operation: (Int) -> Int,
        val test: Test
    ) {
        private val _items = startingItems.toMutableList()
        val items: List<Item> get() = _items

        private var _activity = 0
        val activity: Int get() = _activity

        fun inspectItems() {
            _items.forEachIndexed { index, item ->
                val newWorryLevel = operation(item.worryLevel) / 3
                _items[index] = item.copy(worryLevel = newWorryLevel)
                _activity++
            }
        }

        fun throwAwayItems() {
            _items.clear()
        }

        fun addItem(item: Item) {
            _items += item
        }
    }

    data class Item(val worryLevel: Int)

    data class Test(
        val divisibleBy: Int,
        val throwToMonkeyIfTrue: Int,
        val throwToMonkeyIfFalse: Int
    ) {
        fun calculateMonkeyToThrowTo(item: Item) =
            if (item.worryLevel % divisibleBy == 0) {
                throwToMonkeyIfTrue
            } else {
                throwToMonkeyIfFalse
            }
    }
}

class MonkeyMapper {
    fun mapLinesToMonkeys(lines: List<String>) =
        lines.filter { it.isNotBlank() }.chunked(6).map { monkeyChunk ->
            MonkeyBusiness.Monkey(
                startingItems = mapLineToStartingItems(monkeyChunk[1]),
                operation = mapLineToOperation(monkeyChunk[2]),
                test = mapLinesToTest(monkeyChunk.subList(3, 6))
            )
        }

    private fun mapLineToStartingItems(line: String): List<MonkeyBusiness.Item> =
        line.split(":")
            .last()
            .replace(",", "")
            .split(" ")
            .filter {
                it.isNotBlank()
            }
            .map {
                MonkeyBusiness.Item(it.trim().toInt())
            }

    private fun mapLinesToTest(lines: List<String>): MonkeyBusiness.Test {
        val divisibleBy = lines[0].getLastNumber()
        val throwToMonkeyIfTrue = lines[1].getLastNumber()
        val throwToMonkeyIfFalse = lines[2].getLastNumber()
        return MonkeyBusiness.Test(
            divisibleBy,
            throwToMonkeyIfTrue,
            throwToMonkeyIfFalse

        )
    }

    private fun mapLineToOperation(line: String): (Int) -> Int {
        val expression = line.split(":").last().trim()
        val rightSide = expression.split("=").last().trim()
        val elements = rightSide.split(" ")
        val firstElement = elements[0]
        val operator = elements[1]
        val secondElement = elements[2]

        return if (operator == "+") {
            { mapElementToValue(firstElement, it) + mapElementToValue(secondElement, it) }
        } else {
            { mapElementToValue(firstElement, it) * mapElementToValue(secondElement, it) }
        }
    }

    private fun mapElementToValue(element: String, oldValue: Int): Int =
        if (element == "old") oldValue else element.toInt()

    private fun String.getLastNumber(): Int =
        split(" ").last().toInt()
}

class KeepAway(
    monkeys: List<MonkeyBusiness.Monkey>,
    private val rounds: Int
) {
    private val _monkeys = monkeys.toMutableList()
    val monkeys: List<MonkeyBusiness.Monkey> get() = _monkeys

    fun startGame() {
        repeat(rounds) { round ->
            monkeys.forEachIndexed { index, monkey ->
                monkey.inspectItems()
                monkey.items.forEach { item ->
                    val monkeyToThrowTo = monkey.test.calculateMonkeyToThrowTo(item)
                    monkeys[monkeyToThrowTo].addItem(item)
                }
                monkey.throwAwayItems()
            }
            println("${round + 1}:")
            monkeys.forEachIndexed { index, monkey ->
                println("Monkey $index: ${monkey.items.map { it.worryLevel }}")
            }
        }
    }
}