public static class FoodChain
{
    private const string FlyEnding =
        "I don't know why she swallowed the fly. Perhaps she'll die.";

    private sealed record Animal(string Name)
    {
        public string? CommentLine { get; init; }
        public string DescriptionWhenCaught { get; init; } = "";
        public string EndingLine { get; init; } = FlyEnding;
    }

    private static readonly Animal[] Animals =
    [
        new("fly"),
        WithComment("spider", "It wriggled and jiggled and tickled inside her.") with
        {
            DescriptionWhenCaught = " that wriggled and jiggled and tickled inside her"
        },
        WithComment("bird", "How absurd to swallow a bird!"),
        WithComment("cat", "Imagine that, to swallow a cat!"),
        WithComment("dog", "What a hog, to swallow a dog!"),
        WithComment("goat", "Just opened her throat and swallowed a goat!"),
        WithComment("cow", "I don't know how she swallowed a cow!"),
        new("horse") { EndingLine = "She's dead, of course!" }
    ];

    public static string Recite(int verseNumber)
    {
        var animal = Animals[verseNumber - 1];

        var lines = new List<string> { OpeningLine(animal) };

        lines.AddRange(CommentLines(animal));

        lines.AddRange(CatchLines(verseNumber));

        lines.Add(animal.EndingLine);

        return lines.Join("\n");
    }

    public static string Recite(int startVerse, int endVerse) =>
        Enumerable
            .Range(startVerse, endVerse - startVerse + 1)
            .Select(verse => Recite(verse))
            .Join("\n\n");

    private static string OpeningLine(Animal animal) =>
        $"I know an old lady who swallowed a {animal.Name}.";

    private static Animal WithComment(string name, string comment) =>
        new(name) { CommentLine = comment };

    private static IEnumerable<string> CommentLines(Animal animal) =>
        animal.CommentLine is { } comment ? [comment] : [];

    private static IEnumerable<(Animal Swallower, Animal Prey)> CatchPairs(int animalIndex)
    {
        for (var index = animalIndex - 1; index > 0 && index < Animals.Length - 1; index--)
        {
            yield return (Animals[index], Animals[index - 1]);
        }
    }

    private static IEnumerable<string> CatchLines(int animalIndex) =>
        CatchPairs(animalIndex).Select(pair =>
            $"She swallowed the {pair.Swallower.Name} to catch the {pair.Prey.Name}{pair.Prey.DescriptionWhenCaught}.");
}

public static class StringEnumerableExtensions
{
    public static string Join(this IEnumerable<string> values, string separator) =>
        string.Join(separator, values);
}
