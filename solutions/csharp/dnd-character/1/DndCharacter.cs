public record DndCharacter(
    int Strength,
    int Dexterity,
    int Constitution,
    int Intelligence,
    int Wisdom,
    int Charisma)
{
    private const int SidesPerDie = 6;
    private const int BaseHitpoints = 10;
    private static readonly Random Random = new();

    public int Hitpoints { get; } = BaseHitpoints + Modifier(Constitution);

    public static int Modifier(int score) => (int)Math.Floor((score - 10) / 2.0);

    public static int Ability() => ThreeLargestDice().Sum();

    public static DndCharacter Generate() =>
        new(Ability(), Ability(), Ability(), Ability(), Ability(), Ability());

    private static IEnumerable<int> FourDice() =>
        Enumerable.Range(0, 4).Select(_ => Random.Next(1, SidesPerDie + 1));

    private static IEnumerable<int> ThreeLargestDice() =>
        FourDice().OrderDescending().Take(3);
}
