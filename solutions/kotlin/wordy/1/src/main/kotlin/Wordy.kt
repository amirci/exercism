object Wordy {
    fun answer(input: String): Int {
        val tokens =
            input
                .removePrefix("What is ")
                .removeSuffix("?")
                .replace("multiplied by", "multiplied")
                .replace("divided by", "divided")
                .replace(POWER_PATTERN, "raised $1")
                .split(" ")

        val first = tokens.firstOrNull()?.toIntOrNull() ?: throw invalidQuestion()

        return evaluate(first, tokens.drop(1))
    }

    private fun evaluate(current: Int, tokens: List<String>): Int = when {
        tokens.isEmpty() -> current
        tokens.size < 2 -> throw invalidQuestion()
        else -> {
            val operator = tokens[0]
            val operand = tokens[1].toIntOrNull() ?: throw invalidQuestion()
            val result = applyOperator(current, operator, operand)

            evaluate(result, tokens.drop(2))
        }
    }

    private fun applyOperator(left: Int, operator: String, right: Int): Int = when (operator) {
        "plus" -> left + right
        "minus" -> left - right
        "multiplied" -> left * right
        "divided" -> left / right
        "raised" -> left.power(right)
        else -> throw invalidQuestion()
    }

    private fun Int.power(exponent: Int): Int = (1..exponent).fold(1) { result, _ -> result * this }

    private fun invalidQuestion() = IllegalArgumentException("Invalid question")

    private val POWER_PATTERN = Regex("raised to the (\\d+)(?:st|nd|rd|th) power")
}
