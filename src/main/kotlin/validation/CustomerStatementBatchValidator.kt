package validation

import model.CustomerStatement

class CustomerStatementBatchValidator(private val batch: List<CustomerStatement>){
    private val validator = Validator<CustomerStatement>()
        .addRule("All transaction references should be unique") { toValidate ->
            // todo ask if this should be per batch or for all records that have been validated.
            batch.count { batchValue -> batchValue.reference == toValidate.reference } == 1
        }
        .addRule("The end balance should be the result of the mutation applied to start balance") { toValidate ->
            (toValidate.startBalance + toValidate.mutation).compareTo(toValidate.endBalance) == 0
        }

    fun validate(): List<Validation<CustomerStatement>> {
        return batch.map { statement -> validator.validate(statement) }
    }
}