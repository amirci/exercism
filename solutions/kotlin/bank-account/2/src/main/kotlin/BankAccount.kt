class BankAccount {
    private var currentBalance: Long? = 0

    val balance: Long
        @Synchronized
        get() = currentBalance ?: throw closedAccount()

    @Synchronized
    fun adjustBalance(amount: Long) {
        currentBalance = balance + amount
    }

    @Synchronized
    fun close() {
        currentBalance = null
    }

    private fun closedAccount() = IllegalStateException("Account is closed.")
}
