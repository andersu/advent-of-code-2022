package technology.anders

import java.io.File

fun main() {
    ElfCalorieCounter().findMaxCalories()
}

class ElfCalorieCounter {
    fun findMaxCalories() {
        val caloriesPerElfList = mutableListOf<Int>()
        var currentElfCalories = 0

        val lines = readLinesFromResourceFile("december_1_input.txt")
        lines.forEach {
            if (it.isNotBlank()) {
                currentElfCalories += it.toInt()
            } else {
                caloriesPerElfList.add(currentElfCalories)
                currentElfCalories = 0
            }
        }

        println("First answer: ${caloriesPerElfList.max()}")

        println("Second answer: ${caloriesPerElfList.sortedDescending().take(3).sum()}")
    }
}


