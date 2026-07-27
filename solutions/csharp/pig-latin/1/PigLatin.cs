public static class PigLatin
{
    public static string Translate(string word) =>
        string.Join(" ", word.Split(' ').Select(TranslateWord));

    private static string TranslateWord(string word) =>
        StartsWithVowelSound(word) ? $"{word}ay" : TranslateConsonant(word);

    private static string TranslateConsonant(string word)
    {
        bool IsClusterEnd(int index) =>
            index > 0 && word[index] == 'y' || IsVowel(word[index]) || IsQu(word, index);

        var index = Enumerable.Range(0, word.Length)
            .First(IsClusterEnd);

        var split = IsQu(word, index) ? index + 2 : index;

        return $"{word[split..]}{word[..split]}ay";
    }

    private static bool StartsWithVowelSound(string word) =>
        IsVowel(word[0]) || word.StartsWith("xr") || word.StartsWith("yt");

    private static bool IsVowel(char character) => "aeiou".Contains(character);

    private static bool IsQu(string word, int index) =>
        word[index] == 'q' && index + 1 < word.Length && word[index + 1] == 'u';
}
