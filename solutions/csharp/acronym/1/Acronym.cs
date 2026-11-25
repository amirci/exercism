public static class Acronym
{
    public static string Abbreviate(string phrase)
    {
        string cleaned = new(phrase
            .Where(character => !char.IsPunctuation(character) || character == '-')
            .ToArray());

        return string.Concat(cleaned
            .Replace("-", " ")
            .Split((char[]?)null, StringSplitOptions.RemoveEmptyEntries)
            .Select(word => char.ToUpperInvariant(word[0])));
    }
}
