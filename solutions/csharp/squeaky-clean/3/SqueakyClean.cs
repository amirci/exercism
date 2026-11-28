using System.Text;

public static class Identifier
{
    public static string Clean(string identifier)
    {
        var result = new StringBuilder();
        bool uppercaseNext = false;

        foreach (char character in identifier)
        {
            result.Append(CleanCharacter(character, uppercaseNext));
            uppercaseNext = character == '-';
        }

        return result.ToString();
    }

    private static string? CleanCharacter(char character, bool uppercaseNext)
        => character switch
        {
            _ when char.IsControl(character) => "CTRL",
            ' ' => "_",
            '-' => null,
            >= 'α' and <= 'ω' => null,
            _ when char.IsLetter(character) => (uppercaseNext ? char.ToUpperInvariant(character) : character).ToString(),
            _ => null
        };
}
