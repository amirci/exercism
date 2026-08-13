object Change {
  private type Coins = List[Int]

  def findFewestCoins(target: Int, coins: Coins): Option[Coins] =
    if target < 0 then None
    else
      val sortedCoins = coins.sorted
      val best = Array.fill[Option[Coins]](target + 1)(None)
      best(0) = Some(List.empty)

      for amount <- 1 to target do
        best(amount) = bestChangeFor(amount, sortedCoins, best)

      best(target)

  private def bestChangeFor(amount: Int, coins: Coins, best: Array[Option[Coins]]): Option[Coins] =
    coins
      .filter(_ <= amount)
      .flatMap(coin => best(amount - coin).map(change => coin +: change))
      .sortBy(_.length)
      .headOption
}
