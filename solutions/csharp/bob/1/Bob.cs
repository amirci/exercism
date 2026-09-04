public static class Bob
{
    public static string Response(string statement)
    {
        var trimmedStatement = statement.Trim();

        return (IsSilence(trimmedStatement), IsYelling(trimmedStatement), IsQuestion(trimmedStatement)) switch
        {
            (true, _, _) => "Fine. Be that way!",
            (_, true, true) => "Calm down, I know what I'm doing!",
            (_, true, false) => "Whoa, chill out!",
            (_, false, true) => "Sure.",
            _ => "Whatever."
        };
    }

    private static bool IsSilence(string statement) => statement.Length == 0;

    private static bool IsQuestion(string statement) => statement.EndsWith('?');

    private static bool IsYelling(string statement) =>
        statement.Any(char.IsLetter) && !statement.Any(char.IsLower);
}
