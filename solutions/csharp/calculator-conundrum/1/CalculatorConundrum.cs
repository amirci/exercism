public static class SimpleCalculator
{
    public static string Calculate(int operand1, int operand2, string? operation)
    {
        return operation switch
        {
            null => throw new ArgumentNullException(nameof(operation), "The operation cannot be null"),
            "+" => $"{operand1} + {operand2} = {operand1 + operand2}",
            "*" => $"{operand1} * {operand2} = {operand1 * operand2}",
            "/" when operand2 == 0 => "Division by zero is not allowed.",
            "/" => $"{operand1} / {operand2} = {operand1 / operand2}",
            "**" => throw new ArgumentOutOfRangeException(nameof(operation), "The operation is not supported"),
            _ => throw new ArgumentException("The operation must be valid ('*', '+', '/')")
        };

    }
}
