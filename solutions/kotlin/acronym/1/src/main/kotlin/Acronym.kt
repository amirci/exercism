object Acronym {
    fun generate(phrase: String): String = WORD.findAll(phrase.withoutPunctuation())
        .map { match -> match.value.first().uppercase() }
        .joinToString("")

    private val WORD = Regex("[A-Za-z]+")

    private fun String.withoutPunctuation(): String = replace("-", " ").replace(Regex("[^A-Za-z\\s]"), "")
}
