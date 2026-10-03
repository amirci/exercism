object CryptoSquare {

    fun ciphertext(plaintext: String): String {
        val normalized = plaintext
            .filter { it.isLetterOrDigit() }
            .lowercase()

        if (normalized.isEmpty()) return ""

        val columns = columnsFor(normalized.length)
        val rows = if (columns * (columns - 1) >= normalized.length) columns - 1 else columns

        return (0 until columns).joinToString(" ") { column ->
            (0 until rows)
                .map { row -> normalized.getOrElse(row * columns + column) { ' ' } }
                .joinToString("")
        }
    }

    private fun columnsFor(length: Int): Int {
        var columns = 0
        while (columns * columns < length) columns++
        return columns
    }

}
