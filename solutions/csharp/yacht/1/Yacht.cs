public enum YachtCategory
{
    Ones = 1,
    Twos = 2,
    Threes = 3,
    Fours = 4,
    Fives = 5,
    Sixes = 6,
    FullHouse = 7,
    FourOfAKind = 8,
    LittleStraight = 9,
    BigStraight = 10,
    Choice = 11,
    Yacht = 12,
}

public static class YachtGame
{
    private static readonly int[] LittleStraight = [1, 2, 3, 4, 5];
    private static readonly int[] BigStraight = [2, 3, 4, 5, 6];

    public static int Score(int[] dice, YachtCategory category)
    {
        var counts = CountsByFace(dice);

        return category switch
        {
            >= YachtCategory.Ones and <= YachtCategory.Sixes => SumFace(dice, (int)category),
            YachtCategory.FullHouse => IsFullHouse(counts) ? dice.Sum() : 0,
            YachtCategory.FourOfAKind => FourOfAKindScore(counts),
            YachtCategory.LittleStraight => IsStraight(dice, LittleStraight) ? 30 : 0,
            YachtCategory.BigStraight => IsStraight(dice, BigStraight) ? 30 : 0,
            YachtCategory.Choice => dice.Sum(),
            YachtCategory.Yacht => counts.Count == 1 ? 50 : 0,
            _ => 0
        };
    }

    private static int SumFace(int[] dice, int face) =>
        dice.Where(die => die == face).Sum();

    private static bool IsFullHouse(Dictionary<int, int> counts) =>
        counts.Values.Order().SequenceEqual([2, 3]);

    private static int FourOfAKindScore(Dictionary<int, int> counts) =>
        counts.Where(group => group.Value >= 4)
            .Select(group => group.Key * 4)
            .FirstOrDefault();

    private static bool IsStraight(int[] dice, int[] expected) =>
        dice.Order().SequenceEqual(expected);

    private static Dictionary<int, int> CountsByFace(int[] dice) =>
        dice.GroupBy(die => die)
            .ToDictionary(group => group.Key, group => group.Count());
}
