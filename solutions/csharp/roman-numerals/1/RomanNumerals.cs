public static class RomanNumeralExtension
{
    private static readonly (int Value, string Symbol)[] Symbols =
    [
        (1000, "M"),
        (900, "CM"),
        (500, "D"),
        (400, "CD"),
        (100, "C"),
        (90, "XC"),
        (50, "L"),
        (40, "XL"),
        (10, "X"),
        (9, "IX"),
        (5, "V"),
        (4, "IV"),
        (1, "I")
    ];

    public static string ToRoman(this int value)
    {
        var result = new System.Text.StringBuilder();

        foreach (var (number, symbol) in Symbols)
        {
            var count = value / number;
            value -= count * number;

            AppendRepeated(result, symbol, count);
        }

        return result.ToString();
    }

    private static void AppendRepeated(System.Text.StringBuilder result, string symbol, int count)
    {
        for (var i = 0; i < count; i++)
        {
            result.Append(symbol);
        }
    }
}
