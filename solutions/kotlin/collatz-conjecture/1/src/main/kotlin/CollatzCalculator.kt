object CollatzCalculator {
    fun computeStepCount(start: Int): Int {
        require(start > 0)

        var value = start
        var steps = 0

        while (value != 1) {
            value =
                if (value.even()) {
                    value / 2
                } else {
                    value * 3 + 1
                }
            steps++
        }

        return steps
    }

    private fun Int.even(): Boolean = this % 2 == 0
}
