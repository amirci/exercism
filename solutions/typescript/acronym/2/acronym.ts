const firstUnicodeCharacter = (word: string): string =>
  Array.from(word)[0] ?? "";

const REMOVABLE_PUNCTUATION = /[^\p{L}\p{N}\s-]/gu;
const CAMEL_CASE_BOUNDARY = /(\p{Ll})(\p{Lu})/gu;
const WORD_SEPARATOR = /[\s-]+/;

export function parse(phrase: string): string {
  return phrase
    .replace(REMOVABLE_PUNCTUATION, "")
    .replace(CAMEL_CASE_BOUNDARY, "$1 $2")
    .split(WORD_SEPARATOR)
    .map((word) => firstUnicodeCharacter(word).toUpperCase())
    .join("");
}
