using System.Text;

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
        return Symbols.Aggregate(
            (result: new StringBuilder(), remaining: value),
            (state, denomination) =>
            {
                var count = state.remaining / denomination.Value;

                return (
                    result: state.result.Append(denomination.Symbol, count),
                    remaining: state.remaining - count * denomination.Value
                );
            }
        ).result.ToString();
    }

}

public static class StringBuilderExtensions
{
    public static StringBuilder Append(this StringBuilder builder, string value, int count)
    {
        for (var i = 0; i < count; i++)
        {
            builder.Append(value);
        }

        return builder;
    }
}


