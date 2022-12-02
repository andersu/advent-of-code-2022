package technology.anders

import java.io.File

fun readLinesFromResourceFile(filename: String): List<String> =
    File("src/main/resources/$filename").readLines()