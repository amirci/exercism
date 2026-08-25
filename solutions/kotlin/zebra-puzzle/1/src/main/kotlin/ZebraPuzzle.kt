import kotlin.enums.EnumEntries

class ZebraPuzzle {
    fun drinksWater(): String = solution.waterDrinker.displayName

    fun ownsZebra(): String = solution.zebraOwner.displayName

    private val solution: Solution by lazy { solutions().first() }
}

private data class Solution(val waterDrinker: Resident, val zebraOwner: Resident)

private enum class Resident(val displayName: String) {
    Englishman("Englishman"),
    Spaniard("Spaniard"),
    Ukrainian("Ukrainian"),
    Norwegian("Norwegian"),
    Japanese("Japanese"),
}

private enum class Color {
    Red,
    Green,
    Ivory,
    Yellow,
    Blue,
}

private enum class Pet {
    Dog,
    Snails,
    Fox,
    Horse,
    Zebra,
}

private enum class Drink {
    Coffee,
    Tea,
    Milk,
    OrangeJuice,
    Water,
}

private enum class Hobby {
    Dancing,
    Painting,
    Reading,
    Football,
    Chess,
}

private fun solutions(): Sequence<Solution> = Resident.entries
    .permutations()
    .matching(List<Resident>::hasNorwegianInFirstHouse)
    .flatMap { residents ->
        validColorPermutations(residents).flatMap { colors ->
            validDrinkPermutations(residents, colors).flatMap { drinks ->
                validPetPermutations(residents).flatMap { pets ->
                    validHobbyPermutations(residents, colors, drinks, pets).map {
                        Solution(
                            waterDrinker = residents[drinks.indexOf(Drink.Water)],
                            zebraOwner = residents[pets.indexOf(Pet.Zebra)],
                        )
                    }
                }
            }
        }
    }

private fun validColorPermutations(residents: List<Resident>): Sequence<List<Color>> = Color.entries
    .permutations()
    .matching(residents::hasEnglishmanInRedHouse)
    .matching(residents::hasNorwegianNextToBlueHouse)
    .matching(List<Color>::hasGreenHouseRightOfIvoryHouse)

private fun validDrinkPermutations(residents: List<Resident>, colors: List<Color>): Sequence<List<Drink>> =
    Drink.entries
        .permutations()
        .matching(List<Drink>::hasMilkInMiddleHouse)
        .matching(residents::hasUkrainianDrinkingTea)
        .matching(colors::hasGreenHouseDrinkingCoffee)

private fun validPetPermutations(residents: List<Resident>): Sequence<List<Pet>> = Pet.entries
    .permutations()
    .matching(residents::hasSpaniardOwningDog)

private fun validHobbyPermutations(
    residents: List<Resident>,
    colors: List<Color>,
    drinks: List<Drink>,
    pets: List<Pet>,
): Sequence<List<Hobby>> = Hobby.entries
    .permutations()
    .matching(pets::hasSnailOwnerDancing)
    .matching(colors::hasYellowHousePainter)
    .matching { it.hasFootballPlayerDrinkingOrangeJuice(drinks) }
    .matching { it.hasJapaneseChessPlayer(residents) }
    .matching { it.hasReaderNextToFoxOwner(pets) }
    .matching { it.hasPainterNextToHorseOwner(pets) }

private fun List<Resident>.hasNorwegianInFirstHouse(): Boolean = indexOf(Resident.Norwegian) == FIRST_HOUSE_INDEX

private fun List<Resident>.hasEnglishmanInRedHouse(colors: List<Color>): Boolean =
    indexOf(Resident.Englishman) == colors.indexOf(Color.Red)

private fun List<Resident>.hasNorwegianNextToBlueHouse(colors: List<Color>): Boolean =
    nextTo(indexOf(Resident.Norwegian), colors.indexOf(Color.Blue))

private fun List<Resident>.hasUkrainianDrinkingTea(drinks: List<Drink>): Boolean =
    indexOf(Resident.Ukrainian) == drinks.indexOf(Drink.Tea)

private fun List<Resident>.hasSpaniardOwningDog(pets: List<Pet>): Boolean =
    indexOf(Resident.Spaniard) == pets.indexOf(Pet.Dog)

private fun List<Color>.hasGreenHouseRightOfIvoryHouse(): Boolean = indexOf(Color.Ivory) + 1 == indexOf(Color.Green)

private fun List<Color>.hasGreenHouseDrinkingCoffee(drinks: List<Drink>): Boolean =
    indexOf(Color.Green) == drinks.indexOf(Drink.Coffee)

private fun List<Color>.hasYellowHousePainter(hobbies: List<Hobby>): Boolean =
    indexOf(Color.Yellow) == hobbies.indexOf(Hobby.Painting)

private fun List<Pet>.hasSnailOwnerDancing(hobbies: List<Hobby>): Boolean =
    indexOf(Pet.Snails) == hobbies.indexOf(Hobby.Dancing)

private fun List<Drink>.hasMilkInMiddleHouse(): Boolean = indexOf(Drink.Milk) == MIDDLE_HOUSE_INDEX

private fun List<Hobby>.hasFootballPlayerDrinkingOrangeJuice(drinks: List<Drink>): Boolean =
    indexOf(Hobby.Football) == drinks.indexOf(Drink.OrangeJuice)

private fun List<Hobby>.hasJapaneseChessPlayer(residents: List<Resident>): Boolean =
    indexOf(Hobby.Chess) == residents.indexOf(Resident.Japanese)

private fun List<Hobby>.hasReaderNextToFoxOwner(pets: List<Pet>): Boolean =
    nextTo(indexOf(Hobby.Reading), pets.indexOf(Pet.Fox))

private fun List<Hobby>.hasPainterNextToHorseOwner(pets: List<Pet>): Boolean =
    nextTo(indexOf(Hobby.Painting), pets.indexOf(Pet.Horse))

private fun <T : Enum<T>> EnumEntries<T>.permutations(): Sequence<List<T>> = toList().permutations()

private fun <T> Sequence<T>.matching(predicate: (T) -> Boolean): Sequence<T> = filter(predicate)

private fun <T> List<T>.permutations(): Sequence<List<T>> = sequence {
    if (size == 1) {
        yield(this@permutations)
    } else {
        for (index in indices) {
            val item = this@permutations[index]
            val rest = take(index) + drop(index + 1)
            for (permutation in rest.permutations()) {
                yield(listOf(item) + permutation)
            }
        }
    }
}

private fun nextTo(left: Int, right: Int): Boolean = kotlin.math.abs(left - right) == 1

private const val FIRST_HOUSE_INDEX = 0
private const val MIDDLE_HOUSE_INDEX = 2
