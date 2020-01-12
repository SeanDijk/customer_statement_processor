package parser

import com.fasterxml.jackson.core.JsonParseException
import model.CustomerStatement
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

internal class CustomerStatementParserCsvTest {

    @Test
    fun `Parse with valid csv content`(@TempDir tempDir: Path) {
        val file = tempDir.resolve("file.csv")
        Files.writeString(
            file,
            """
Reference,Account Number,Description,Start Balance,Mutation,End Balance
185633,NL90ABNA0585647886,Tickets from Jan Bakker,108.45,-22.74,85.71
112806,NL93ABNA0585619023,Subscription for Willem King,24.76,-19.96,4.8
            """.trimIndent()
        )

        val result = CustomerStatementParserCsv.parse(file)

        val expected = listOf(
            CustomerStatement(
                185633,
                "NL90ABNA0585647886",
                "Tickets from Jan Bakker",
                BigDecimal("108.45"),
                BigDecimal("-22.74"),
                BigDecimal("85.71")
            ),
            CustomerStatement(
                112806,
                "NL93ABNA0585619023",
                "Subscription for Willem King",
                BigDecimal("24.76"),
                BigDecimal("-19.96"),
                BigDecimal("4.8")
            )
        )

        assertIterableEquals(expected, result)
    }


    @Test
    fun `Parse with invalid csv content`(@TempDir tempDir: Path) {
        val file = tempDir.resolve("file.csv")
        Files.writeString(
            file,
            """
Reference,Account Number,Description,Start Balance,Mutation,,End Balance
185633,NL90ABNA0585647886,,Tickets from Jan Bakker,108.45,-22.74,85.71
112806,NL93ABNA0585619023,Subscription for Willem King,24.76,-19.96,4.8
            """.trimIndent()
        )

        assertThrows(JsonParseException::class.java) {
            CustomerStatementParserXml.parse(file)
        }
    }
}