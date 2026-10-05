class BankAccount {
    private int balance;
    private boolean open;

    synchronized void open() throws BankAccountActionInvalidException {
        ensureClosed();
        balance = 0;
        open = true;
    }

    synchronized void close() throws BankAccountActionInvalidException {
        rejectIf(!open, "Account not open");
        open = false;
    }

    synchronized int getBalance() throws BankAccountActionInvalidException {
        ensureOpen();
        return balance;
    }

    synchronized void deposit(int amount) throws BankAccountActionInvalidException {
        ensureOpen();
        ensureNonNegative(amount);
        balance += amount;
    }

    synchronized void withdraw(int amount) throws BankAccountActionInvalidException {
        ensureOpen();
        ensureNonNegative(amount);
        ensureSufficientFunds(amount);
        balance -= amount;
    }

    private void ensureSufficientFunds(int amount) throws BankAccountActionInvalidException {
        rejectIf(amount > balance, "Cannot withdraw more money than is currently in the account");
    }

    private void ensureClosed() throws BankAccountActionInvalidException {
        rejectIf(open, "Account already open");
    }

    private void ensureOpen() throws BankAccountActionInvalidException {
        rejectIf(!open, "Account closed");
    }

    private void ensureNonNegative(int amount) throws BankAccountActionInvalidException {
        rejectIf(amount < 0, "Cannot deposit or withdraw negative amount");
    }

    private void rejectIf(boolean condition, String message) throws BankAccountActionInvalidException {
        if (condition) {
            throw new BankAccountActionInvalidException(message);
        }
    }

}
