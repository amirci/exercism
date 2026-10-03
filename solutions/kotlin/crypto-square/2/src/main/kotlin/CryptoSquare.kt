object CryptoSquare {

    fun ciphertext(plaintext: String): String {
        val normalized = plaintext
            .filter { it.isLetterOrDigit() }
            .lowercase()

        if (normalized.isEmpty()) return ""

        val columns = columnsFor(normalized.length)

        val rows = if (columns * (columns - 1) >= normalized.length) columns - 1 else columns

        return (0 until columns).joinToString(" ") { col -> columnText(normalized, col, columns, rows) }
    }

    private fun columnsFor(length: Int): Int =
        generateSequence(0) { it + 1 }
            .first { it * it >= length }

    private fun columnText(
        text: String,
        column: Int,
        columns: Int,
        rows: Int,
    ): String = (0 until rows)
        .map { row -> text.getOrElse(row * columns + column) { ' ' } }
        .joinToString("")

}
