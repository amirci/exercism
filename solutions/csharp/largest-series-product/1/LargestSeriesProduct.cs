public static class LargestSeriesProduct
{
    public static long GetLargestProduct(string digits, int span)
    {
        ValidateSpan(digits, span);

        if (!digits.All(char.IsDigit))
            throw new ArgumentException();

        var parsedDigits = Digits(digits).ToArray();

        return Windows(parsedDigits, span)
            .Select(Product)
            .DefaultIfEmpty(1)
            .Max();
    }

    private static void ValidateSpan(string digits, int span)
    {
        if (span < 0 || span > digits.Length)
            throw new ArgumentException();
    }

    private static IEnumerable<int> Digits(string digits) =>
        digits.Select(digit => digit - '0');

    private static IEnumerable<IEnumerable<int>> Windows(int[] digits, int span) =>
        Enumerable.Range(0, digits.Length - span + 1)
            .Select(start => digits[start..(start + span)]);

    private static long Product(IEnumerable<int> digits) =>
        digits.Aggregate(1L, (product, digit) => product * digit);
}
