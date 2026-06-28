static class SavingsAccount
{
    /// <summary>
    /// Calculates the annual interest rate for the specified balance.
    /// Negative balances use 3.213%, balances below 1000 use 0.5%,
    /// balances below 5000 use 1.621%, and balances of 5000 or more use 2.475%.
    /// </summary>
    /// <param name="balance">The current account balance.</param>
    /// <returns>The annual interest rate as a percentage.</returns>
    public static float InterestRate(decimal balance)
    {
        return balance switch
        {
            < 0 => 3.213f,
            < 1_000.0m => 0.5f,
            < 5_000.0m => 1.621f,
            _ => 2.475f
        };
    }

    /// <summary>
    /// Calculates the interest amount earned or owed for the specified balance.
    /// </summary>
    /// <param name="balance">The current account balance.</param>
    /// <returns>The balance multiplied by its annual interest rate.</returns>
    public static decimal Interest(decimal balance)
    {
        return balance * (decimal)InterestRate(balance) / 100;
    }

    /// <summary>
    /// Calculates the balance after one annual interest update.
    /// </summary>
    /// <param name="balance">The current account balance.</param>
    /// <returns>The current balance plus the annual interest amount.</returns>
    public static decimal AnnualBalanceUpdate(decimal balance)
    {
        return balance + Interest(balance);
    }

    /// <summary>
    /// Calculates the minimum number of years needed for a balance to reach a target balance
    /// when interest is compounded annually.
    /// </summary>
    /// <param name="startingBalance">The current account balance.</param>
    /// <param name="targetBalance">The desired account balance.</param>
    /// <returns>The number of annual balance updates required to reach the target balance.</returns>
    public static int YearsBeforeDesiredBalance(decimal startingBalance, decimal targetBalance)
    {
        var years = 0;

        for (var balance = startingBalance; balance < targetBalance; years++)
        {
            balance = AnnualBalanceUpdate(balance);
        }

        return years;
    }
}
