using System.Text.RegularExpressions;

public class LogParser
{
    private static readonly Regex ValidLogLine = new(@"^\[(TRC|DBG|INF|WRN|ERR|FTL)\]");

    public bool IsValidLine(string text) => ValidLogLine.IsMatch(text);

    private static readonly Regex LogLineSeparator = new(@"<[\^*=-]*>");

    public string[] SplitLogLine(string text) => LogLineSeparator.Split(text);

    private static readonly Regex QuotedPassword = new("\".*password.*\"", RegexOptions.IgnoreCase);

    public int CountQuotedPasswords(string lines) =>
        lines.Split(Environment.NewLine).Count(QuotedPassword.IsMatch);

    private static readonly Regex EndOfLine = new(@"end-of-line\d+");

    public string RemoveEndOfLineText(string line) => EndOfLine.Replace(line, "");

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
