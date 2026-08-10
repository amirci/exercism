using SearchFile = System.Func<string, string, System.Collections.Generic.IEnumerable<string>>;

public static class Grep
{
    public static string Match(string pattern, string flags, string[] files)
    {
        var searchFile = ParseOptions(flags, files);
        var output = files.SelectMany(file => searchFile(file, pattern));

        return string.Join('\n', output);
    }

    private static IEnumerable<LineInfo> LinesIn(string file) =>
        File.ReadLines(file).Select((line, index) => new LineInfo(file, index + 1, line));

    private readonly record struct LineInfo(string File, int LineNumber, string Line);

    private static SearchFile ParseOptions(string flags, string[] files)
    {
        var values = flags.Split(' ', StringSplitOptions.RemoveEmptyEntries);
        var isMatch = Matcher();
        var outputFormatter = OutputFormatter();

        return IsSet(OnlyFileNamesOption) ? MatchingFileName : MatchingOutputLines;

        bool IsSet(string flag) => values.Contains(flag);

        Func<LineInfo, string> OutputFormatter()
        {
            var lineNumberPrefix = LineNumberPrefixFor(IsSet(LineNumbersOption));
            var fileNamePrefix = FileNamePrefixFor(files);
            return match => $"{fileNamePrefix(match)}{lineNumberPrefix(match)}{match.Line}";
        }

        Func<LineInfo, string, bool> Matcher()
        {
            var comparison = values.Contains(IgnoreCaseOption) ? StringComparison.OrdinalIgnoreCase : StringComparison.Ordinal;

            Func<LineInfo, string, bool> matcher = IsSet(MatchEntireLineOption)
                ? (line, pattern) => string.Equals(line.Line, pattern, comparison)
                : (line, pattern) => line.Line.Contains(pattern, comparison);

            return IsSet(InvertMatchOption) ? (line, pattern) => !matcher(line, pattern) : matcher;
        }

        IEnumerable<string> MatchingOutputLines(string file, string pattern) =>
            LinesIn(file).Where(line => isMatch(line, pattern)).Select(outputFormatter);

        IEnumerable<string> MatchingFileName(string file, string pattern) =>
            LinesIn(file).Any(line => isMatch(line, pattern)) ? [file] : [];
    }

    private static Func<LineInfo, string> LineNumberPrefixFor(bool enabled) =>
        enabled ? line => $"{line.LineNumber}:" : _ => "";

    private static Func<LineInfo, string> FileNamePrefixFor(string[] files) =>
        files.Length > 1 ? line => $"{line.File}:" : _ => "";

    private const string LineNumbersOption = "-n";
    private const string OnlyFileNamesOption = "-l";
    private const string IgnoreCaseOption = "-i";
    private const string InvertMatchOption = "-v";
    private const string MatchEntireLineOption = "-x";
}
