public static class CryptoSquare
{
    public static string Ciphertext(string plaintext)
    {
        var normalized = Normalize(plaintext);

        if (normalized.Length == 0)
            return "";

        var columns = Columns(normalized.Length);

        return string.Join(' ', Transpose(Rows(normalized, columns)));
    }

    private static string Normalize(string text) =>
        string.Concat(text.Where(char.IsLetterOrDigit).Select(char.ToLowerInvariant));

    private static string[] Rows(string text, int columns) =>
        text.Chunk(columns)
            .Select(row => new string(row).PadRight(columns))
            .ToArray();

    private static IEnumerable<string> Transpose(string[] rows) =>
        Enumerable.Range(0, rows[0].Length)
            .Select(column => string.Concat(rows.Select(row => row[column])));

    private static int Columns(int length) => (int)Math.Ceiling(Math.Sqrt(length));
}
