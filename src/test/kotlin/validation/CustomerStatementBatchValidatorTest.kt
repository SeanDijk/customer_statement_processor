package validation

import model.CustomerStatement

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class CustomerStatementBatchValidatorTest {


    @ParameterizedTest(name = "Validate end balance given: [{arguments}]")
    @CsvSource(
        "100, -10, 90",
        "100, 10, 110",
        "106.67, 39.73, 146.40",
        "106.67, 39.73, 146.4",
        "5429, -939, 4490"
    )
    fun endBalance(startBalance: String, mutation: String, endBalance: String) {
        val customerStatementBatchValidator = CustomerStatementBatchValidator(listOf(
            CustomerStatement(
                123, "abc",
                description = "",
                startBalance = BigDecimal(startBalance),
                mutation = BigDecimal(mutation),
                endBalance = BigDecimal(endBalance)
                )
        ))
        customerStatementBatchValidator.validate().forEach {


        }
        assertTrue(customerStatementBatchValidator.validate().all { it.isValid() })
    }
}