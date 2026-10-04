class CalculatorConundrum {
    public String calculate(int operand1, int operand2, String operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }

        if (operation.isEmpty()) {
            throw new IllegalArgumentException("Operation cannot be empty");
        }

        try {
            var result = switch (operation) {
                case "+" -> operand1 + operand2;
                case "*" -> operand1 * operand2;
                case "/" -> operand1 / operand2;
                default -> throw new IllegalOperationException("Operation '%s' does not exist".formatted(operation));
            };

            return "%d %s %d = %d".formatted(operand1, operation, operand2, result);
        } catch (ArithmeticException exception) {
            throw new IllegalOperationException("Division by zero is not allowed", exception);
        }
    }
}
