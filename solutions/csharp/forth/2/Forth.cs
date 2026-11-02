public static class Forth
{
    public static string Evaluate(string[] instructions)
    {
        var interpreter = new Interpreter();

        foreach (var instruction in instructions)
            interpreter.Evaluate(instruction);

        return interpreter.Result();
    }

    private sealed class Interpreter
    {
        private readonly Stack<int> stack = new();
        private readonly Dictionary<string, string[]> definitions = new();

        public void Evaluate(string instruction)
        {
            var tokens = Tokens(instruction);

            switch (tokens)
            {
                case []:
                    return;
                case [var first, ..] when IsDefinitionStart(first):
                    Define(tokens);
                    return;
                default:
                    Execute(tokens);
                    return;
            }
        }

        public string Result() => string.Join(' ', stack.Reverse());

        private void Define(string[] tokens)
        {
            var name = tokens[1];

            EnsureDefinitionNameIsNotNumber(name);

            definitions[name] = tokens[2..^1].SelectMany(TokensFor).ToArray();
        }

        private void Execute(IEnumerable<string> tokens)
        {
            foreach (var token in tokens.SelectMany(TokensFor))
                Execute(token);
        }

        private void Execute(string token)
        {
            if (int.TryParse(token, out var number))
            {
                stack.Push(number);
                return;
            }

            OperationFor(token)();
        }

        private IEnumerable<string> TokensFor(string token) =>
            definitions.TryGetValue(token, out var definition) ? definition : [token];

        private Action OperationFor(string token) => token switch
        {
            "+" => Sum,
            "-" => Difference,
            "*" => Product,
            "/" => Quotient,
            "dup" => Dup,
            "drop" => Drop,
            "swap" => Swap,
            "over" => Over,
            _ => throw new InvalidOperationException()
        };

        private void Sum() => ApplyBinaryOp((left, right) => left + right);

        private void Difference() => ApplyBinaryOp((left, right) => left - right);

        private void Product() => ApplyBinaryOp((left, right) => left * right);

        private void Quotient() => ApplyBinaryOp((left, right) => left / right);

        private void Dup()
        {
            var value = PopValue();
            stack.Push(value);
            stack.Push(value);
        }

        private void Drop() => PopValue();

        private void Swap()
        {
            var (below, top) = PopTwoValues();
            stack.Push(top);
            stack.Push(below);
        }

        private void Over()
        {
            var (below, top) = PopTwoValues();
            stack.Push(below);
            stack.Push(top);
            stack.Push(below);
        }

        private void ApplyBinaryOp(Func<int, int, int> operation)
        {
            var (left, right) = PopTwoValues();
            stack.Push(operation(left, right));
        }

        private int PopValue()
        {
            if (stack.Count < 1)
                throw new InvalidOperationException();

            return stack.Pop();
        }

        private (int Below, int Top) PopTwoValues()
        {
            if (stack.Count < 2)
                throw new InvalidOperationException();

            var top = stack.Pop();
            var below = stack.Pop();
            return (below, top);
        }

        private static void EnsureDefinitionNameIsNotNumber(string name)
        {
            if (int.TryParse(name, out _))
                throw new InvalidOperationException();
        }

        private static string[] Tokens(string instruction) =>
            instruction
                .ToLowerInvariant()
                .Split(' ', StringSplitOptions.RemoveEmptyEntries);

        private static bool IsDefinitionStart(string token) => token == ":";
    }
}
