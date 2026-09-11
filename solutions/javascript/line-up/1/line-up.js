const SUFFIXES = {
  1: 'st',
  2: 'nd',
  3: 'rd',
};

function suffixFor(number) {
  const lastTwoDigits = number % 100;
  if (lastTwoDigits >= 11 && lastTwoDigits <= 13) {
    return 'th';
  }

  return SUFFIXES[number % 10] ?? 'th';
}

export const format = (name, number) =>
  `${name}, you are the ${number}${suffixFor(number)} customer we serve today. Thank you!`;
