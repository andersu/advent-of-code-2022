package technology.anders

fun main() {
    val deviceSpaceCleaner = DeviceSpaceCleaner()
    deviceSpaceCleaner.solveFirstPart()
    deviceSpaceCleaner.solveSecondPart()
}

class DeviceSpaceCleaner {
    fun solveFirstPart() {
        val lines = readLinesFromResourceFile("december_7_input.txt")
        val fileSystem = mapLinesToFileSystem(lines)
        val directories = fileSystem.getDirectories()

        val targetSize: Long = 100000
        val answer = directories.map { it.size }.filter { it <= targetSize }.sum()
        println("First answer: $answer")
    }

    fun solveSecondPart() {
        val lines = readLinesFromResourceFile("december_7_input.txt")
        val fileSystem = mapLinesToFileSystem(lines)
        val directories = fileSystem.getDirectories()

        val totalDiskSpace = 70_000_000
        val updateSize = 30_000_000
        val spaceToFreeUp = fileSystem.size + updateSize - totalDiskSpace
        val answer = directories.filter { it.size > spaceToFreeUp }.minOfOrNull { it.size }
        println("Second answer: $answer")
    }

    private fun mapLinesToFileSystem(lines: List<String>): FileSystem {
        val fileSystem = FileSystem()

        lines.forEach { line ->
            when {
                line.startsWith("$ cd") -> {
                    val directoryName = line.split(" ").last()
                    fileSystem.changeDirectory(directoryName)
                }
                line == "$ ls" -> {
                    // Do nothing
                }
                else -> {
                    fileSystem.addItem(line)
                }
            }
        }

        return fileSystem
    }
}

class FileSystem {
    private val root = Item.Directory(null, "/")
    private var currentDirectory: Item.Directory = root

    val size get() = root.size

    fun changeDirectory(name: String) {
        if (name == "..") {
            currentDirectory.parent?.let {
                currentDirectory = currentDirectory.parent!!
            }
            return
        } else if (name == "/") {
            currentDirectory = root
            return
        }

        currentDirectory = currentDirectory.getDirectory(name)
    }

    fun getDirectories(): List<Item.Directory> =
        root.getDirectories()

    fun addItem(itemLine: String) = currentDirectory.addItem(itemLine)
}

sealed interface Item {
    val name: String
    val size: Long

    data class Directory(
        val parent: Directory?,
        override val name: String
    ) : Item {
        private val children = mutableListOf<Item>()

        override val size: Long
            get() = children.sumOf { it.size }

        fun getDirectory(name: String): Directory =
            children.filterIsInstance<Directory>().first { it.name == name }

        fun getDirectories(): List<Directory> {
            val directories = mutableListOf<Directory>()
            directories.add(this)
            val childDirectories = children.filterIsInstance<Directory>()
            childDirectories.forEach {
                directories.addAll(it.getDirectories())
            }
            return directories
        }

        fun addItem(itemLine: String) {
            val item = if (itemLine.startsWith("dir")) {
                val directoryName = itemLine.split(" ").last()
                Directory(this, directoryName)
            } else {
                val parts = itemLine.split(" ")
                File(parts.last(), parts.first().toLong())
            }

            children.add(item)
        }
    }

    data class File(
        override val name: String,
        override val size: Long
    ) : Item
}
