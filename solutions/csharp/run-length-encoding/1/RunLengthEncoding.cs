using System.Text.RegularExpressions;

public static class RunLengthEncoding
{
    private const string EncodedRunPattern = @"(\d*)(\D)";

    public static string Encode(string input) =>
        string.Concat(Runs(input).Select(EncodeRun));

    public static string Decode(string input) =>
        Regex.Replace(input, EncodedRunPattern, DecodeRun);

    private static string EncodeRun((char Value, int Count) run) =>
        run.Count == 1 ? run.Value.ToString() : $"{run.Count}{run.Value}";

    private static string DecodeRun(Match match)
    {
        var countText = match.Groups[1].Value;
        var value = match.Groups[2].Value[0];
        var count = string.IsNullOrEmpty(countText) ? 1 : int.Parse(countText);

        return new string(value, count);
    }

    private static IEnumerable<(char Value, int Count)> Runs(string input)
    {
        if (input.Length == 0)
        {
            yield break;
        }

        var current = input[0];
        var count = 1;

        foreach (var value in input[1..])
        {
            if (value == current)
            {
                count++;
            }
            else
            {
                yield return (current, count);
                current = value;
                count = 1;
            }
        }

        yield return (current, count);
    }
}
