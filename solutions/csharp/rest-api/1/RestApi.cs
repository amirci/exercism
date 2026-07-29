using System;
using System.Collections.Immutable;
using System.Text.Json;
using System.Text.Json.Serialization;

using Debts = System.Collections.Immutable.ImmutableSortedDictionary<string, decimal>;
using UserPredicate = System.Func<User, bool>;

public class RestApi
{
    private static readonly JsonSerializerOptions JsonOptions = new()
    {
        PropertyNamingPolicy = JsonNamingPolicy.SnakeCaseLower
    };

    private readonly List<User> _users;
    private readonly Dictionary<string, int> _userIndex;

    public RestApi(string database)
    {
        _users = Deserialize<List<User>>(database);
        _userIndex = UserIndex(_users);
    }

    public string Get(string url, string? payload = null)
    {
        object response = (url, payload) switch
        {
            ("/users", null) => GetUsers(null),
            ("/users", string usersPayload) => GetUsers(Deserialize<GetUsersRequest>(usersPayload)),
            _ => new { }
        };

        return Serialize(response);
    }

    public string Post(string url, string payload)
    {
        object response = url switch
        {
            "/add" => AddUser(Deserialize<AddUserRequest>(payload)),
            "/iou" => RegisterLoan(Deserialize<IouRequest>(payload)),
            _ => new { }
        };

        return Serialize(response);
    }

    private IEnumerable<User> GetUsers(GetUsersRequest? request)
    {
        UserPredicate filter = request switch
        {
            null => _ => true,
            _ => user => request.Users.Contains(user.Name)
        };

        return _users.Where(filter);
    }

    private User AddUser(AddUserRequest request)
    {
        var user = new User(request.User, Debts.Empty, Debts.Empty, 0);

        _users.Add(user);
        _userIndex[user.Name] = _users.Count - 1;

        return user;
    }

    private IEnumerable<User> RegisterLoan(IouRequest request)
    {
        var lender = UserNamed(request.Lender);
        var borrower = UserNamed(request.Borrower);

        var repayment = Math.Min(request.Amount, lender.AmountOwedTo(borrower));
        var newLoan = request.Amount - repayment;

        var newLender = lender.Repay(borrower, repayment).LendTo(borrower, newLoan);
        var newBorrower = borrower.ReceiveRepaymentFrom(lender, repayment).BorrowFrom(lender, newLoan);

        Replace(newLender);
        Replace(newBorrower);

        return new[] { newLender, newBorrower }.OrderBy(user => user.Name);
    }

    private User UserNamed(string name) => _users[_userIndex[name]];

    private void Replace(User updated)
    {
        _users[_userIndex[updated.Name]] = updated;
    }

    private static T Deserialize<T>(string json) => JsonSerializer.Deserialize<T>(json, JsonOptions)!;

    private static string Serialize(object value) => JsonSerializer.Serialize(value, JsonOptions);

    private static Dictionary<string, int> UserIndex(IEnumerable<User> users) =>
        users
        .Select((user, index) => (user.Name, index))
        .ToDictionary(item => item.Name, item => item.index);
}

record User(
    string Name,
    Debts Owes,
    [property: JsonPropertyName("owed_by")]
    Debts OwedBy,
    decimal Balance
)
{
    public decimal AmountOwedTo(User user) => Owes.GetValueOrDefault(user.Name);

    public User Repay(User user, decimal amount) => WithOwes(user, -amount);

    public User ReceiveRepaymentFrom(User user, decimal amount) => WithOwedBy(user, -amount);

    public User LendTo(User user, decimal amount) => WithOwedBy(user, amount);

    public User BorrowFrom(User user, decimal amount) => WithOwes(user, amount);

    private User WithOwes(User user, decimal amount) => WithDebts(UpdateDebt(Owes, user, amount), OwedBy);

    private User WithOwedBy(User user, decimal amount) => WithDebts(Owes, UpdateDebt(OwedBy, user, amount));

    private User WithDebts(Debts owes, Debts owedBy) =>
        this with
        {
            Owes = owes,
            OwedBy = owedBy,
            Balance = CalculateBalance(owes, owedBy)
        };

    private static Debts UpdateDebt(Debts debts, User user, decimal amount)
    {
        var newAmount = debts.GetValueOrDefault(user.Name) + amount;

        return newAmount == 0 ? debts.Remove(user.Name) : debts.SetItem(user.Name, newAmount);
    }

    private static decimal CalculateBalance(Debts owes, Debts owedBy) => owedBy.Values.Sum() - owes.Values.Sum();

}

record AddUserRequest(string User);

record GetUsersRequest(string[] Users);

record IouRequest(string Lender, string Borrower, decimal Amount);
