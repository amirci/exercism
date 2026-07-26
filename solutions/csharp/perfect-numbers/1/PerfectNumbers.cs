public enum Classification
{
    Perfect,
    Abundant,
    Deficient
}

public static class PerfectNumbers
{
    public static Classification Classify(int number)
    {
        if (number < 1)
        {
            throw new ArgumentOutOfRangeException(nameof(number));
        }

        return AliquotSum(number).CompareTo(number) switch
        {
            0 => Classification.Perfect,
            > 0 => Classification.Abundant,
            _ => Classification.Deficient
        };
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

    private static Func<int, IEnumerable<int>> FactorPair(int number) =>
        factor =>
        {
            var pair = number / factor;

            return pair == factor ? [factor] : [factor, pair];
        };
}
