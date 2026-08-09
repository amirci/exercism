public enum Color { Red, Green, Ivory, Yellow, Blue }
public enum Nationality { Englishman, Spaniard, Ukrainian, Japanese, Norwegian }
public enum Pet { Dog, Snails, Fox, Horse, Zebra }
public enum Drink { Coffee, Tea, Milk, OrangeJuice, Water }
public enum Smoke { OldGold, Kools, Chesterfields, LuckyStrike, Parliaments }

public static class ZebraPuzzle
{
    public static Nationality DrinksWater() => NationalityMatching(house => house.Drink == Drink.Water);

    public static Nationality OwnsZebra() => NationalityMatching(house => house.Pet == Pet.Zebra);

    private static Nationality NationalityMatching(Func<House, bool> predicate) =>
        FindHousesMatchingAllRules()
            .First(predicate)
            .Nationality;

    private static List<House> FindHousesMatchingAllRules() =>
        (
            from colors in ValidColors()
            from nationalities in MatchingNationalities(colors)
            from drinks in MatchingDrinks(nationalities)
            from smokes in MatchingSmokes(drinks)
            from pets in MatchingPets(smokes)
            select pets
        ).First();

    private static IEnumerable<ColorSolution> ValidColors() =>
        Permutations<Color>()
            .Satisfying(GreenImmediatelyRightOfIvory)
            .Satisfying(BlueIsSecond)
            .Select(colors => new ColorSolution(colors));

    private static IEnumerable<NationalitySolution> MatchingNationalities(ColorSolution solution) =>
        Permutations<Nationality>()
            .Satisfying(NorwegianLivesInFirstHouse)
            .Satisfying(EnglishmanLivesInRedHouse(solution))
            .Select(nationalities => new NationalitySolution(solution.Colors, nationalities));

    private static IEnumerable<DrinkSolution> MatchingDrinks(NationalitySolution solution) =>
        Permutations<Drink>()
            .Satisfying(MilkInMiddleHouse)
            .Satisfying(GreenHouseDrinksCoffee(solution))
            .Satisfying(UkrainianDrinksTea(solution))
            .Select(drinks => new DrinkSolution(solution.Colors, solution.Nationalities, drinks));

    private static IEnumerable<SmokeSolution> MatchingSmokes(DrinkSolution solution) =>
        Permutations<Smoke>()
            .Satisfying(PainterLivesInYellowHouse(solution))
            .Satisfying(FootballPlayerDrinksOrangeJuice(solution))
            .Satisfying(JapanesePersonPlaysChess(solution))
            .Select(smokes => new SmokeSolution(solution.Colors, solution.Nationalities, solution.Drinks, smokes));

    private static IEnumerable<List<House>> MatchingPets(SmokeSolution solution) =>
        Permutations<Pet>()
            .Satisfying(SpaniardOwnsDog(solution))
            .Satisfying(SnailOwnerGoesDancing(solution))
            .Satisfying(ReaderLivesNextToFox(solution))
            .Satisfying(PainterLivesNextToHorse(solution))
            .Select(pets => BuildHouses(solution.Colors, solution.Nationalities, pets, solution.Drinks, solution.Smokes));

    private static IEnumerable<T[]> Satisfying<T>(this IEnumerable<T[]> candidates, Func<T[], bool> rule) =>
        candidates.Where(rule);

    private static bool GreenImmediatelyRightOfIvory(Color[] colors) =>
        Position(colors, Color.Green) == Position(colors, Color.Ivory) + 1;

    private static bool BlueIsSecond(Color[] colors) => Position(colors, Color.Blue) == 1;

    private static bool NorwegianLivesInFirstHouse(Nationality[] nationalities) =>
        Position(nationalities, Nationality.Norwegian) == 0;

    private static Func<Nationality[], bool> EnglishmanLivesInRedHouse(ColorSolution solution) =>
        nationalities => SamePosition(nationalities, Nationality.Englishman, solution.Colors, Color.Red);

