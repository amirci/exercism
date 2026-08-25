private const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"
private const val DEFAULT_KEY_LENGTH = 100

class Cipher(val key: String = randomKey()) {
    init {
        require(key.isNotEmpty() && key.all { it in ALPHABET })
    }

    fun encode(s: String): String = translate(s, Int::plus)

    fun decode(s: String): String = translate(s, Int::minus)

    private fun translate(input: String, shift: (Int, Int) -> Int): String = input.mapIndexed { index, character ->
        val characterIndex = ALPHABET.indexOf(character)
        val keyIndex = ALPHABET.indexOf(key[index % key.length])
        ALPHABET.floorModIndex(shift(characterIndex, keyIndex))
    }.joinToString("")

    private fun String.floorModIndex(index: Int): Char = this[Math.floorMod(index, length)]
}

private fun randomKey(): String = List(DEFAULT_KEY_LENGTH) { ALPHABET.random() }.joinToString("")
