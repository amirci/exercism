public static class Change
{
    public static int[] FindFewestCoins(int[] coins, int target)
    {
        EnsureChange(coins, target);

        var best = new List<int>?[target + 1];
        best[0] = [];

        foreach (var amount in Enumerable.Range(1, target))
        {
            best[amount] = coins
                .Where(coin => coin <= amount)
                .Select(coin => AddCoin(best[amount - coin], coin))
                .OfType<List<int>>()
                .MinBy(candidate => candidate.Count);
        }

        return best[target]?.Order().ToArray() ?? throw new ArgumentException();
    }

    private static void EnsureChange(int[] coins, int target)
    {
        if (target < 0 || target > 0 && coins.All(coin => coin > target))
            throw new ArgumentException();
    }

    private static List<int>? AddCoin(List<int>? coins, int coin)
    {
        if (coins is null)
            return null;

        return [.. coins, coin];
    }
}
