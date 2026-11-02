public static class FoodChain
{
    private static readonly string[] Animals =
    [
        "fly",
        "spider",
        "bird",
        "cat",
        "dog",
        "goat",
        "cow",
        "horse"
    ];

    private static readonly string[] Comments =
    [
        "",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        "She's dead, of course!"
    ];

    public static string Recite(int verseNumber)
    {
        var lines = new List<string> {OpeningLine(verseNumber)};

        if (verseNumber == Animals.Length)
        {
            lines.Add(Comments[verseNumber - 1]);
            return string.Join("\n", lines);
        }

        if (verseNumber > 1)
            lines.Add(Comments[verseNumber - 1]);

        for (var animalIndex = verseNumber - 1; animalIndex > 0; animalIndex--)
        {
            var spiderDescription = animalIndex == 2
                ? " that wriggled and jiggled and tickled inside her"
                : "";
            lines.Add($"She swallowed the {Animals[animalIndex]} to catch the {Animals[animalIndex - 1]}{spiderDescription}.");
        }

        lines.Add("I don't know why she swallowed the fly. Perhaps she'll die.");

        return lines.Join("\n");
    }

    public static string Recite(int startVerse, int endVerse) =>
        Enumerable
          .Range(startVerse, endVerse - startVerse + 1)
          .Select(verse => Recite(verse))
          .Join("\n\n");


    private static string OpeningLine(int verseNumber) =>
        $"I know an old lady who swallowed a {Animals[verseNumber - 1]}.";
}

public static class StringEnumerableExtensions
{
    public static string Join(this IEnumerable<string> values, string separator) =>
        string.Join(separator, values);
}
