public static class Bob
{
    public static string Response(string statement)
    {
        if( IsSilence(statement) ) {
            return "Fine. Be that way!";
        }
        
        var trimmedStatement = statement.Trim();

        return (IsYelling(trimmedStatement), IsQuestion(trimmedStatement)) switch
        {
            (true, true) => "Calm down, I know what I'm doing!",
            (true, false) => "Whoa, chill out!",
            (false, true) => "Sure.",
            _ => "Whatever."
        };
    }

    private static bool IsSilence(string statement) => string.IsNullOrWhiteSpace(statement);

    private static bool IsQuestion(string statement) => statement.EndsWith('?');

    private static bool IsYelling(string statement) =>
        statement.Any(char.IsLetter) && !statement.Any(char.IsLower);
}
