public static class ProteinTranslation
{
    private const string Stop = "STOP";

    private static readonly Dictionary<string, string> ProteinsByCodon = new()
    {
        ["AUG"] = "Methionine",
        ["UUU"] = "Phenylalanine",
        ["UUC"] = "Phenylalanine",
        ["UUA"] = "Leucine",
        ["UUG"] = "Leucine",
        ["UCU"] = "Serine",
        ["UCC"] = "Serine",
        ["UCA"] = "Serine",
        ["UCG"] = "Serine",
        ["UAU"] = "Tyrosine",
        ["UAC"] = "Tyrosine",
        ["UGU"] = "Cysteine",
        ["UGC"] = "Cysteine",
        ["UGG"] = "Tryptophan",
        ["UAA"] = Stop,
        ["UAG"] = Stop,
        ["UGA"] = Stop
    };

    public static string[] Proteins(string strand) =>
        strand.Chunk(3)
            .Select(ProteinFor)
            .TakeWhile(protein => protein != Stop)
            .ToArray();

    private static string ProteinFor(char[] chunk) =>
        ProteinsByCodon[new string(chunk)];
}
