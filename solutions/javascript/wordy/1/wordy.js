//
// This is only a SKELETON file for the 'Wordy' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const OPERATORS = new Set(["plus", "minus", "multiplied", "divided"]);

const isNumber = (token) => /^-?\d+$/.test(token);

const syntaxError = () => {
  throw new Error("Syntax error");
};

export const answer = (input) => {
  const prefix = "What is ";
  if (!input.startsWith(prefix) || !input.endsWith("?")) {
    throw new Error("Unknown operation");
  }

  const tokens = input.slice(prefix.length, -1).trim().split(/\s+/);
  if (!tokens[0]) {
    return syntaxError();
  }
  if (!isNumber(tokens[0])) {
    throw new Error(OPERATORS.has(tokens[0]) ? "Syntax error" : "Unknown operation");
  }

  let result = Number(tokens[0]);
  let index = 1;

  while (index < tokens.length) {
    const operator = tokens[index];
    if (!OPERATORS.has(operator)) {
      throw new Error(isNumber(operator) ? "Syntax error" : "Unknown operation");
    }

    const operandIndex = operator === "multiplied" || operator === "divided"
      ? index + 2
      : index + 1;
    if ((operator === "multiplied" || operator === "divided") && tokens[index + 1] !== "by") {
      return syntaxError();
    }
    if (!isNumber(tokens[operandIndex])) {
      return syntaxError();
    }

    const operand = Number(tokens[operandIndex]);
    switch (operator) {
      case "plus": result += operand; break;
      case "minus": result -= operand; break;
      case "multiplied": result *= operand; break;
      case "divided": result /= operand; break;
    }
    index = operandIndex + 1;
  }

  return result;
};