    private static bool MilkInMiddleHouse(Drink[] drinks) => Position(drinks, Drink.Milk) == 2;

    private static Func<Drink[], bool> GreenHouseDrinksCoffee(NationalitySolution solution) =>
        drinks => SamePosition(drinks, Drink.Coffee, solution.Colors, Color.Green);

    private static Func<Drink[], bool> UkrainianDrinksTea(NationalitySolution solution) =>
        drinks => SamePosition(drinks, Drink.Tea, solution.Nationalities, Nationality.Ukrainian);

    private static Func<Smoke[], bool> PainterLivesInYellowHouse(DrinkSolution solution) =>
        smokes => SamePosition(smokes, Smoke.Kools, solution.Colors, Color.Yellow);

    private static Func<Smoke[], bool> FootballPlayerDrinksOrangeJuice(DrinkSolution solution) =>
        smokes => SamePosition(smokes, Smoke.LuckyStrike, solution.Drinks, Drink.OrangeJuice);

    private static Func<Smoke[], bool> JapanesePersonPlaysChess(DrinkSolution solution) =>
        smokes => SamePosition(smokes, Smoke.Parliaments, solution.Nationalities, Nationality.Japanese);

    private static Func<Pet[], bool> SpaniardOwnsDog(SmokeSolution solution) =>
        pets => SamePosition(pets, Pet.Dog, solution.Nationalities, Nationality.Spaniard);

    private static Func<Pet[], bool> SnailOwnerGoesDancing(SmokeSolution solution) =>
        pets => SamePosition(pets, Pet.Snails, solution.Smokes, Smoke.OldGold);

    private static Func<Pet[], bool> ReaderLivesNextToFox(SmokeSolution solution) =>
        pets => NextTo(pets, Pet.Fox, solution.Smokes, Smoke.Chesterfields);

    private static Func<Pet[], bool> PainterLivesNextToHorse(SmokeSolution solution) =>
        pets => NextTo(pets, Pet.Horse, solution.Smokes, Smoke.Kools);

    private static List<House> BuildHouses(Color[] colors, Nationality[] nationalities, Pet[] pets, Drink[] drinks, Smoke[] smokes) =>
        Enumerable
          .Range(0, 5)
          .Select(position => new House(colors[position], nationalities[position], pets[position], drinks[position], smokes[position]))
          .ToList();

    private static bool NextTo(int left, int right) => Math.Abs(left - right) == 1;

    private static bool NextTo<TLeft, TRight>(TLeft[] leftValues, TLeft left, TRight[] rightValues, TRight right) =>
        NextTo(Position(leftValues, left), Position(rightValues, right));

    private static bool SamePosition<TLeft, TRight>(TLeft[] leftValues, TLeft left, TRight[] rightValues, TRight right) =>
        Position(leftValues, left) == Position(rightValues, right);

    private static int Position<T>(T[] values, T value) => Array.IndexOf(values, value);

    private static IEnumerable<T[]> Permutations<T>() where T : struct, Enum => Permutations(Enum.GetValues<T>());

    private static IEnumerable<T[]> Permutations<T>(T[] values) =>
        values.Length == 1
            ? [values]
            : values.SelectMany(value =>
                Permutations(values.Where(other => !other!.Equals(value)).ToArray()),
                (value, permutation) => new[] { value }.Concat(permutation).ToArray());

    private record ColorSolution(Color[] Colors);

    private record NationalitySolution(Color[] Colors, Nationality[] Nationalities);

    private record DrinkSolution(Color[] Colors, Nationality[] Nationalities, Drink[] Drinks);

    private record SmokeSolution(Color[] Colors, Nationality[] Nationalities, Drink[] Drinks, Smoke[] Smokes);

    private record House(Color Color, Nationality Nationality, Pet Pet, Drink Drink, Smoke Smoke);

}
