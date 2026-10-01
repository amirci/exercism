const isNonNegativeInteger = (value: number | bigint): boolean =>
  typeof value === "bigint"
    ? value >= 0n
    : Number.isInteger(value) && value >= 0

const sumOfPoweredDigits = (value: number | bigint): bigint => {
  const digits = value.toString()
  const power = BigInt(digits.length)

  return [...digits].reduce(
    (total, digit) => total + BigInt(digit) ** power,
    0n,
  )
}

export function isArmstrongNumber(number: number | bigint): boolean {
  return isNonNegativeInteger(number) &&
    sumOfPoweredDigits(number) === BigInt(number)
}
