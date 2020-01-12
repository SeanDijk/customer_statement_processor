package validation

class Validation<T>(val validated: T,
                    val validRules: List<String>,
                    val invalidRules: List<String>) {

    fun isValid() = invalidRules.isEmpty();
    fun isNotValid() = !isValid();
}