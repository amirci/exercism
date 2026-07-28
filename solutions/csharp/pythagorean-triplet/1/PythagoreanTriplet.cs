public static class PythagoreanTriplet
{
    public static IEnumerable<(int a, int b, int c)> TripletsWithSum(int sum) =>
        from a in Enumerable.Range(1, sum / 3)
        // Derived by substituting c = sum - a - b into a*a + b*b = c*c.
        let numerator = sum * (sum - 2 * a)
        let denominator = 2 * (sum - a)
        where numerator % denominator == 0
        let b = numerator / denominator
        let c = sum - a - b
        where a < b && b < c && IsPythagorean(a, b, c)
        select (a, b, c);

    private static bool IsPythagorean(int a, int b, int c) =>
        Square(a) + Square(b) == Square(c);

    private static int Square(int number) =>
        number * number;
}
