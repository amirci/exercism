public static class DifferenceOfSquares
{
    public static int CalculateSquareOfSum(int max) => SumTo(max) * SumTo(max);

    public static int CalculateSumOfSquares(int max) => max * (max + 1) * (2 * max + 1) / 6;

    public static int CalculateDifferenceOfSquares(int max) =>
        CalculateSquareOfSum(max) - CalculateSumOfSquares(max);

    private static int SumTo(int max) => max * (max + 1) / 2;
}
