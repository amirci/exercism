//
// This is only a SKELETON file for the 'Wordy' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const OPERATORS = new Set(["plus", "minus", "multiplied", "divided"]);

const OPERATION_FUNCTIONS = {
  plus: (left, right) => left + right,
  minus: (left, right) => left - right,
  multiplied: (left, right) => left * right,
  divided: (left, right) => left / right,
};

const isNumber = (token) => /^-?\d+$/.test(token);

const syntaxError = () => {
  throw new Error("Syntax error");
};

const validateQuestion = (input, prefix) => {
  if (!input.startsWith(prefix) || !input.endsWith("?")) {
    throw new Error("Unknown operation");
  }
};

const parseTokensAndValidate = (input, prefix) => {
  const [initial, ...rest] = input.slice(prefix.length, -1).trim().split(/\s+/);

  if (!initial) {
    return syntaxError();
  }

  if (!isNumber(initial)) {
    throw new Error(OPERATORS.has(initial) ? "Syntax error" : "Unknown operation");
  }

  return [Number(initial), rest];
};

const parseOperations = (tokens) => {
  const operations = [];
  let index = 0;

  while (index < tokens.length) {
    const operator = tokens[index];
    if (!OPERATORS.has(operator)) {
      throw new Error(isNumber(operator) ? "Syntax error" : "Unknown operation");
    }

    const isTwoWordOperator = operator === "multiplied" || operator === "divided";
    const operandIndex = isTwoWordOperator ? index + 2 : index + 1;
    if (isTwoWordOperator && tokens[index + 1] !== "by") {
      return syntaxError();
    }
    if (!isNumber(tokens[operandIndex])) {
      return syntaxError();
    }

    operations.push([operator, Number(tokens[operandIndex])]);
    index = operandIndex + 1;
  }

  return operations;
};

export const answer = (input) => {
  const prefix = "What is ";

  validateQuestion(input, prefix);

  const [initial, tokens] = parseTokensAndValidate(input, prefix);

  const operations = parseOperations(tokens);

  return operations.reduce(
    (result, [operator, operand]) => OPERATION_FUNCTIONS[operator](result, operand),
    initial,
  );
};
