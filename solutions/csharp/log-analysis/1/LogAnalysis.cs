public static class LogAnalysis
{
    public static string SubstringAfter(this string str, string delim)
    {
        var idx = str.IndexOf(delim);
        return idx == -1 ? "" : str[(idx + delim.Length)..];
    }

    public static string SubstringBetween(this string str, string start, string end)
    {
        var after = str.SubstringAfter(start);
        var idx = after.IndexOf(end);
        return idx == -1 ? "" : after[0..idx];
    }

    public static string Message(this string str) => str.SubstringAfter(": ");

    public static string LogLevel(this string str) => str.SubstringBetween("[", "]");
}
