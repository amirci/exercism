public class Anagram
{
    private readonly string word;
    private readonly string sortedLetters;

    public Anagram(string baseWord)
    {
        word = Normalize(baseWord);
        sortedLetters = SortLetters(word);
    }

    public string[] FindAnagrams(string[] potentialMatches)
    {
        return potentialMatches
            .Where(IsAnagram)
            .ToArray();
    }

    private bool IsAnagram(string candidate)
    {
        var normalizedCandidate = Normalize(candidate);
        return normalizedCandidate != word && SortLetters(normalizedCandidate) == sortedLetters;
    }

    private static string Normalize(string text) => text.ToLowerInvariant();

    private static string SortLetters(string text) => new(text.Order().ToArray());
}
