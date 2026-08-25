class BankAccount {
    private var currentBalance: Long? = 0

    val balance: Long
        get() =
            synchronized(this) {
                currentBalance ?: throw closedAccount()
            }

    fun adjustBalance(amount: Long) {
        synchronized(this) {
            currentBalance = balance + amount
        }
    }

    fun close() {
        synchronized(this) {
            currentBalance = null
        }
    }

    private fun closedAccount() = IllegalStateException("Account is closed.")
}
