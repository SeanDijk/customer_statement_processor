package files

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.nio.file.Path

internal class FileUtilTest {



    @ParameterizedTest
    @CsvSource(
        "index.html,html",
        "source.csv,csv",
        "source.xml,xml"
        )
    fun getFileExtention(input: String, expected: String) {
        val result = FileUtil.getFileExtention(Path.of(input))
        assertEquals(expected, result)
    }

}