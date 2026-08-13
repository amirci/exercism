object Yacht {
  private type Die = Int
  private type Dice = List[Die]

  private val LittleStraight: Dice = List(1, 2, 3, 4, 5)
  private val BigStraight: Dice = List(2, 3, 4, 5, 6)

  def score(dice: Dice, category: String): Int =
    val frequencies = dice.groupBy(identity).view.mapValues(_.length).toMap

    category match {
      case "ones" => sumOf(dice, 1)
      case "twos" => sumOf(dice, 2)
      case "threes" => sumOf(dice, 3)
      case "fours" => sumOf(dice, 4)
      case "fives" => sumOf(dice, 5)
      case "sixes" => sumOf(dice, 6)
      case "full house" => fullHouseScore(dice, frequencies)
      case "four of a kind" => fourOfAKindScore(frequencies)
      case "little straight" => straightScore(dice, LittleStraight)
      case "big straight" => straightScore(dice, BigStraight)
      case "choice" => dice.sum
      case "yacht" => if frequencies.size == 1 then 50 else 0
      case _ => 0
    }

  private def sumOf(dice: Dice, number: Die): Int =
    dice.filter(_ == number).sum

  private def fullHouseScore(dice: Dice, frequencies: Map[Die, Int]): Int =
    if frequencies.values.toSet == Set(2, 3) then dice.sum else 0

  private def fourOfAKindScore(frequencies: Map[Die, Int]): Int =
    frequencies
      .find { case (_, count) => count >= 4 }
      .fold(0) { case (number, _) => number * 4 }

  private def straightScore(dice: Dice, straight: Dice): Int =
    if dice.sorted == straight then 30 else 0
}
