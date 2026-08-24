private val SHARP_NOTES = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
private val FLAT_NOTES = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
private val FLAT_TONICS = setOf("F", "Bb", "Eb", "Ab", "Db", "Gb", "d", "g", "c", "f", "bb", "eb")

class Scale(private val tonic: String) {

    private val normalizedTonic = tonic.replaceFirstChar { it.uppercase() }
    private val notes = if (tonic in FLAT_TONICS) FLAT_NOTES else SHARP_NOTES

    fun chromatic(): List<String> =
        notes
            .cycle()
            .dropWhile { it != normalizedTonic }
            .take(12)
            .toList()

    fun interval(intervals: String): List<String> {
        var index = notes.indexOf(normalizedTonic)

        return intervals.map { interval ->
            notes[index].also {
                index = (index + stepFor(interval)) % notes.size
            }
        }
    }

    private fun stepFor(interval: Char): Int =
        when (interval) {
            'm' -> 1
            'M' -> 2
            else -> 3
        }
}

private fun <T> List<T>.cycle(): Sequence<T> = generateSequence { this }.flatten()
