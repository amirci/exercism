typealias RailIndex = Int
typealias RailLetter = Pair<RailIndex, Char>

private val RailLetter.railIndex: RailIndex
    get() = first

private val RailLetter.letter: Char
    get() = second

class RailFenceCipher(private val numberOfRails: Int) {
    init {
        require(numberOfRails > 0)
    }

    private fun directionFromRail(rail: RailIndex, currentDirection: Int): Int = when (rail) {
        0 -> 1
        numberOfRails - 1 -> -1
        else -> currentDirection
    }

    private fun zigZagNumbers(): Sequence<RailIndex> = sequence {
        var railIndex = 0
        var direction = 1

        while (true) {
            yield(railIndex)
            direction = directionFromRail(railIndex, direction)
            railIndex += direction
        }
    }

    private fun zigZagPairs(letters: String): Sequence<RailLetter> =
        zigZagNumbers().zip(letters.asSequence())

    fun getEncryptedData(letters: String): String =
        zigZagNumbers()
          .zip(letters.asSequence())
          .groupBy(RailLetter::railIndex, RailLetter::letter)
          .toSortedMap()
          .values
          .joinToString("") { it.joinToString("") }


    fun getDecryptedData(encrypted: String): String {
        val route = zigZagPairs(encrypted).map(RailLetter::railIndex)
        val railCounts = IntArray(numberOfRails)
        route.forEach { rail -> railCounts[rail]++ }

        val railText = Array(numberOfRails) { "" }
        var offset = 0
        for (rail in railText.indices) {
            val end = offset + railCounts[rail]
            railText[rail] = encrypted.substring(offset, end)
            offset = end
        }

        val railOffsets = IntArray(numberOfRails)
        return route.joinToString("") { rail ->
            railText[rail][railOffsets[rail]++].toString()
        }
    }
}
