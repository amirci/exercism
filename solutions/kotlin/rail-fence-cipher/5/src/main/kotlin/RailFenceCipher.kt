typealias RailIndex = Int
typealias RailLetter = Pair<RailIndex, Char>
typealias RailPosition = Pair<RailIndex, Int>
typealias PositionedLetter = Pair<RailPosition, Char>

private val RailLetter.railIndex: RailIndex
    get() = first

private val RailLetter.letter: Char
    get() = second

private val RailPosition.railIndexInPosition: RailIndex
    get() = first

private val RailPosition.position: Int
    get() = second

private val PositionedLetter.originalPosition: Int
    get() = first.position

class RailFenceCipher(private val numberOfRails: Int) {
    private val railCycle: List<RailIndex> =
        (0 until numberOfRails) + (numberOfRails - 2 downTo 1)

    init {
        require(numberOfRails > 0)
    }

    private fun zigZagNumbers(): Sequence<RailIndex> =
        generateSequence { railCycle }.flatten()

    fun getEncryptedData(letters: String): String =
        zigZagNumbers()
          .zip(letters.asSequence())
          .groupBy(RailLetter::railIndex, RailLetter::letter)
          .values
          .joinToString("") { it.joinToString("") }


    fun getDecryptedData(encrypted: String): String =
        zigZagNumbers()
            .take(encrypted.length)
            .mapIndexed { position, rail -> rail to position }
            .sortedBy(RailPosition::railIndexInPosition)
            .zip(encrypted.asSequence())
            .sortedBy(PositionedLetter::originalPosition)
            .joinToString("") { (_, letter) -> letter.toString() }
}
