using System.Text.RegularExpressions;

public static class RunLengthEncoding
{
    private const string UnencodedRunPattern = @"(.)\1*";
    private const string EncodedRunPattern = @"(\d*)(\D)";

    public static string Encode(string input) =>
        Regex.Replace(input, UnencodedRunPattern, EncodeRun);

    public static string Decode(string input) =>
        Regex.Replace(input, EncodedRunPattern, DecodeRun);

    private static string EncodeRun(Match match)
    {
        var value = match.Value[0];
        var count = match.Length;

        return count == 1 ? value.ToString() : $"{count}{value}";
    }

    private static string DecodeRun(Match match)
    {
        var countText = match.Groups[1].Value;
        var value = match.Groups[2].Value[0];
        var count = string.IsNullOrEmpty(countText) ? 1 : int.Parse(countText);

        return new string(value, count);
    }
}
