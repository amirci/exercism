using System.Text.RegularExpressions;
using BinaryOperation = System.Func<int, int, int>;

public static class Wordy
{
    private static readonly Regex NumberPattern = new(@"^-?\d+");
    private static readonly (string Token, BinaryOperation Operation)[] Operators =
    [
        ("plus", (left, right) => left + right),
        ("minus", (left, right) => left - right),
        ("multiplied by", (left, right) => left * right),
        ("divided by", (left, right) => left / right),
    ];

    private delegate ParseResult<T>? Parser<T>(string input);

    private sealed record ParseResult<T>(T Value, string Rest);
    private sealed record OperationExpression(BinaryOperation Apply, int Operand);

    public static int Answer(string question)
    {
        var result = Question()(question);

        if (result is null || result.Rest != "")
        {
            throw InvalidQuestion();
        }

        return result.Value;
    }

    private static Parser<int> Question() =>
        from _ in Token("What is ")
        from initial in Number()
        from operations in OperationExpressionParser().Many()
        from __ in Token("?")
        select Evaluate(initial, operations);

    private static int Evaluate(int initial, OperationExpression[] operations) =>
        operations.Aggregate(initial, (value, operation) =>
            operation.Apply(value, operation.Operand));

    private static ArgumentException InvalidQuestion() =>
        new("Invalid question");

    private static Parser<OperationExpression> OperationExpressionParser() =>
        from _ in Space()
        from operation in Operator()
        from __ in Space()
        from operand in Number()
        select new OperationExpression(operation, operand);

    private static Parser<BinaryOperation> Operator() =>
        input => Operators
            .Select(item => Operator(item.Token, item.Operation)(input))
            .FirstOrDefault(result => result is not null);

    private static Parser<BinaryOperation> Operator(string token, BinaryOperation operation) =>
        Token(token).Select(_ => operation);

    private static Parser<int> Number() =>
        input =>
        {
            var match = NumberPattern.Match(input);

            if (!match.Success)
            {
                return null;
            }

            return new ParseResult<int>(
                int.Parse(match.Value),
                input[match.Length..]);
        };

    private static Parser<string> Token(string token) =>
        input => input.StartsWith(token)
            ? new ParseResult<string>(token, input[token.Length..])
            : null;

    private static Parser<string> Space() => Token(" ");

    private static Parser<T[]> Many<T>(this Parser<T> parser) =>
        input =>
        {
            var values = new List<T>();
            var rest = input;

            while (parser(rest) is { } result)
            {
                values.Add(result.Value);
                rest = result.Rest;
            }

            return new ParseResult<T[]>(values.ToArray(), rest);
        };

    private static Parser<TResult> Select<T, TResult>(
        this Parser<T> parser,
        Func<T, TResult> map) =>
        input =>
        {
            var result = parser(input);
            return result is null
                ? null
                : new ParseResult<TResult>(map(result.Value), result.Rest);
        };

    private static Parser<TResult> SelectMany<T, U, TResult>(
        this Parser<T> parser,
        Func<T, Parser<U>> bind,
        Func<T, U, TResult> project) =>
        input =>
        {
            var result = parser(input);
            if (result is null)
            {
                return null;
            }

            var nextResult = bind(result.Value)(result.Rest);
            return nextResult is null
                ? null
                : new ParseResult<TResult>(
                    project(result.Value, nextResult.Value),
                    nextResult.Rest);
        };
}
