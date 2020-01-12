package parser

import files.FileUtil
import model.CustomerStatement
import java.nio.file.Path

interface CustomerStatementParser {
    fun parse(path: Path): List<CustomerStatement>

    companion object {
        fun getFor(path: Path): CustomerStatementParser? {
            return when (FileUtil.getFileExtention(path).toLowerCase()) {
                "xml" -> CustomerStatementParserXml
                "csv" -> CustomerStatementParserCsv
                else -> null
            }
        }
    }
}
