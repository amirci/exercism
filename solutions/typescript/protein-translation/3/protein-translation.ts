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

const isTranslatableCodon = (codon: string): codon is ProteinCodon =>
  !isStopCodon(codon) && isProteinCodon(codon);

function* codonsIn(rna: string): Generator<string> {
  for (let index = 0; index < rna.length; index += 3) {
    yield rna.slice(index, index + 3);
  }
}

type TakeWhileResult<TInput, TAccepted extends TInput> = {
  values: TAccepted[];
  firstRejected: TInput | undefined;
};

function takeWhile<TInput, TAccepted extends TInput>(
  items: Iterable<TInput>,
  predicate: (item: TInput) => item is TAccepted,
): TakeWhileResult<TInput, TAccepted> {
  const values: TAccepted[] = [];

  for (const item of items) {
    if (!predicate(item)) return { values, firstRejected: item };
    values.push(item);
  }

  return { values, firstRejected: undefined };
}

export function translate(rna: string): Protein[] {
  const { values: codons, firstRejected } = takeWhile(codonsIn(rna), isTranslatableCodon);

  if (firstRejected && !isStopCodon(firstRejected)) {
    throw new Error("Invalid codon");
  }

  return codons.map((codon) => proteinsByCodon[codon]);
}
