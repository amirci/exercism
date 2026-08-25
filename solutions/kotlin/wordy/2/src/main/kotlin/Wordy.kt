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

        return parseOperations(tokens.drop(1)).fold(first) { current, operation ->
            applyOperator(current, operation.operator, operation.operand)
        }
    }

    private fun parseOperations(tokens: List<String>): List<Operation> {
        if (tokens.size % 2 != 0) {
            throw invalidQuestion()
        }

        return tokens.chunked(2).map { pair ->
            val operand = pair[1].toIntOrNull() ?: throw invalidQuestion()
            Operation(pair[0], operand)
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

    private data class Operation(val operator: String, val operand: Int)
}
