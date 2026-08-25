private typealias BinaryOperation = (Int, Int) -> Int

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

        val first = parseNumber(tokens.firstOrNull())

        return parseOperations(tokens.drop(1)).fold(first) { current, operation ->
            applyOperator(current, operation.operator, operation.operand)
        }
    }

    private fun parseOperations(tokens: List<String>): List<Operation> {
        if (tokens.size % 2 != 0) {
            throw invalidQuestion()
        }

        return tokens.chunked(2).map { pair ->
            Operation(pair[0], parseNumber(pair[1]))
        }
    }

    private fun applyOperator(left: Int, operator: String, right: Int): Int =
        OPERATIONS[operator]?.invoke(left, right) ?: throw invalidQuestion()

    private fun parseNumber(token: String?): Int = token?.toIntOrNull() ?: throw invalidQuestion()

    private fun Int.power(exponent: Int): Int = (1..exponent).fold(1) { result, _ -> result * this }

    private fun invalidQuestion() = IllegalArgumentException("Invalid question")

    private val POWER_PATTERN = Regex("raised to the (\\d+)(?:st|nd|rd|th) power")
    private val OPERATIONS: Map<String, BinaryOperation> =
        mapOf(
            "plus" to Int::plus,
            "minus" to Int::minus,
            "multiplied" to Int::times,
            "divided" to Int::div,
            "raised" to { left, right -> left.power(right) },
        )

    private data class Operation(val operator: String, val operand: Int)
}
