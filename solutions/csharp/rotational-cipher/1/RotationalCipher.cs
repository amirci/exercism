public static class RotationalCipher
{
    public static string Rotate(string text, int shiftKey)
    {
        var shift = shiftKey % 26;

        var cipher = text.Select(character => char.IsLetter(character) ? RotateLetter(character, shift) : character);

        return string.Concat(cipher);
    }

    private static char RotateLetter(char character, int shiftKey)
    {
        var alphabetStart = char.IsUpper(character) ? 'A' : 'a';

        return (char)(alphabetStart + (character - alphabetStart + shiftKey) % 26);
    }
}
