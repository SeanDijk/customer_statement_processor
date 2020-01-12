package parser

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import model.CustomerStatement
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Path

object CustomerStatementParserCsv : CustomerStatementParser {
    private val csvMapper: CsvMapper = CsvMapper()
        .registerModule(KotlinModule())
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        .setPropertyNamingStrategy(
            // Custom naming stratagy to filter out the spaces.
            object : PropertyNamingStrategy.PropertyNamingStrategyBase() {
                override fun translate(input: String?): String? {
                    return input?.filterNot { it == ' ' }
                }
            }
        ) as CsvMapper

    private val schema = csvMapper.typedSchemaFor(CustomerStatement::class.java)
        .withHeader()

    override fun parse(path: Path): List<CustomerStatement> {
        return csvMapper.readerFor(CustomerStatement::class.java).with(schema)
            .readValues<CustomerStatement>(BufferedReader(InputStreamReader(FileInputStream(path.toFile()), "ISO-8859-1")))
            .readAll()
    }
}