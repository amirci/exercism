public static class BookStore
{
    private static readonly decimal[] GroupPrices = [0m, 8m, 15.2m, 21.6m, 25.6m, 30m];

    public static decimal Total(IEnumerable<int> books)
    {
        var counts = books
            .GroupBy(book => book)
            .Select(group => group.Count())
            .OrderDescending()
            .ToArray();

        return Cheapest(counts);
    }

    private static decimal Cheapest(int[] counts) =>
        Cheapest(counts, new());

    private static decimal Cheapest(int[] counts, Dictionary<string, decimal> cache)
    {
        if (counts.All(count => count == 0))
            return 0m;

        var key = string.Join(",", counts);

        if (cache.TryGetValue(key, out var cached))
            return cached;

        var available = counts
            .Select((count, index) => (count, index))
            .Where(book => book.count > 0)
            .Select(book => book.index)
            .ToArray();

        var cheapest = NonEmptySubGroups(available)
            .Select(Price)
            .Min();

        cache[key] = cheapest;

        return cheapest;

        decimal Price(int[] group)
        {
            var remaining = counts.ToArray();

            foreach (var book in group)
                remaining[book]--;

            return GroupPrices[group.Length] + Cheapest(remaining, cache);
        }
    }

    private static IEnumerable<int[]> NonEmptySubGroups(int[] books) =>
        Enumerable.Range(1, NumberOfSubGroups(books.Length) - 1)
            .Select(mask => books.Where((_, index) => Includes(mask, index)).ToArray());

    private static int NumberOfSubGroups(int count) => 1 << count;

    private static bool Includes(int group, int index) => (group & (1 << index)) != 0;
}
