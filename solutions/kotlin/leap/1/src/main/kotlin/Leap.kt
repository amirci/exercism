data class Year(private val year: Int) {
    val isLeap: Boolean =
        year.isDivisibleBy(4) && (!year.isDivisibleBy(100) || year.isDivisibleBy(400))

    private fun Int.isDivisibleBy(divisor: Int): Boolean = this % divisor == 0
}
