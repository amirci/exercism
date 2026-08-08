import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ZebraPuzzle {
    String getWaterDrinker() {
        return nationalityMatching(house -> house.drink() == Drink.WATER);
    }

    String getZebraOwner() {
        return nationalityMatching(house -> house.pet() == Pet.ZEBRA);
    }

    private static String nationalityMatching(Predicate<House> predicate) {
        return solve().stream()
            .filter(predicate)
            .findFirst()
            .orElseThrow()
            .nationality()
            .displayName();
    }

    private static List<House> solve() {
        return validColors()
            .flatMap(ZebraPuzzle::addNationalities)
            .flatMap(ZebraPuzzle::addDrinks)
            .flatMap(ZebraPuzzle::addHobbies)
            .flatMap(ZebraPuzzle::addPets)
            .findFirst()
            .orElseThrow();
    }

    private static Stream<ColorSolution> validColors() {
        return permutations(Color.values())
            .filter(ZebraPuzzle::greenImmediatelyRightOfIvory)
            .filter(ZebraPuzzle::blueIsSecond)
            .map(ColorSolution::new);
    }

    private static Stream<NationalitySolution> addNationalities(ColorSolution solution) {
        return permutations(Nationality.values())
            .filter(ZebraPuzzle::norwegianLivesInFirstHouse)
            .filter(englishmanLivesInRedHouse(solution))
            .map(nationalities -> new NationalitySolution(solution.colors(), nationalities));
    }

    private static Stream<DrinkSolution> addDrinks(NationalitySolution solution) {
        return permutations(Drink.values())
            .filter(ZebraPuzzle::milkInMiddleHouse)
            .filter(greenHouseDrinksCoffee(solution))
            .filter(ukrainianDrinksTea(solution))
            .map(drinks -> new DrinkSolution(solution.colors(), solution.nationalities(), drinks));
    }

    private static Stream<HobbySolution> addHobbies(DrinkSolution solution) {
        return permutations(Hobby.values())
            .filter(painterLivesInYellowHouse(solution))
            .filter(footballPlayerDrinksOrangeJuice(solution))
            .filter(japanesePersonPlaysChess(solution))
            .map(hobbies -> new HobbySolution(solution.colors(), solution.nationalities(), solution.drinks(), hobbies));
    }

    private static Stream<List<House>> addPets(HobbySolution solution) {
        return permutations(Pet.values())
            .filter(spaniardOwnsDog(solution))
            .filter(snailOwnerGoesDancing(solution))
            .filter(readerLivesNextToFox(solution))
            .filter(painterLivesNextToHorse(solution))
            .map(pets -> buildHouses(
                solution.colors(),
                solution.nationalities(),
                pets,
                solution.drinks(),
                solution.hobbies()
            ));
    }

    private static boolean greenImmediatelyRightOfIvory(Color[] colors) {
        return position(colors, Color.GREEN) == position(colors, Color.IVORY) + 1;
    }

    private static boolean blueIsSecond(Color[] colors) {
        return position(colors, Color.BLUE) == 1;
    }

    private static boolean norwegianLivesInFirstHouse(Nationality[] nationalities) {
        return position(nationalities, Nationality.NORWEGIAN) == 0;
    }

    private static Predicate<Nationality[]> englishmanLivesInRedHouse(ColorSolution solution) {
        return nationalities -> samePosition(nationalities, Nationality.ENGLISHMAN, solution.colors(), Color.RED);
    }

    private static boolean milkInMiddleHouse(Drink[] drinks) {
        return position(drinks, Drink.MILK) == 2;
    }

    private static Predicate<Drink[]> greenHouseDrinksCoffee(NationalitySolution solution) {
        return drinks -> samePosition(drinks, Drink.COFFEE, solution.colors(), Color.GREEN);
    }

    private static Predicate<Drink[]> ukrainianDrinksTea(NationalitySolution solution) {
        return drinks -> samePosition(drinks, Drink.TEA, solution.nationalities(), Nationality.UKRAINIAN);
    }

    private static Predicate<Hobby[]> painterLivesInYellowHouse(DrinkSolution solution) {
        return hobbies -> samePosition(hobbies, Hobby.PAINTING, solution.colors(), Color.YELLOW);
    }

    private static Predicate<Hobby[]> footballPlayerDrinksOrangeJuice(DrinkSolution solution) {
        return hobbies -> samePosition(hobbies, Hobby.FOOTBALL, solution.drinks(), Drink.ORANGE_JUICE);
    }

    private static Predicate<Hobby[]> japanesePersonPlaysChess(DrinkSolution solution) {
        return hobbies -> samePosition(hobbies, Hobby.CHESS, solution.nationalities(), Nationality.JAPANESE);
    }

    private static Predicate<Pet[]> spaniardOwnsDog(HobbySolution solution) {
        return pets -> samePosition(pets, Pet.DOG, solution.nationalities(), Nationality.SPANIARD);
    }

    private static Predicate<Pet[]> snailOwnerGoesDancing(HobbySolution solution) {
        return pets -> samePosition(pets, Pet.SNAILS, solution.hobbies(), Hobby.DANCING);
    }

    private static Predicate<Pet[]> readerLivesNextToFox(HobbySolution solution) {
        return pets -> nextTo(pets, Pet.FOX, solution.hobbies(), Hobby.READING);
    }

    private static Predicate<Pet[]> painterLivesNextToHorse(HobbySolution solution) {
        return pets -> nextTo(pets, Pet.HORSE, solution.hobbies(), Hobby.PAINTING);
    }

    private static List<House> buildHouses(Color[] colors, Nationality[] nationalities, Pet[] pets, Drink[] drinks, Hobby[] hobbies) {
        return IntStream.range(0, 5)
            .mapToObj(position -> new House(colors[position], nationalities[position], pets[position], drinks[position], hobbies[position]))
            .toList();
    }

    private static boolean nextTo(int left, int right) {
        return Math.abs(left - right) == 1;
    }

    private static <TLeft, TRight> boolean nextTo(TLeft[] leftValues, TLeft left, TRight[] rightValues, TRight right) {
        return nextTo(position(leftValues, left), position(rightValues, right));
    }

    private static <TLeft, TRight> boolean samePosition(TLeft[] leftValues, TLeft left, TRight[] rightValues, TRight right) {
        return position(leftValues, left) == position(rightValues, right);
    }

    private static <T> int position(T[] values, T value) {
        return Arrays.asList(values).indexOf(value);
    }

    private static <T> Stream<T[]> permutations(T[] values) {
        if (values.length == 1) {
            return Stream.<T[]>of(values);
        }

        return Arrays.stream(values)
            .flatMap(value -> permutations(valuesWithout(values, value))
                .map(permutation -> prepend(value, permutation)));
    }

    private static <T> T[] valuesWithout(T[] values, T value) {
        return Arrays.stream(values)
            .filter(other -> !other.equals(value))
            .toArray(length -> Arrays.copyOf(values, length));
    }

    private static <T> T[] prepend(T value, T[] values) {
        var result = Arrays.copyOf(values, values.length + 1);

        System.arraycopy(result, 0, result, 1, values.length);
        result[0] = value;

        return result;
    }

    private enum Color {
        RED,
        GREEN,
        IVORY,
        YELLOW,
        BLUE
    }

    private enum Nationality {
        ENGLISHMAN("Englishman"),
        SPANIARD("Spaniard"),
        UKRAINIAN("Ukrainian"),
        JAPANESE("Japanese"),
        NORWEGIAN("Norwegian");

        private final String displayName;

        Nationality(String displayName) {
            this.displayName = displayName;
        }

        String displayName() {
            return displayName;
        }
    }

    private enum Pet {
        DOG,
        SNAILS,
        FOX,
        HORSE,
        ZEBRA
    }

    private enum Drink {
        COFFEE,
        TEA,
        MILK,
        ORANGE_JUICE,
        WATER
    }

    private enum Hobby {
        PAINTING,
        DANCING,
        READING,
        FOOTBALL,
        CHESS
    }

    private record ColorSolution(Color[] colors) {}

    private record NationalitySolution(Color[] colors, Nationality[] nationalities) {}

    private record DrinkSolution(Color[] colors, Nationality[] nationalities, Drink[] drinks) {}

    private record HobbySolution(Color[] colors, Nationality[] nationalities, Drink[] drinks, Hobby[] hobbies) {}

    private record House(Color color, Nationality nationality, Pet pet, Drink drink, Hobby hobby) {}
}
