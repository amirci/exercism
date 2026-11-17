class PhoneNumber(input: String) {

    val number: String = clean(input)

    private fun clean(input: String): String {
        require(input.all(::isAllowedCharacter))

        val digits = input.filter { it in '0'..'9' }
        val hasPlusPrefix = '+' in input
        if (hasPlusPrefix) {
            require(input.trimStart().startsWith("+") && input.count { it == '+' } == 1)
            require(digits.length == 11 && digits.startsWith("1"))
        }

        val localNumber = when (digits.length) {
            10 -> digits
            11 if digits.startsWith("1") -> digits.drop(1)
            else -> throw IllegalArgumentException("Phone number must have ten digits, optionally prefixed with 1")
        }

        require(PATTERN.matches(localNumber)) { "Area and exchange codes must start with 2 through 9" }
        return localNumber
    }

    private fun isAllowedCharacter(character: Char): Boolean =
        character in '0'..'9' || character.isWhitespace() || character in "().-+"

    private companion object {
        val PATTERN = Regex("[2-9][0-9]{2}[2-9][0-9]{6}")
    }
}
