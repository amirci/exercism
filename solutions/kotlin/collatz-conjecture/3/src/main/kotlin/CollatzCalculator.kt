object CollatzCalculator {
    fun computeStepCount(start: Int): Int {
        require(start > 0)

        return generateSequence(start.toLong(), ::next)
            .takeWhile { value -> value != 1L }
            .count()
    }

    private fun next(value: Long): Long = if (value.even()) {
        value / 2
    } else {
        value * 3 + 1
    }

    private fun Long.even(): Boolean = this % 2 == 0L
}
