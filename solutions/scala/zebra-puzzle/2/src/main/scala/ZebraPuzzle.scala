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
      List(red, green, ivory, yellow) <- positions.filter(_ != 1).permutations
      if green == ivory + 1
    yield Colors(red, green, ivory, yellow, 1)

  private def nationalityCandidates(positions: List[Int], colors: Colors): Iterator[Nationalities] =
    for
      List(englishman, spaniard, ukrainian, japanese) <- positions.filter(_ != 0).permutations
      if englishman == colors.red
    yield Nationalities(englishman, spaniard, ukrainian, 0, japanese)

  private def drinkCandidates(positions: List[Int], colors: Colors, nationalities: Nationalities): Iterator[Drinks] =
    for
      List(tea, orangeJuice, water) <- positions.filterNot(position => position == colors.green || position == 2).permutations
      if tea == nationalities.ukrainian
    yield Drinks(colors.green, tea, 2, orangeJuice, water)

  private def petCandidates(positions: List[Int], nationalities: Nationalities): Iterator[Pets] =
    for
      List(snails, fox, horse, zebra) <- positions.filter(_ != nationalities.spaniard).permutations
    yield Pets(nationalities.spaniard, snails, fox, horse, zebra)

  private def smokeCandidates(positions: List[Int], colors: Colors, nationalities: Nationalities, drinks: Drinks, pets: Pets): Iterator[Smokes] =
    val usedPositions = List(pets.snails, colors.yellow, drinks.orangeJuice, nationalities.japanese)

    for
      chesterfields <- positions.diff(usedPositions).iterator
      if usedPositions.distinct.length == usedPositions.length
      if nextTo(chesterfields, pets.fox)
      if nextTo(colors.yellow, pets.horse)
    yield Smokes(pets.snails, colors.yellow, chesterfields, drinks.orangeJuice, nationalities.japanese)

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
