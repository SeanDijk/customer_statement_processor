package files

import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FileUtil {
    fun getFileExtention(path: Path): String {
        val filename = path.fileName.toString()
        return filename.substringAfterLast(".", "")
    }


    private val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS")
    private fun createTimeBasedFile(destination: Path, fileName: String) =
        destination.resolve(LocalDateTime.now().format(pattern) + "_" + fileName)

    fun moveWithTimeBasedFilename(fileToMove: Path, destination: Path) {
        val destinationFile = createTimeBasedFile(destination, fileToMove.fileName.toString())
        Files.move(fileToMove, destinationFile)
    }

    fun createFileWithTimeBasedFilename(fileName: String, destination: Path, contents: String) {
        FileWriter(createTimeBasedFile(destination, fileName).toFile()).apply {
            write(contents)
            flush()
            close()
        }
    }
}

