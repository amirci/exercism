object MatchingBrackets {
    fun isValid(input: String): Boolean {
        val openBrackets = BracketStack()

        val allMatched = input.all { character ->
            when (character) {
                in BRACKET_PAIRS.keys -> openBrackets.addOpenBracket(character)
                in BRACKET_PAIRS.values -> openBrackets.closesLastOpenBracket(character)
                else -> true
            }
        }

        return allMatched && openBrackets.isEmpty()
    }

    private val BRACKET_PAIRS = mapOf('(' to ')', '[' to ']', '{' to '}')

    private fun BracketStack.addOpenBracket(character: Char): Boolean {
        addLast(character)
        return true
    }

    private fun BracketStack.closesLastOpenBracket(character: Char): Boolean {
        val lastOpenBracket = removeLastOrNull()
        return BRACKET_PAIRS[lastOpenBracket] == character
    }
}

private typealias BracketStack = ArrayDeque<Char>
