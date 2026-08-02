public static class Diamond
{
    public static string Make(char target)
    {
        var rows = Enumerable.Range('A', target - 'A' + 1)
            .Select(code => Line(target, (char)code))
            .ToArray();

        var bottom = rows.Reverse().Skip(1);

        return string.Join('\n', rows.Concat(bottom));
    }

    private static string Line(char target, char character)
    {
        var outerPaddingSize = target - character;

        if (character == 'A')
            return $"{Spaces(outerPaddingSize)}A{Spaces(outerPaddingSize)}";

        var outerPadding = Spaces(outerPaddingSize);
        var innerPadding = Spaces((character - 'A') * 2 - 1);

        return $"{outerPadding}{character}{innerPadding}{character}{outerPadding}";
    }

    private static string Spaces(int count) => new(' ', count);
}
