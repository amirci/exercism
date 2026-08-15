object ZebraPuzzle {

  sealed trait Resident
  case object Englishman extends Resident
  case object Spaniard extends Resident
  case object Ukrainian extends Resident
  case object Norwegian extends Resident
  case object Japanese extends Resident

  case class Solution(waterDrinker: Resident, zebraOwner: Resident)

  lazy val solve: Solution =
    val positions = List.range(0, 5)

    val solutions =
      for
        List(red, green, ivory, yellow, blue) <- positions.permutations
        if green == ivory + 1
        List(englishman, spaniard, ukrainian, norwegian, japanese) <- positions.permutations
        if englishman == red
        if norwegian == 0
        if nextTo(norwegian, blue)
        List(coffee, tea, milk, orangeJuice, water) <- positions.permutations
        if coffee == green
        if tea == ukrainian
        if milk == 2
        List(dog, snails, fox, horse, zebra) <- positions.permutations
        if dog == spaniard
        List(oldGold, kools, chesterfields, luckyStrike, parliaments) <- positions.permutations
        if oldGold == snails
        if kools == yellow
        if luckyStrike == orangeJuice
        if parliaments == japanese
        if nextTo(chesterfields, fox)
        if nextTo(kools, horse)
      yield Solution(
        waterDrinker = residentAt(water, englishman, spaniard, ukrainian, norwegian, japanese),
        zebraOwner = residentAt(zebra, englishman, spaniard, ukrainian, norwegian, japanese)
      )

    solutions.next()

  private def nextTo(left: Int, right: Int): Boolean =
    (left - right).abs == 1

  private def residentAt(
      position: Int,
      englishman: Int,
      spaniard: Int,
      ukrainian: Int,
      norwegian: Int,
      japanese: Int
  ): Resident =
    position match
      case `englishman` => Englishman
      case `spaniard` => Spaniard
      case `ukrainian` => Ukrainian
      case `norwegian` => Norwegian
      case `japanese` => Japanese
}
