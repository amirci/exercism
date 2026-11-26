export function parse(phrase: string): string {
  return phrase
    .replace(/[^\p{L}\p{N}\s-]/gu, "")
    .replace(/(\p{Ll})(\p{Lu})/gu, "$1 $2")
    .split(/[\s-]+/)
    .map((word) => Array.from(word)[0]?.toUpperCase() ?? "")
    .join("");
}
