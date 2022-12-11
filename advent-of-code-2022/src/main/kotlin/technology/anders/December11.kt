package technology.anders

fun main() {
    val monkeyBusiness = MonkeyBusiness()
    val firstAnswer = monkeyBusiness.solveFirstPart()
    val secondAnswer = monkeyBusiness.solveSecondPart()
    println("First answer: $firstAnswer")
    println("Second answer: $secondAnswer")
}

class MonkeyBusiness {
    private val monkeyMapper = MonkeyMapper()

    fun solveFirstPart(): Long {
        val lines = readLinesFromResourceFile("december_11_input.txt")
        val monkeys = monkeyMapper.mapLinesToMonkeys(lines)

        val keepAway = KeepAway(
            monkeys = monkeys,
            rounds = 20,
            calculateNewWorryLevel = { it / 3 }
        )
        keepAway.startGame()

        return calculateLevelOfMonkeyBusiness(keepAway.monkeys)
    }

    fun solveSecondPart(): Long {
        val lines = readLinesFromResourceFile("december_11_input.txt")
        val monkeys = monkeyMapper.mapLinesToMonkeys(lines)

        val worryNormalizer = WorryNormalizer(monkeys)
        val keepAway = KeepAway(
            monkeys = monkeys,
            rounds = 10_000,
            calculateNewWorryLevel = { worryNormalizer.normalizeWorryLevel(it) }
        )
        keepAway.startGame()

        return calculateLevelOfMonkeyBusiness(keepAway.monkeys)
    }

    private fun calculateLevelOfMonkeyBusiness(monkeys: List<Monkey>): Long =
        monkeys.map { it.activity }
            .sortedDescending()
            .take(2)
            .reduce { acc, activity -> acc * activity }

    class Monkey(
        startingItems: List<Item>,
        val operation: (Long) -> Long,
        val test: Test
    ) {
        private val _items = startingItems.toMutableList()
        val items: List<Item> get() = _items

        private var _activity = 0L
        val activity: Long get() = _activity

        fun inspectItems(calculateNewWorryLevel: (worryLevel: Long) -> Long) {
            _items.forEachIndexed { index, item ->
                _items[index] = item.copy(worryLevel = calculateNewWorryLevel(operation(item.worryLevel)))
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

    data class Item(val worryLevel: Long)

    data class Test(
        val divisibleBy: Int,
        val throwToMonkeyIfTrue: Int,
        val throwToMonkeyIfFalse: Int
    ) {
        fun calculateMonkeyToThrowTo(item: Item) =
            if (item.worryLevel % divisibleBy == 0L) {
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
                MonkeyBusiness.Item(it.trim().toLong())
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

    private fun mapLineToOperation(line: String): (Long) -> Long {
        val expression = line.split(":").last().trim()
        val rightSide = expression.split("=").last().trim()
        val elements = rightSide.split(" ")
        val firstElement = elements[0]
        val operator = elements[1]
        val secondElement = elements[2]

        return if (operator == "+") {
            {
                mapElementToValue(firstElement, it) + mapElementToValue(secondElement, it)
            }
        } else {
            {
                mapElementToValue(firstElement, it) * mapElementToValue(secondElement, it)
            }
        }
    }

    private fun mapElementToValue(element: String, oldValue: Long): Long =
        if (element == "old") oldValue else element.toLong()

    private fun String.getLastNumber(): Int =
        split(" ").last().toInt()
}

class KeepAway(
    monkeys: List<MonkeyBusiness.Monkey>,
    private val rounds: Int,
    private val calculateNewWorryLevel: (worryLevel: Long) -> Long
) {
    private val _monkeys = monkeys.toMutableList()
    val monkeys: List<MonkeyBusiness.Monkey> get() = _monkeys

    fun startGame() {
        repeat(rounds) { round ->
            monkeys.forEach { monkey ->
                monkey.inspectItems(calculateNewWorryLevel)

                monkey.items.forEach { item ->
                    val monkeyToThrowTo = monkey.test.calculateMonkeyToThrowTo(item)
                    monkeys[monkeyToThrowTo].addItem(item)
                }
                monkey.throwAwayItems()
            }

            if ((round + 1) in listOf(1, 20) || (round + 1) % 1000 == 0) {
                println("== After round ${round + 1} ==")
                monkeys.forEachIndexed { index, monkey ->
                    println("Monkey $index inspected items ${monkey.activity} times.")
                }
                println()
            }
        }
    }
}

class WorryNormalizer(monkeys: List<MonkeyBusiness.Monkey>) {
    private val commonDenominator = monkeys.map { it.test.divisibleBy }.reduce { acc, divisibleBy ->
        acc * divisibleBy
    }

    fun normalizeWorryLevel(worryLevel: Long): Long {
        return worryLevel % commonDenominator
    }
}