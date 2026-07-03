using Domino = (int, int);
using DominoBag = System.Collections.Generic.List<(int, int)>;

public static class Dominoes
{
    public static bool CanChain(IEnumerable<Domino> dominoes) =>
        dominoes.ToList() switch
        {
            [] => true,
            [(var target, var current), .. var remaining] => CanReach(target, current, remaining)
        };

    private static bool CanReach(int target, int current, DominoBag dominoes)
    {
        if (dominoes.Count == 0)
        {
            return target == current;
        }

        return FindMatches(current, dominoes)
            .Any(candidate => CanReach(target, OtherSide(current, candidate), dominoes.Without(candidate)));
    }

    private static List<Domino> FindMatches(int value, DominoBag dominoes) =>
        dominoes
          .Where(domino => domino.Item1 == value || domino.Item2 == value)
          .ToList();

    private static int OtherSide(int value, Domino domino) =>
        domino.Item1 == value ? domino.Item2 : domino.Item1;

    private static DominoBag Without(this DominoBag dominoes, Domino candidate)
    {
        var remaining = dominoes.ToList();
        remaining.Remove(candidate);
        return remaining;
    }
}
