object ZebraPuzzle {

  sealed trait Resident
  case object Englishman extends Resident
  case object Spaniard extends Resident
  case object Ukrainian extends Resident
  case object Norwegian extends Resident
  case object Japanese extends Resident

  case class Solution(waterDrinker: Resident, zebraOwner: Resident)

  lazy val solve: Solution =
    solutions.next()

  private enum Nation:
    case Englishman, Spaniard, Ukrainian, Norwegian, Japanese

  private enum Color:
    case Red, Green, Ivory, Yellow, Blue

  private enum Smoke:
    case OldGold, Kools, Chesterfields, LuckyStrike, Parliaments

  private enum Pet:
    case Dog, Snails, Fox, Horse, Zebra

  private enum Drink:
    case Coffee, Tea, Milk, OrangeJuice, Water

  private def solutions: Iterator[Solution] =
    for
      nations <- permutations(Nation.values)
      if nations.indexOf(Nation.Norwegian) == 0

      colors <- permutations(Color.values)
      if nations.indexOf(Nation.Englishman) == colors.indexOf(Color.Red)
      if nextTo(nations.indexOf(Nation.Norwegian), colors.indexOf(Color.Blue))
      if colors.indexOf(Color.Ivory) + 1 == colors.indexOf(Color.Green)

      drinks <- permutations(Drink.values)
      if drinks.indexOf(Drink.Milk) == 2
      if nations.indexOf(Nation.Ukrainian) == drinks.indexOf(Drink.Tea)
      if colors.indexOf(Color.Green) == drinks.indexOf(Drink.Coffee)

      pets <- permutations(Pet.values)
      if nations.indexOf(Nation.Spaniard) == pets.indexOf(Pet.Dog)

      smokes <- permutations(Smoke.values)
      if smokes.indexOf(Smoke.OldGold) == pets.indexOf(Pet.Snails)
      if smokes.indexOf(Smoke.Kools) == colors.indexOf(Color.Yellow)
      if smokes.indexOf(Smoke.LuckyStrike) == drinks.indexOf(Drink.OrangeJuice)
      if smokes.indexOf(Smoke.Parliaments) == nations.indexOf(Nation.Japanese)
      if nextTo(smokes.indexOf(Smoke.Chesterfields), pets.indexOf(Pet.Fox))
      if nextTo(smokes.indexOf(Smoke.Kools), pets.indexOf(Pet.Horse))
    yield Solution(
      waterDrinker = residentFor(nations(drinks.indexOf(Drink.Water))),
      zebraOwner = residentFor(nations(pets.indexOf(Pet.Zebra)))
    )

  private def permutations[A](values: Array[A]): Iterator[List[A]] =
    values.toList.permutations

  private def nextTo(left: Int, right: Int): Boolean =
    (left - right).abs == 1

  private def residentFor(nation: Nation): Resident =
    nation match
      case Nation.Englishman => Englishman
      case Nation.Spaniard => Spaniard
      case Nation.Ukrainian => Ukrainian
      case Nation.Norwegian => Norwegian
      case Nation.Japanese => Japanese
}
