private data class ParsingState(val digits: String, val remaining: String)

private class PhoneNumberParser(input: String) {
    private val initialState = ParsingState(digits = "", remaining = input.trim())

    fun parse(): String? = parseCountryCode(initialState)
        ?.let(::parseAreaCode)
        ?.let(::parseExchangeCode)
        ?.let(::parseSubscriberNumber)
        ?.takeIf { it.remaining.isEmpty() }
        ?.digits

    private fun parseCountryCode(state: ParsingState): ParsingState? {
        val match = countryCodePattern.matchAt(state.remaining, 0) ?: return state
        if (match.groups[2] != null) return null
        return state.copy(remaining = state.remaining.drop(match.range.last + 1))
    }

    private fun parseAreaCode(state: ParsingState): ParsingState? {
        val match = areaCodePattern.matchAt(state.remaining, 0) ?: return null
        val areaCode = match.groups[1]?.value ?: match.groups[2]?.value
        return state.copy(
            digits = state.digits + areaCode,
            remaining = state.remaining.drop(match.range.last + 1),
        )
    }

    private fun parseExchangeCode(state: ParsingState): ParsingState? = parseDigitGroup(state, exchangeCodePattern)

    private fun parseSubscriberNumber(state: ParsingState): ParsingState? =
        parseDigitGroup(state, subscriberNumberPattern)

    private fun parseDigitGroup(state: ParsingState, pattern: Regex): ParsingState? {
        val match = pattern.matchAt(state.remaining, 0) ?: return null
        val digits = match.groups[1]?.value ?: return null
        return state.copy(
            digits = state.digits + digits,
            remaining = state.remaining.drop(match.range.last + 1),
        )
    }

    private companion object {
        val countryCodePattern = Regex("""(\+1|1)[\s.-]*|(\+)""")
        val areaCodePattern = Regex("""\(([2-9]\d{2})\)|([2-9]\d{2})""")
        val exchangeCodePattern = Regex("""[\s.-]*([2-9]\d{2})""")
        val subscriberNumberPattern = Regex("""[\s.-]*(\d{4})""")
    }
}

class PhoneNumber(input: String) {
    val number: String = PhoneNumberParser(input).parse()
        ?: throw IllegalArgumentException("Invalid phone number: $input")
}
