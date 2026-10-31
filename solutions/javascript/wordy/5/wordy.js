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

const errorWith = (msg) => {
  throw new Error(msg);
}

const requireOrError = (value, msg) => {
    if (!value) {
        errorWith(msg);
    }
}

const SYNTAX_ERROR = "Syntax error";
const UNKNOWN_OPERATION = "Unknown operation";

const requireValidQuestionSyntax = (input, prefix) => {
  requireOrError(input.startsWith(prefix) && input.endsWith("?"), UNKNOWN_OPERATION);
};


const splitSeparatedBySpaces = (input, prefix) => input.slice(prefix.length, -1).trim().split(/\s+/);


const requireNonEmptyTokens = (initial) => requireOrError(initial, SYNTAX_ERROR);


const requireStartWithNumber = (initial) =>
  requireOrError(isNumber(initial), OPERATORS.has(initial) ? SYNTAX_ERROR: UNKNOWN_OPERATION);

const QUESTION_PREFIX = "What is";

const validateAndParseTokens = (input) => {
    requireValidQuestionSyntax(input, QUESTION_PREFIX);

    const [initial, ...rest] = splitSeparatedBySpaces(input, QUESTION_PREFIX);

    requireNonEmptyTokens(initial);

    requireStartWithNumber(initial);

  return [Number(initial), rest];
};

const isOneOf = (candidate, ...values) => values.indexOf(candidate) != -1;

const parseBinary = (tokens) => {
  const [operator, ...rest] = tokens;
    if (!isOneOf(operator, "plus", "minus")) {
        return null;
    }
  const [operand, ...remaining] = rest;
  if (!isNumber(operand)) {
    return errorWith(SYNTAX_ERROR);
  }
  return [[operator, Number(operand)], remaining];
};

const parseTernary = (tokens) => {
  const [operator, by, operand, ...rest] = tokens;
  if (operator !== "multiplied" && operator !== "divided") {
    return null;
  }
  if (by !== "by" || !isNumber(operand)) {
    return errorWith(SYNTAX_ERROR);
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
  const [initial, tokens] = validateAndParseTokens(input);

  const operations = parseOperations(tokens);

  return operations.reduce(
    (result, [operator, operand]) => OPERATION_FUNCTIONS[operator](result, operand),
    initial,
  );
};
