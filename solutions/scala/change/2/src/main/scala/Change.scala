object Change {
  private type Coin = Int
  private type Coins = List[Coin]
  private type Amount = Int

  def findFewestCoins(target: Amount, coins: Coins): Option[Coins] =
    if target < 0 then None
    else
      val sortedCoins = coins.sorted
      val best = Array.fill[Option[Coins]](target + 1)(None)
      best(0) = Some(List.empty)

      for amount <- 1 to target do
        best(amount) = bestChangeFor(amount, sortedCoins, best)

      best(target)

  private def bestChangeFor(amount: Amount, coins: Coins, best: Array[Option[Coins]]): Option[Coins] =
    coins
      .filter(_ <= amount)
      .flatMap(coin => best(amount - coin).map(change => coin +: change))
      .sortBy(_.length)
      .headOption
}
