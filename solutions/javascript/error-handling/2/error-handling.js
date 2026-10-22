
export const processString = (input) => {
  try {
    requireString(input);

    if (isEmpty(input)) {
      return null;
    }

    requireValidLength(input);

    requireNoMixedLettersAndNumbers(input);

    return input.toUpperCase();
  } catch (error) {
    console.log(error.message);
    throw error;
  }
};

const isEmpty = (input) => input === "";
const hasLetters = (input) => /[A-Za-z]/.test(input);
const hasNumbers = (input) => /\d/.test(input);

function requireString(input) {
  if (typeof input !== "string") {
    throw new TypeError("Input must be a string");
  }
}

function requireValidLength(input) {
  if (input.length < 10 || input.length > 100) {
    throw new RangeError("Input length must be between 10 and 100 characters");
  }
}

function requireNoMixedLettersAndNumbers(input) {
  if (hasLetters(input) && hasNumbers(input)) {
    throw new SyntaxError("Input cannot contain both letters and numbers");
  }
}
