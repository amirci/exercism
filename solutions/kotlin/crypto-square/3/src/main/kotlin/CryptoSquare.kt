object CryptoSquare {

    fun ciphertext(plaintext: String): String {
        val normalized = plaintext.normalize()

        if (normalized.isEmpty()) return ""

        return normalized
            .toRectangleMatrix()
            .transpose()
            .joinToString(" ")
    }

    private fun String.normalize(): String = lowercase().filter(Char::isLetterOrDigit)

    private fun String.toRectangleMatrix(): List<String> {
        val columns = columnsFor(length)
        return chunked(columns) { it.padEnd(columns, ' ').toString() }
    }

    private fun List<String>.transpose(): List<String> =
        firstOrNull()
          ?.indices
          ?.map { column ->
              indices.joinToString("") { row -> this[row][column].toString() }
          } ?: emptyList()

    private fun columnsFor(length: Int): Int =
        generateSequence(0) { it + 1 }
            .first { it * it >= length }
}
