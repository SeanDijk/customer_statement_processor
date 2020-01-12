package validation
open class Validator<T> {

    private val rules: MutableList<Rule> = arrayListOf()
    fun addRule(description: String, ruleCheck: (T) -> Boolean): Validator<T> {
        rules.add(Rule(description, ruleCheck))
        return this
    }

    fun validate(toValidate: T): Validation<T> {
        val validatedRules = rules.asSequence()
            .groupBy { rule -> rule.ruleCheck(toValidate) }
            .mapValues { entry -> extractDescriptions(entry) }

        return Validation(
            toValidate,
            validatedRules[true].orEmpty(),
            validatedRules[false].orEmpty()
        )
    }

    private fun extractDescriptions(entry: Map.Entry<Boolean, List<Rule>>) : List<String> {
        return entry.value.asSequence()
            .map { rule -> rule.description }
            .toList()
    }

    private inner class Rule(val description: String,
                     val ruleCheck: (T) -> Boolean)
}