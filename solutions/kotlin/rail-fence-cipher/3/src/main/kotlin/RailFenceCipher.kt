class RailFenceCipher(private val numberOfRails: Int) {
    init {
        require(numberOfRails > 0)
    }

    fun getEncryptedData(letters: String): String {
        val rails = Array(numberOfRails) { StringBuilder() }
        var rail = 0
        var direction = 1

        for (letter in letters) {
            rails[rail].append(letter)
            if (rail == 0) direction = 1
            if (rail == numberOfRails - 1) direction = -1
            rail += direction
        }

        return rails.joinToString("")
    }

    fun getDecryptedData(letters: String): String {
        val railCounts = IntArray(numberOfRails)
        var rail = 0
        var direction = 1

        repeat(letters.length) {
            railCounts[rail]++
            if (rail == 0) direction = 1
            if (rail == numberOfRails - 1) direction = -1
            rail += direction
        }

        val rails = Array(numberOfRails) { StringBuilder() }
        var offset = 0
        for (index in rails.indices) {
            rails[index].append(letters.substring(offset, offset + railCounts[index]))
            offset += railCounts[index]
        }

        val railOffsets = IntArray(numberOfRails)
        val decoded = StringBuilder(letters.length)
        rail = 0
        direction = 1

        repeat(letters.length) {
            decoded.append(rails[rail][railOffsets[rail]++])
            if (rail == 0) direction = 1
            if (rail == numberOfRails - 1) direction = -1
            rail += direction
        }

        return decoded.toString()
    }
}
