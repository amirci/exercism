public enum Classification
{
    Deficient = -1,
    Perfect = 0,
    Abundant = 1
}

public static class PerfectNumbers
{
    public static Classification Classify(int number)
    {
        if (number < 1)
        {
            throw new ArgumentOutOfRangeException(nameof(number));
        }

        return (Classification)AliquotSum(number).CompareTo(number);
    }

    private static int AliquotSum(int number)
    {
        if (number == 1) return 0;

        return Enumerable
            .Range(2, (int)Math.Sqrt(number) - 1)
            .Where(IsMultipleOf(number))
            .SelectMany(FactorPair(number))
            .Sum() + 1;
    }

    private static Func<int, bool> IsMultipleOf(int number) => factor => number % factor == 0;

    private static Func<int, IEnumerable<int>> FactorPair(int number) => factor =>
    {
        var pair = number / factor;

        return pair == factor ? [factor] : [factor, pair];
    };
}
