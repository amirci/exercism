public class Player
{
    private static readonly Random random = new();
    private const int DieSides = 18;

    public int RollDie() => random.Next(1, DieSides + 1);

    public double GenerateSpellStrength() => random.NextDouble() * 100;
}
