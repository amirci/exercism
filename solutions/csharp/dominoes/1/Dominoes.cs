using Domino = (int, int);
using DominoBag = System.Collections.Generic.Dictionary<(int, int), int>;

public static class Dominoes
{
    public static bool CanChain(IEnumerable<Domino> dominoes) =>
        dominoes.ToList() switch
        {
            [] => true,
            [(var target, var current), .. var remaining] => CanReach(target, current, ToBag(remaining))
        };

    private static bool CanReach(int target, int current, DominoBag dominoes)
    {
        if (dominoes.Count == 0)
        {
            return target == current;
        }

        foreach (var match in FindMatches(current, dominoes))
        {
            Remove(dominoes, match);

            if (CanReach(target, OtherSide(current, match), dominoes))
            {
                return true;
            }

            Restore(dominoes, match);
        }

        return false;
    }

    private static DominoBag ToBag(IEnumerable<Domino> dominoes) =>
        dominoes
            .Select(Normalize)
            .GroupBy(domino => domino)
            .ToDictionary(group => group.Key, group => group.Count());

    private static List<Domino> FindMatches(int value, DominoBag dominoes) =>
        dominoes.Keys
            .Where(domino => domino.Item1 == value || domino.Item2 == value)
            .ToList();

    private static void Remove(DominoBag dominoes, Domino domino)
    {
        if (dominoes[domino] == 1)
        {
            dominoes.Remove(domino);
        }
        else
        {
            dominoes[domino]--;
        }
    }

    private static void Restore(DominoBag dominoes, Domino domino)
    {
        dominoes.TryAdd(domino, 0);
        dominoes[domino]++;
    }

    private static Domino Normalize(Domino domino) =>
        domino.Item1 <= domino.Item2 ? domino : (domino.Item2, domino.Item1);

    private static int OtherSide(int value, Domino domino) =>
        domino.Item1 == value ? domino.Item2 : domino.Item1;
}
