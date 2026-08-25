class ChangeCalculator(private val coins: List<Int>) {
    fun computeMostEfficientChange(grandTotal: Int): List<Int> {
        require(grandTotal >= 0) { "Negative totals are not allowed." }

        val changes = MutableList<List<Int>?>(grandTotal + 1) { null }
        changes[0] = emptyList()

        for (amount in 1..grandTotal) {
            changes[amount] =
                coins
                    .filter { coin -> coin <= amount }
                    .mapNotNull { coin -> changes[amount - coin]?.plus(coin) }
                    .minByOrNull { change -> change.size }
        }

        return changes[grandTotal]
            ?: throw IllegalArgumentException("The total $grandTotal cannot be represented in the given currency.")
    }
}
