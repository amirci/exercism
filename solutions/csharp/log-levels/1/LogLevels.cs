using System.Text.RegularExpressions;

static class LogLine
{
    public static string Message(string logLine) => Parse(logLine).Message;

    public static string LogLevel(string logLine) => Parse(logLine).Level.ToLower();

    public static string Reformat(string logLine) =>
        $"{Message(logLine)} ({LogLevel(logLine)})";

    private static (string Level, string Message) Parse(string logLine)
    {
        var match = Regex.Match(logLine, @"^\[(?<level>INFO|WARNING|ERROR)\]:\s*(?<message>.*)$");

        return (
            match.Groups["level"].Value,
            match.Groups["message"].Value.Trim());
    }
}
