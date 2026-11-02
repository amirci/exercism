const RNA_COMPLEMENTS: Record<string, string> = {
  G: "C",
  C: "G",
  T: "A",
  A: "U",
};

export function toRna(dnaString: string): string {
  return Array.from(dnaString, nucleotide => {
    const complement = RNA_COMPLEMENTS[nucleotide];
    if (complement === undefined) {
      throw new Error("Invalid input DNA.");
    }
    return complement;
  }).join("");
}
