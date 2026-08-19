trait BankAccount {

  def closeAccount(): Unit

  def getBalance: Option[Int]

  def incrementBalance(increment: Int): Option[Int]
}

object Bank {
  def openAccount(): BankAccount = new Account

  private class Account extends BankAccount {
    private var balance: Option[Int] = Some(0)

    override def closeAccount(): Unit = synchronized {
      balance = None
    }

    override def getBalance: Option[Int] = synchronized {
      balance
    }

    override def incrementBalance(increment: Int): Option[Int] = synchronized {
      balance = balance.map(_ + increment)
      balance
    }
  }
}
