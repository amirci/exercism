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

const parseBinary = (tokens) => {
  const [operator, ...rest] = tokens;
  if (operator !== "plus" && operator !== "minus") {
    return null;
  }
  const [operand, ...remaining] = rest;
  if (!isNumber(operand)) {
    return syntaxError();
  }
  return [[operator, Number(operand)], remaining];
};

const parseTernary = (tokens) => {
  const [operator, by, operand, ...rest] = tokens;
  if (operator !== "multiplied" && operator !== "divided") {
    return null;
  }
  if (by !== "by" || !isNumber(operand)) {
    return syntaxError();
  }
  return [[operator, Number(operand)], rest];
};

const unknownOperation = ([operator]) => {
  throw new Error(isNumber(operator) ? "Syntax error" : "Unknown operation");
};

const parseOperation = (tokens) =>
  parseBinary(tokens) ?? parseTernary(tokens) ?? unknownOperation(tokens);

const parseOperations = (tokens) => {
  const operations = [];
  let remaining = tokens;

  while (remaining.length > 0) {
    const [operation, rest] = parseOperation(remaining);
    operations.push(operation);
    remaining = rest;
  }

  return operations;
};

export const answer = (input) => {
  const prefix = "What is";

  validateQuestion(input, prefix);

  const [initial, tokens] = parseTokensAndValidate(input, prefix);

  const operations = parseOperations(tokens);

  return operations.reduce(
    (result, [operator, operand]) => OPERATION_FUNCTIONS[operator](result, operand),
    initial,
  );
};
