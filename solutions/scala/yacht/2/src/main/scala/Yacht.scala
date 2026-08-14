object Yacht {
  private type Die = Int
  private type Dice = List[Die]

  private val LittleStraight: Dice = List(1, 2, 3, 4, 5)
  private val BigStraight: Dice = List(2, 3, 4, 5, 6)

  def score(dice: Dice, category: String): Int =
    val frequencies = frequenciesFor(dice)

    category match {
      case "ones" => sumOf(dice, 1)
      case "twos" => sumOf(dice, 2)
      case "threes" => sumOf(dice, 3)
      case "fours" => sumOf(dice, 4)
      case "fives" => sumOf(dice, 5)
      case "sixes" => sumOf(dice, 6)
      case "full house" => fullHouseScore(dice, frequencies)
      case "four of a kind" => fourOfAKindScore(frequencies)
      case "little straight" => straightScore(frequencies, LittleStraight)
      case "big straight" => straightScore(frequencies, BigStraight)
      case "choice" => dice.sum
      case "yacht" => if frequencies.contains(5) then 50 else 0
      case _ => 0
    }

  private def frequenciesFor(dice: Dice): Array[Int] =
    val frequencies = Array.fill(7)(0)
    dice.foreach(die => frequencies(die) += 1)
    frequencies

  private def sumOf(dice: Dice, number: Die): Int =
    dice.filter(_ == number).sum

  private def fullHouseScore(dice: Dice, frequencies: Array[Int]): Int =
    if frequencies.contains(2) && frequencies.contains(3) then dice.sum else 0

  private def fourOfAKindScore(frequencies: Array[Int]): Int =
    (1 to 6)
      .find(die => frequencies(die) >= 4)
      .fold(0)(_ * 4)

  private def straightScore(frequencies: Array[Int], straight: Dice): Int =
    if straight.forall(die => frequencies(die) == 1) then 30 else 0
}
