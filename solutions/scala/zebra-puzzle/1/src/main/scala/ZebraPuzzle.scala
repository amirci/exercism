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
        colors <- colorCandidates(positions)
        nationalities <- nationalityCandidates(positions, colors)
        drinks <- drinkCandidates(positions, colors, nationalities)
        pets <- petCandidates(positions, nationalities)
        smokes <- smokeCandidates(positions, colors, nationalities, drinks, pets)
      yield Solution(
        waterDrinker = residentAt(drinks.water, nationalities),
        zebraOwner = residentAt(pets.zebra, nationalities)
      )

    solutions.next()

  private case class Colors(red: Int, green: Int, ivory: Int, yellow: Int, blue: Int)
  private case class Nationalities(englishman: Int, spaniard: Int, ukrainian: Int, norwegian: Int, japanese: Int)
  private case class Drinks(coffee: Int, tea: Int, milk: Int, orangeJuice: Int, water: Int)
  private case class Pets(dog: Int, snails: Int, fox: Int, horse: Int, zebra: Int)
  private case class Smokes(oldGold: Int, kools: Int, chesterfields: Int, luckyStrike: Int, parliaments: Int)

  private def colorCandidates(positions: List[Int]): Iterator[Colors] =
    for
      List(red, green, ivory, yellow, blue) <- positions.permutations
      if green == ivory + 1
    yield Colors(red, green, ivory, yellow, blue)

  private def nationalityCandidates(positions: List[Int], colors: Colors): Iterator[Nationalities] =
    for
      List(englishman, spaniard, ukrainian, japanese) <- positions.filter(_ != 0).permutations
      if englishman == colors.red
      if nextTo(0, colors.blue)
    yield Nationalities(englishman, spaniard, ukrainian, 0, japanese)

  private def drinkCandidates(positions: List[Int], colors: Colors, nationalities: Nationalities): Iterator[Drinks] =
    for
      List(coffee, tea, orangeJuice, water) <- positions.filter(_ != 2).permutations
      if coffee == colors.green
      if tea == nationalities.ukrainian
    yield Drinks(coffee, tea, 2, orangeJuice, water)

  private def petCandidates(positions: List[Int], nationalities: Nationalities): Iterator[Pets] =
    for
      List(dog, snails, fox, horse, zebra) <- positions.permutations
      if dog == nationalities.spaniard
    yield Pets(dog, snails, fox, horse, zebra)

  private def smokeCandidates(positions: List[Int], colors: Colors, nationalities: Nationalities, drinks: Drinks, pets: Pets): Iterator[Smokes] =
    for
      List(oldGold, kools, chesterfields, luckyStrike, parliaments) <- positions.permutations
      if oldGold == pets.snails
      if kools == colors.yellow
      if luckyStrike == drinks.orangeJuice
      if parliaments == nationalities.japanese
      if nextTo(chesterfields, pets.fox)
      if nextTo(kools, pets.horse)
    yield Smokes(oldGold, kools, chesterfields, luckyStrike, parliaments)

  private def nextTo(left: Int, right: Int): Boolean =
    (left - right).abs == 1

  private def residentAt(position: Int, nationalities: Nationalities): Resident =
    position match
      case nationalities.englishman => Englishman
      case nationalities.spaniard => Spaniard
      case nationalities.ukrainian => Ukrainian
      case nationalities.norwegian => Norwegian
      case nationalities.japanese => Japanese
}
