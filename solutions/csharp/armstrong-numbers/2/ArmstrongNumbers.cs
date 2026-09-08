public static class ArmstrongNumbers
{
    public static bool IsArmstrongNumber(int number)
    {
        var digits = ToDigits(number).ToArray();
        return digits.Sum(digit => Math.Pow(digit, digits.Length)) == number;
    }

    private static IEnumerable<int> ToDigits(int number)
    {
        do
        {
            yield return number % 10;
            number /= 10;
        } while (number > 0);
    }
}
