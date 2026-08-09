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

            switch (token)
            {
                case "+":
                    ApplyBinaryOp((left, right) => left + right);
                    break;
                case "-":
                    ApplyBinaryOp((left, right) => left - right);
                    break;
                case "*":
                    ApplyBinaryOp((left, right) => left * right);
                    break;
                case "/":
                    ApplyBinaryOp((left, right) => left / right);
                    break;
                case "dup":
                    RequireValues(1);
                    stack.Push(stack.Peek());
                    break;
                case "drop":
                    RequireValues(1);
                    stack.Pop();
                    break;
                case "swap":
                    RequireValues(2);
                    var top = stack.Pop();
                    var second = stack.Pop();
                    stack.Push(top);
                    stack.Push(second);
                    break;
                case "over":
                    RequireValues(2);
                    stack.Push(stack.Skip(1).First());
                    break;
                default:
                    throw new InvalidOperationException();
            }
        }

        private IEnumerable<string> TokensFor(string token) =>
            definitions.TryGetValue(token, out var definition) ? definition : [token];

        private void ApplyBinaryOp(Func<int, int, int> operation)
        {
            RequireValues(2);

            var right = stack.Pop();
            var left = stack.Pop();

            stack.Push(operation(left, right));
        }

        private void RequireValues(int count)
        {
            if (stack.Count < count)
                throw new InvalidOperationException();
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
