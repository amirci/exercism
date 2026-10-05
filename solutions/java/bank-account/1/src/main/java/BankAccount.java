class BankAccount {
    private int balance;
    private boolean open;

    synchronized void open() throws BankAccountActionInvalidException {
        if (open) {
            throw new BankAccountActionInvalidException("Account already open");
        }
        balance = 0;
        open = true;
    }

    synchronized void close() throws BankAccountActionInvalidException {
        if (!open) {
            throw new BankAccountActionInvalidException("Account not open");
        }
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
        if (amount > balance) {
            throw new BankAccountActionInvalidException(
                    "Cannot withdraw more money than is currently in the account");
        }
        balance -= amount;
    }

    private void ensureOpen() throws BankAccountActionInvalidException {
        if (!open) {
            throw new BankAccountActionInvalidException("Account closed");
        }
    }

    private void ensureNonNegative(int amount) throws BankAccountActionInvalidException {
        if (amount < 0) {
            throw new BankAccountActionInvalidException(
                    "Cannot deposit or withdraw negative amount");
        }
    }

}
