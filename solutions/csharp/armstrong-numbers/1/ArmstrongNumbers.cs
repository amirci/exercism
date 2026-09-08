public static class ArmstrongNumbers
{
    public static bool IsArmstrongNumber(int number)
    {
        var digits = ToDigits(number).ToArray();
        return digits.Sum(digit => Power(digit, digits.Length)) == number;
    }

    private static IEnumerable<int> ToDigits(int number)
    {
        do
        {
            yield return number % 10;
            number /= 10;
        } while (number > 0);
    }

    private static long Power(int basis, int exponent) =>
        Enumerable.Repeat(basis, exponent)
            .Aggregate(1L, (product, factor) => product * factor);
}
