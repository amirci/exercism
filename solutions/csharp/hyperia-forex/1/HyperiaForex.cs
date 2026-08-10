public readonly struct CurrencyAmount
{
    private readonly decimal amount;
    private readonly string currency;

    public CurrencyAmount(decimal amount, string currency)
    {
        this.amount = amount;
        this.currency = currency;
    }

    public static bool operator ==(CurrencyAmount left, CurrencyAmount right) =>
        Compare(left, right, (l, r) => l == r);

    public static bool operator !=(CurrencyAmount left, CurrencyAmount right) => !(left == right);

    public static bool operator <(CurrencyAmount left, CurrencyAmount right) => Compare(left, right, (l, r) => l < r);

    public static bool operator >(CurrencyAmount left, CurrencyAmount right) => Compare(left, right, (l, r) => l > r);

    public static CurrencyAmount operator +(CurrencyAmount left, CurrencyAmount right) =>
        Calculate(left, right, (l, r) => l + r);

    public static CurrencyAmount operator -(CurrencyAmount left, CurrencyAmount right) =>
        Calculate(left, right, (l, r) => l - r);

    public static CurrencyAmount operator *(CurrencyAmount amount, decimal factor) =>
        new CurrencyAmount(amount.amount * factor, amount.currency);

    public static CurrencyAmount operator *(decimal factor, CurrencyAmount amount) => amount * factor;

    public static CurrencyAmount operator /(CurrencyAmount amount, decimal divisor) =>
        new CurrencyAmount(amount.amount / divisor, amount.currency);

    public static explicit operator double(CurrencyAmount amount) => Convert.ToDouble(amount.amount);

    public static implicit operator decimal(CurrencyAmount amount) => amount.amount;

    public override readonly bool Equals(object? obj) => obj is CurrencyAmount other && this == other;

    public override readonly int GetHashCode() => HashCode.Combine(amount, currency);

    private static bool Compare(
        CurrencyAmount left,
        CurrencyAmount right,
        Func<decimal, decimal, bool> comparison)
    {
        EnsureSameCurrency(left, right);
        return comparison(left.amount, right.amount);
    }

    private static CurrencyAmount Calculate(
        CurrencyAmount left,
        CurrencyAmount right,
        Func<decimal, decimal, decimal> operation)
    {
        EnsureSameCurrency(left, right);
        return new CurrencyAmount(operation(left.amount, right.amount), left.currency);
    }

    private static void EnsureSameCurrency(CurrencyAmount left, CurrencyAmount right)
    {
        if (left.currency != right.currency)
            throw new ArgumentException();
    }
}
