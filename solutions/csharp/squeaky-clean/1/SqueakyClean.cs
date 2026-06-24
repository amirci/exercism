using System.Text.RegularExpressions;

public static class Identifier
{
    private const string Whitespace = @"\s";
    private const string ControlCharacter = @"\p{Cc}";
    private const string DashAndFollowingLetter = @"-(\p{L})";
    private const string NonLetterOrUnderscore = @"[^\p{L}_]";
    private const string GreekLowerCase = @"[α-ω]";

    public static string Clean(string identifier)
    {
        return identifier
                // Space with underscore
                .RxReplace(Whitespace, "_")
                // Control chars with "CTRL"
                .RxReplace(ControlCharacter, "CTRL")
                // Kebab with CamelCase
                .RxReplace(DashAndFollowingLetter, match => match.Groups[1].Value.ToUpperInvariant())
                // omit chars that are not letters except underscore
                .RxReplace(NonLetterOrUnderscore, "")
                // omit greek letters
                .RxReplace(GreekLowerCase, "")
            ;

    }
    
}

static class StringExtensions
{
    public static string RxReplace(this string input, string pattern, string replacement)
        => Regex.Replace(input, pattern, replacement);

    public static string RxReplace(this string input, string pattern, MatchEvaluator evaluator)
        => Regex.Replace(input, pattern, evaluator);
}
