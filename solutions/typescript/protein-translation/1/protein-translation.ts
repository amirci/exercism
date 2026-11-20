const proteinsByCodon = new Map<string, string>([
  ["AUG", "Methionine"],
  ["UUU", "Phenylalanine"],
  ["UUC", "Phenylalanine"],
  ["UUA", "Leucine"],
  ["UUG", "Leucine"],
  ["UCU", "Serine"],
  ["UCC", "Serine"],
  ["UCA", "Serine"],
  ["UCG", "Serine"],
  ["UAU", "Tyrosine"],
  ["UAC", "Tyrosine"],
  ["UGU", "Cysteine"],
  ["UGC", "Cysteine"],
  ["UGG", "Tryptophan"],
  ["UAA", "STOP"],
  ["UAG", "STOP"],
  ["UGA", "STOP"],
]);

export function translate(rna: string): string[] {
  const proteins: string[] = [];

  for (let index = 0; index < rna.length; index += 3) {
    const codon = rna.slice(index, index + 3);
    const protein = proteinsByCodon.get(codon);

    if (protein === undefined) throw new Error("Invalid codon");
    if (protein === "STOP") return proteins;

    proteins.push(protein);
  }

  return proteins;
}
