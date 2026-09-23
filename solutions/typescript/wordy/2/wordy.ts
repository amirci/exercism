type Operator = "plus" | "minus" | "multiplied" | "divided"

type Operation = readonly [Operator, number]

type ParsedOperation = readonly [Operation, string[]]

type BinaryOperation = (left: number, right: number) => number

const operators: ReadonlySet<string> = new Set(["plus", "minus", "multiplied", "divided"])

const operationFunctions: Record<Operator, BinaryOperation> = {
  plus: (left, right) => left + right,
  minus: (left, right) => left - right,
  multiplied: (left, right) => left * right,
  divided: (left, right) => left / right,
}

const isNumber = (token: string | undefined): token is string => /^-?\d+$/.test(token ?? "")

const syntaxError = (): never => {
  throw new Error("Syntax error")
}

const isOperator = (token: string | undefined): token is Operator =>
  token !== undefined && operators.has(token)

const throwTokenError = (token: string | undefined, operatorIsSyntax = false): never => {
  const isSyntaxError = isNumber(token) || (operatorIsSyntax && isOperator(token))
  throw new Error(isSyntaxError ? "Syntax error" : "Unknown operation")
}

const validateQuestion = (input: string, prefix: string): void => {
  if (!input.startsWith(prefix) || !input.endsWith("?")) {
    throw new Error("Unknown operation")
  }
}

const parseTokensAndValidate = (input: string, prefix: string): [number, string[]] => {
  const [initial, ...rest] = input.slice(prefix.length, -1).trim().split(/\s+/)

  if (!initial) {
    return syntaxError()
  }
  if (!isNumber(initial)) {
    return throwTokenError(initial, true)
  }

  return [Number(initial), rest]
}

const parseBinary = (tokens: string[]): ParsedOperation | null => {
  const [operator, operand, ...rest] = tokens
  if (operator !== "plus" && operator !== "minus") {
    return null
  }
  if (!isNumber(operand)) {
    return syntaxError()
  }

  return [[operator, Number(operand)], rest]
}

const parseTernary = (tokens: string[]): ParsedOperation | null => {
  const [operator, by, operand, ...rest] = tokens
  if (operator !== "multiplied" && operator !== "divided") {
    return null
  }
  if (by !== "by" || !isNumber(operand)) {
    return syntaxError()
  }

  return [[operator, Number(operand)], rest]
}

const unknownOperation = (tokens: string[]): never => {
  return throwTokenError(tokens[0])
}

const parseOperation = (tokens: string[]): ParsedOperation =>
  parseBinary(tokens) ?? parseTernary(tokens) ?? unknownOperation(tokens)

const parseOperations = (tokens: string[]): Operation[] => {
  const operations: Operation[] = []
  let remaining = tokens

  while (remaining.length > 0) {
    const [operation, rest] = parseOperation(remaining)
    operations.push(operation)
    remaining = rest
  }

  return operations
}

export const answer = (input: string): number => {
  const prefix = "What is"
  validateQuestion(input, prefix)

  const [initial, tokens] = parseTokensAndValidate(input, prefix)
  return parseOperations(tokens).reduce(
    (result, [operator, operand]) => operationFunctions[operator](result, operand),
    initial,
  )
}
