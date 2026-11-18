class PhoneNumber(input: String) {

    val number: String = clean(input)

    private fun clean(input: String): String {
        requireValidCharacters(input)
        val digits = input.filter(Char::isDigit)
        requireValidCountryCode(input, digits)

        val localNumber = removeCountryCode(digits)
        requireTenDigitsLength(localNumber)
        requireValidAreaCodeStart(localNumber)
        requireValidExchangeCodeStart(localNumber)
        return localNumber
    }

    private fun requireValidCharacters(input: String) {
        require(input.all { it.isDigit() || it.isWhitespace() || it in "().-+" }) {
            "The number ($input) contains unsupported characters."
        }
    }

    private fun requireValidCountryCode(input: String, digits: String) {
        if ('+' in input) {
            require(
                input.trimStart().startsWith("+") && input.count { it == '+' } == 1 &&
                    digits.length == 11 && digits.startsWith("1"),
            ) {
                "The number ($input) must use country code +1."
            }
        }
    }

    private fun removeCountryCode(digits: String): String = when {
        digits.length == 11 && digits.startsWith("1") -> digits.drop(1)
        else -> digits
    }

    private fun requireValidAreaCodeStart(digits: String) {
        require(digits[0] in '2'..'9') { "The area code for number ($digits) must start from 2 to 9." }
    }

    private fun requireValidExchangeCodeStart(digits: String) {
        require(digits[3] in '2'..'9') { "The exchange code for number ($digits) must start from 2 to 9." }
    }

    private fun requireTenDigitsLength(digits: String) {
        require(digits.length == 10) { "The number ($digits) must be exactly 10 digits." }
    }
}
