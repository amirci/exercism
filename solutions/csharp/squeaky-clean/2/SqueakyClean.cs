using System.Text.RegularExpressions;

public static class Identifier
{
    private const string CleanerPattern = @"(?<ws>\s)|(?<ctrl>\p{Cc})|-(?<kb>\p{L})|([^\p{L}_]|[α-ω])";

    public static string Clean(string identifier)
    {
        return Regex.Replace(identifier, CleanerPattern, match =>
        {
            var matchedGroup = match.Groups.Values.First(group => group.Success && group.Name != "0");

            return matchedGroup.Name switch
            {
                "ws" => "_",
                "ctrl" => "CTRL",
                "kb" => matchedGroup.Value.ToUpperInvariant(),
                _ => ""
            };
        });
    }
}
