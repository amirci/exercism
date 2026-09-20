const suffixes: Record<number, string> = {
  1: 'st',
  2: 'nd',
  3: 'rd',
}

function ordinalSuffix(number: number): string {
  const lastTwoDigits = number % 100

  return lastTwoDigits >= 11 && lastTwoDigits <= 13
    ? 'th'
    : suffixes[number % 10] ?? 'th'
}

export function format(name: string, number: number): string {
  return `${name}, you are the ${number}${ordinalSuffix(number)} customer we serve today. Thank you!`
}
