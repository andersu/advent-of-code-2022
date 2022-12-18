package technology.anders.december13

class Parser {

    fun parsePacketContent(line: String): List<Any> =
        line.parseValues().first

    private fun String.parseValues(): Pair<List<Any>, Int> {
        val list = mutableListOf<Any>()
        var currentNumber = ""
        var i = 0
        while (i < length) {
            when (val c = this[i]) {
                '[' -> {
                    val innerList = substring(i + 1).parseValues()
                    list.add(innerList.first)
                    i += innerList.second + 1
                }

                ']' -> {
                    if (currentNumber.isNotEmpty()) {
                        list.add(currentNumber.toInt())
                    }
                    return Pair(list, i)
                }

                ',' -> {
                    if (currentNumber.isNotEmpty()) {
                        list.add(currentNumber.toInt())
                        currentNumber = ""
                    }
                }

                else -> {
                    currentNumber += c
                }
            }
            i++
        }
        return Pair(list, i)
    }
}