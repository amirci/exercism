public enum Color { Red , Green , Ivory , Yellow , Blue }
public enum Nationality { Englishman , Spaniard , Ukrainian , Japanese , Norwegian }
public enum Pet { Dog , Snails , Fox , Horse , Zebra }
public enum Drink { Coffee , Tea , Milk , OrangeJuice , Water }
public enum Smoke { OldGold , Kools , Chesterfields , LuckyStrike , Parliaments }

public static class ZebraPuzzle
{
    public static Nationality DrinksWater() => Solve().First(house => house.Drink == Drink.Water).Nationality;

    public static Nationality OwnsZebra() => Solve().First(house => house.Pet == Pet.Zebra).Nationality;

    private static List<House> Solve() =>
        ValidColors()
            .SelectMany(AddNationalities)
            .SelectMany(AddDrinks)
            .SelectMany(AddSmokes)
            .SelectMany(AddPets)
            .First();

    private static Func<T[], bool> MatchesPosition<T>(T value, int position) =>
        values => Position(values, value) == position;

    private static Func<TLeft[], bool> MatchesPosition<TLeft, TRight>(
        TLeft left,
        TRight[] rightValues,
        TRight right) =>
        leftValues => Position(leftValues, left) == Position(rightValues, right);

    private static IEnumerable<ColorSolution> ValidColors() =>
        Permutations<Color>()
            .WhereImmediatelyRightOf(Color.Green, Color.Ivory)
            .WhereMatchesPosition(Color.Blue, 1)
            .Select(colors => new ColorSolution(colors));

    private static IEnumerable<NationalitySolution> AddNationalities(ColorSolution solution) =>
        Permutations<Nationality>()
            .WhereMatchesPosition(Nationality.Norwegian, 0)
            .WhereMatchesPosition(Nationality.Englishman, solution.Colors, Color.Red)
            .Select(nationalities => new NationalitySolution(solution.Colors, nationalities));

    private static IEnumerable<DrinkSolution> AddDrinks(NationalitySolution solution) =>
        Permutations<Drink>()
            .WhereMatchesPosition(Drink.Milk, 2)
            .WhereMatchesPosition(Drink.Coffee, solution.Colors, Color.Green)
            .WhereMatchesPosition(Drink.Tea, solution.Nationalities, Nationality.Ukrainian)
            .Select(drinks => new DrinkSolution(solution.Colors, solution.Nationalities, drinks));

    private static IEnumerable<SmokeSolution> AddSmokes(DrinkSolution solution) =>
        Permutations<Smoke>()
            .WhereMatchesPosition(Smoke.Kools, solution.Colors, Color.Yellow)
            .WhereMatchesPosition(Smoke.LuckyStrike, solution.Drinks, Drink.OrangeJuice)
            .WhereMatchesPosition(Smoke.Parliaments, solution.Nationalities, Nationality.Japanese)
            .Select(smokes => new SmokeSolution(solution.Colors, solution.Nationalities, solution.Drinks, smokes));

    private static IEnumerable<List<House>> AddPets(SmokeSolution solution) =>
        Permutations<Pet>()
            .WhereMatchesPosition(Pet.Dog, solution.Nationalities, Nationality.Spaniard)
            .WhereMatchesPosition(Pet.Snails, solution.Smokes, Smoke.OldGold)
            .WhereNextTo(Pet.Fox, solution.Smokes, Smoke.Chesterfields)
            .WhereNextTo(Pet.Horse, solution.Smokes, Smoke.Kools)
            .Select(pets => BuildHouses(solution.Colors, solution.Nationalities, pets, solution.Drinks, solution.Smokes));

    private static IEnumerable<T[]> WhereMatchesPosition<T>(
        this IEnumerable<T[]> candidates,
        T value,
        int position) =>
        candidates.Where(MatchesPosition(value, position));

    private static IEnumerable<TLeft[]> WhereMatchesPosition<TLeft, TRight>(
        this IEnumerable<TLeft[]> candidates,
        TLeft left,
        TRight[] rightValues,
        TRight right) =>
        candidates.Where(MatchesPosition(left, rightValues, right));

    private static IEnumerable<TLeft[]> WhereNextTo<TLeft, TRight>(
        this IEnumerable<TLeft[]> candidates,
        TLeft left,
        TRight[] rightValues,
        TRight right) =>
        candidates.Where(leftValues => NextTo(Position(leftValues, left), Position(rightValues, right)));

    private static IEnumerable<T[]> WhereImmediatelyRightOf<T>(
        this IEnumerable<T[]> candidates,
        T right,
        T left) =>
        candidates.Where(values => Position(values, right) == Position(values, left) + 1);

    private static List<House> BuildHouses(
        Color[] colors,
        Nationality[] nationalities,
        Pet[] pets,
        Drink[] drinks,
        Smoke[] smokes) =>
        Enumerable.Range(0, 5)
            .Select(position => new House(
                colors[position],
                nationalities[position],
                pets[position],
                drinks[position],
                smokes[position]))
            .ToList();

    private static bool NextTo(int left, int right) => Math.Abs(left - right) == 1;

    private static int Position<T>(T[] values, T value) => Array.IndexOf(values, value);

    private static IEnumerable<T[]> Permutations<T>() where T : struct, Enum =>
        Permutations(Enum.GetValues<T>());

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
