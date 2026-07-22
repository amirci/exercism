using System.Text.RegularExpressions;

public class LogParser
{
    public bool IsValidLine(string text) =>
        Regex.IsMatch(text, @"^\[(TRC|DBG|INF|WRN|ERR|FTL)\]");

    private static readonly Regex QuotedPassword = new("\".*password.*\"", RegexOptions.IgnoreCase);

    public string[] SplitLogLine(string text) =>
        Regex.Split(text, @"<[\^*=-]*>");

    public int CountQuotedPasswords(string lines) =>
        lines.Split(Environment.NewLine).Count(QuotedPassword.IsMatch);

    public string RemoveEndOfLineText(string line) =>
        Regex.Replace(line, @"end-of-line\d+", "");

    public string[] ListLinesWithPasswords(string[] lines) =>
        lines.Select(ListLineWithPassword).ToArray();

    private static readonly Regex WeakPassword = new(@"\bpassword\S+", RegexOptions.IgnoreCase);

    private static string ListLineWithPassword(string line)
    {
        var match = WeakPassword.Match(line);
        var password = match.Success ? match.Value : "--------";

        return $"{password}: {line}";
    }

}
