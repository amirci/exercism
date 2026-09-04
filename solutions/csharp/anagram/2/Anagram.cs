public class Anagram
{
    private readonly string target;
    private readonly IEnumerable<char> sortedLetters;

    public Anagram(string baseWord)
    {
        target = baseWord.ToLowerInvariant();
        sortedLetters = target.Sorted();
    }

    public string[] FindAnagrams(string[] potentialMatches)
    {
        return potentialMatches.Where(IsAnagram).ToArray();
    }

    private bool IsAnagram(string candidate)
    {
        var normalizedCandidate = candidate.ToLowerInvariant();
        return normalizedCandidate != target && normalizedCandidate.Sorted().SequenceEqual(sortedLetters);
    }
}

public static class StringExtensions
{
    public static IEnumerable<char> Sorted(this string text) => text.Order();
}
