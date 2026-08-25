object CollatzCalculator {
    fun computeStepCount(start: Int): Int {
        require(start > 0)

        return generateSequence(start, ::next)
            .takeWhile { value -> value != 1 }
            .count()
    }

    private fun next(value: Int): Int = if (value.even()) {
        value / 2
    } else {
        value * 3 + 1
    }

    private fun Int.even(): Boolean = this % 2 == 0
}
