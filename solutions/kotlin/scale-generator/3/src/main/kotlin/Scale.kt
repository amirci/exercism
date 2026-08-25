private val SHARP_NOTES = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
private val FLAT_NOTES = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
private val FLAT_TONICS = setOf("F", "Bb", "Eb", "Ab", "Db", "Gb", "d", "g", "c", "f", "bb", "eb")
private val STEPS_BY_INTERVAL = mapOf('m' to 1, 'M' to 2, 'A' to 3)

class Scale(
    private val tonic: String,
) {
    private val normalizedTonic = tonic.replaceFirstChar(Char::uppercase)
    private val notes = if (tonic in FLAT_TONICS) FLAT_NOTES else SHARP_NOTES
    private val chromaticScale =
        notes
            .cycle()
            .dropWhile { it != normalizedTonic }
            .take(12)
            .toList()

    fun chromatic(): List<String> = chromaticScale

    fun interval(intervals: String): List<String> =
        intervals
            .scan(0) { position, interval -> position + stepFor(interval) }
            .dropLast(1)
            .map { chromaticScale[it] }

    private fun stepFor(interval: Char): Int = STEPS_BY_INTERVAL.getValue(interval)
}

private fun <T> List<T>.cycle(): Sequence<T> = generateSequence { this }.flatten()
