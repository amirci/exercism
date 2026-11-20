type Protein =
  | "Methionine"
  | "Phenylalanine"
  | "Leucine"
  | "Serine"
  | "Tyrosine"
  | "Cysteine"
  | "Tryptophan";

type StopCodon = "UAA" | "UAG" | "UGA";

type Codon =
  | "AUG"
  | "UUU"
  | "UUC"
  | "UUA"
  | "UUG"
  | "UCU"
  | "UCC"
  | "UCA"
  | "UCG"
  | "UAU"
  | "UAC"
  | "UGU"
  | "UGC"
  | "UGG"
  | StopCodon;

type ProteinCodon = Exclude<Codon, StopCodon>;

type CodonsByProtein = Record<Protein, readonly ProteinCodon[]>;

const codonsByProtein: CodonsByProtein = {
  Methionine: ["AUG"],
  Phenylalanine: ["UUU", "UUC"],
  Leucine: ["UUA", "UUG"],
  Serine: ["UCU", "UCC", "UCA", "UCG"],
  Tyrosine: ["UAU", "UAC"],
  Cysteine: ["UGU", "UGC"],
  Tryptophan: ["UGG"],
};

const proteinsByCodon: Record<ProteinCodon, Protein> = Object.fromEntries(
  (Object.keys(codonsByProtein) as Protein[]).flatMap((protein) =>
    codonsByProtein[protein].map((codon) => [codon, protein] as const),
  ),
) as Record<ProteinCodon, Protein>;

const stopCodons: ReadonlySet<StopCodon> = new Set<StopCodon>(["UAA", "UAG", "UGA"]);

const isStopCodon = (codon: string): codon is StopCodon =>
  stopCodons.has(codon as StopCodon);

const isProteinCodon = (codon: string): codon is ProteinCodon =>
  Object.prototype.hasOwnProperty.call(proteinsByCodon, codon);

function* codonsIn(rna: string): Generator<string> {
  for (let index = 0; index < rna.length; index += 3) {
    yield rna.slice(index, index + 3);
  }
}

export function translate(rna: string): Protein[] {
  const proteins: Protein[] = [];

  for (const codon of codonsIn(rna)) {
    if (isStopCodon(codon)) break;
    if (!isProteinCodon(codon)) throw new Error("Invalid codon");
    proteins.push(proteinsByCodon[codon]);
  }

  return proteins;
}
