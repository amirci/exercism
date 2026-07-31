using System.Text.RegularExpressions;

public static class WordCount
{
    public static IDictionary<string, int> CountWords(string phrase)
    {
        return Regex
            .Matches(phrase.ToLower(), @"\b\w+(?:'\w+)?\b")
            .Select(match => match.Value)
            .GroupBy(word => word)
            .ToDictionary(group => group.Key, group => group.Count());
    }
}
