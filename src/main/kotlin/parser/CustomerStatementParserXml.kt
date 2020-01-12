package parser

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import model.CustomerStatement
import java.nio.file.Path

object CustomerStatementParserXml : CustomerStatementParser {
    private val typeReference = object : TypeReference<List<CustomerStatement>>() {}
    private val xmlMapper = XmlMapper().registerModule(KotlinModule())

    override fun parse(path: Path): List<CustomerStatement> {
        return xmlMapper.readValue(path.toFile(), typeReference)
    }
}