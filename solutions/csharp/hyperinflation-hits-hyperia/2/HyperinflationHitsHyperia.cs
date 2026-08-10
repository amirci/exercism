public static class CentralBank
{
    public static string DisplayDenomination(long @base, long multiplier) =>
        TryDisplayOrOverflow(() => checked(@base * multiplier), "*** Too Big ***");

    public static string DisplayGDP(float @base, float multiplier)
    {
        var gdp = @base * multiplier;

        return float.IsInfinity(gdp) ? "*** Too Big ***" : gdp.ToString();
    }

    public static string DisplayChiefEconomistSalary(decimal salaryBase, decimal multiplier) =>
        TryDisplayOrOverflow(() => (salaryBase * multiplier), "*** Much Too Big ***");

    private static string TryDisplayOrOverflow(Func<decimal> display, string overflowMessage)
    {
        try
        {
            return display().ToString();
        }
        catch (OverflowException)
        {
            return overflowMessage;
        }
    }
}
