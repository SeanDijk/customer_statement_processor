package model

import java.math.BigDecimal

data class CustomerStatement(
    val reference: Long,
    val accountNumber: String,
    val description: String,
    val startBalance: BigDecimal,
    val mutation: BigDecimal,
    val endBalance: BigDecimal
)