public static class ParallelLetterFrequency
{
    public static async Task<Dictionary<char, int>> Calculate(IEnumerable<string> texts)
    {
        var counts = await Task.WhenAll(texts.Select(text => Task.Run(() => CountLetters(text))));

        return Combine(counts);
    }

    private static Dictionary<char, int> CountLetters(string text) =>
        text.Where(char.IsLetter)
            .Select(char.ToLower)
            .GroupBy(letter => letter)
            .ToDictionary(group => group.Key, group => group.Count());

    private static Dictionary<char, int> Combine(IEnumerable<Dictionary<char, int>> counts) =>
        counts.SelectMany(count => count)
            .GroupBy(pair => pair.Key)
            .ToDictionary(
                group => group.Key,
                group => group.Sum(pair => pair.Value));
}
