package model

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import parser.CustomerStatementParserXml
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

internal class CustomerStatementParserXmlTest {

    @Test
    fun `Parse with valid xml content`(@TempDir tempDir: Path) {
        val file = tempDir.resolve("file.xml")
        Files.writeString(file,
            """
                <?xml version="1.0"?>
                <records>
                    <record reference="126974">
                        <accountNumber>NL32RABO0195610843</accountNumber>
                        <description>Tickets from Willem King</description>
                        <startBalance>106.67</startBalance>
                        <mutation>+39.73</mutation>
                        <endBalance>146.4</endBalance>
                    </record>
                    <record reference="126501">
                        <accountNumber>NL69ABNA0433647324</accountNumber>
                        <description>Tickets from Jan Dekker</description>
                        <startBalance>5429</startBalance>
                        <mutation>-939</mutation>
                        <endBalance>6368</endBalance>
                    </record>
                </records>
            """.trimIndent())

        val result = CustomerStatementParserXml.parse(file)

        val expected = listOf(
            CustomerStatement(
                126974,
                "NL32RABO0195610843",
                "Tickets from Willem King",
                BigDecimal("106.67"),
                BigDecimal("39.73"),
                BigDecimal("146.4")
            ),
            CustomerStatement(
                126501,
                "NL69ABNA0433647324",
                "Tickets from Jan Dekker",
                BigDecimal("5429"),
                BigDecimal("-939"),
                BigDecimal("6368")
            )
        )

        assertIterableEquals(expected, result)
    }


    @Test
    fun `Parse with invalid xml content`(@TempDir tempDir: Path) {
        val file = tempDir.resolve("file.xml")
        Files.writeString(file,
            """
                <?xml version="1.0"?>
                <records>
                    <record reference="126974">
                        <accountNumber>NL32RABO0195610843</accountNumber>
                        <description>123</description>
                        <startBalance>"106.67"</startBalance>
                        <mutation>+39.73</mutation>
                        <endBalance>146.4</endBalance>
                    </record>
                    <record reference="126501">
                        <accountNumber>NL69ABNA0433647324</accountNumber>
                        <description>Tickets from Jan Dekker</description>
                        <startBalance>5429</startBalance>
                        <mutation>-939</mutation>
                        <endBalance>6368</endBalance>
                    </record>
                </records>
            """.trimIndent())

        assertThrows(InvalidFormatException::class.java) {
            CustomerStatementParserXml.parse(file)
        }
    }
}