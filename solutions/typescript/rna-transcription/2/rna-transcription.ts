type RnaNucleotide = "A" | "C" | "G" | "U";
type RnaComplement = RnaNucleotide | undefined;

const RNA_COMPLEMENTS: Partial<Record<string, RnaNucleotide>> = {
  G: "C",
  C: "G",
  T: "A",
  A: "U",
};

const areAllValid = (complements: RnaComplement[]): boolean =>
  complements.every(complement => complement !== undefined);

export function toRna(dnaString: string): string {
  const result = Array.from(dnaString).map(nucleotide => RNA_COMPLEMENTS[nucleotide]);

  if (!areAllValid(result)) {
    throw new Error("Invalid input DNA.");
  }

  return result.join("");
}
