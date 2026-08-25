object PigLatin {
    fun translate(phrase: String): String =
        phrase
            .split(" ")
            .joinToString(" ", transform = ::translateWord)

    private fun translateWord(word: String): String =
        if (startsWithVowelSound(word)) {
            "${word}ay"
        } else {
            translateConsonantWord(word)
        }

    private fun translateConsonantWord(word: String): String {
        val split = word.indices.first { index -> isClusterEnd(word, index) }
        val clusterEnd = if (isQu(word, split)) split + 2 else split

        return "${word.drop(clusterEnd)}${word.take(clusterEnd)}ay"
    }

    private fun startsWithVowelSound(word: String): Boolean =
        word.first() in VOWELS || word.startsWith("xr") || word.startsWith("yt")

    private fun isClusterEnd(word: String, index: Int,): Boolean =
        index > 0 && word[index] == 'y' || word[index] in VOWELS || isQu(word, index)

    private fun isQu(word: String, index: Int,): Boolean =
        word[index] == 'q' && word.getOrNull(index + 1) == 'u'

    private val VOWELS = setOf('a', 'e', 'i', 'o', 'u')
}
