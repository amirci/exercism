using NumberGroup = (int Number, string Unit);

public static class Say
{
    private static readonly string[] UntilTwenty =
    [
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "ten",
        "eleven",
        "twelve",
        "thirteen",
        "fourteen",
        "fifteen",
        "sixteen",
        "seventeen",
        "eighteen",
        "nineteen"
    ];

    private static readonly string[] Tens =
    [
        "zero",
        "ten",
        "twenty",
        "thirty",
        "forty",
        "fifty",
        "sixty",
        "seventy",
        "eighty",
        "ninety"
    ];

    private static readonly string[] ScaleUnits = ["", "thousand", "million", "billion"];

    public static string InEnglish(long number)
    {
        EnsureRange(number);

        if (number == 0)
            return "zero";

        return string.Join(" ", SplitThousands(number)
            .Where(group => group.Number > 0)
            .Select(AddUnit)
            .Reverse());
    }

    private static void EnsureRange(long number)
    {
        if (number is < 0 or > 999_999_999_999)
            throw new ArgumentOutOfRangeException(nameof(number));
    }

    private static IEnumerable<NumberGroup> SplitThousands(long number)
    {
        for (var unit = 0; number > 0; unit++)
        {
            yield return ((int)(number % 1000), ScaleUnits[unit]);
            number /= 1000;
        }
    }

    private static string AddUnit(NumberGroup group)
    {
        var words = ToWords(group.Number);

        return group.Unit.Length == 0 ? words : $"{words} {group.Unit}";
    }

    private static string ToWords(int number)
    {
        var (hundreds, rest) = Math.DivRem(number, 100);
        var words = new List<string>();

        if (hundreds > 0)
            words.Add($"{UntilTwenty[hundreds]} hundred");

        if (rest is > 0 and < 20)
            words.Add(UntilTwenty[rest]);

        if (rest > 19)
            words.Add(CombinedNumber(rest));

        return string.Join(" ", words);
    }

    private static string CombinedNumber(int number)
    {
        var (tens, ones) = Math.DivRem(number, 10);

        return ones == 0 ? Tens[tens] : $"{Tens[tens]}-{UntilTwenty[ones]}";
    }
}
