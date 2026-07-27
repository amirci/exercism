public class BankAccount
{
    private readonly object _lock = new();
    private decimal? _balance;

    public void Open()
    {
        lock (_lock)
        {
            EnsureClosed();

            _balance = 0m;
        }
    }

    public void Close()
    {
        lock (_lock)
        {
            EnsureOpen();

            _balance = null;
        }
    }

    public decimal Balance
    {
        get
        {
            lock (_lock)
            {
                return EnsureOpen();
            }
        }
    }

    public void Deposit(decimal change)
    {
        lock (_lock)
        {
            var balance = EnsureOpen();
            EnsurePositive(change);

            _balance = balance + change;
        }
    }

    public void Withdraw(decimal change)
    {
        lock (_lock)
        {
            var balance = EnsureOpen();
            EnsurePositive(change);
            EnsureSufficientFundsFor(change, balance);

            _balance = balance - change;
        }
    }

    private decimal EnsureOpen()
    {
        Ensure(_balance is not null);

        return _balance.Value;
    }

    private void EnsureClosed() => Ensure(_balance is null);

    private static void EnsurePositive(decimal change) => Ensure(change >= 0);

    private static void EnsureSufficientFundsFor(decimal change, decimal balance) =>
        Ensure(change <= balance);

    private static void Ensure(bool condition)
    {
        if (!condition)
        {
            throw new InvalidOperationException();
        }
    }
}
